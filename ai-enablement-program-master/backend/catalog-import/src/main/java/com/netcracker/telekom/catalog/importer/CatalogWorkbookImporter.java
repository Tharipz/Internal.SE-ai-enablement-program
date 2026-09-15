package com.netcracker.telekom.catalog.importer;

import com.netcracker.telekom.catalog.core.model.CatalogCategory;
import com.netcracker.telekom.catalog.core.model.CatalogDocument;
import com.netcracker.telekom.catalog.core.model.CatalogEnums;
import com.netcracker.telekom.catalog.core.model.CatalogMetadata;
import com.netcracker.telekom.catalog.core.model.CatalogProduct;
import com.netcracker.telekom.catalog.core.model.DependencyType;
import com.netcracker.telekom.catalog.core.model.PriceOption;
import com.netcracker.telekom.catalog.core.model.ProductDependency;
import com.netcracker.telekom.catalog.core.model.ProductParameter;
import com.netcracker.telekom.catalog.core.model.ProductStatus;
import com.netcracker.telekom.catalog.core.model.ProductType;
import com.netcracker.telekom.catalog.core.model.SaleType;
import com.netcracker.telekom.catalog.core.model.Severity;
import com.netcracker.telekom.catalog.core.model.ValidationIssue;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CatalogWorkbookImporter {
    private static final List<String> PRODUCT_SHEETS = List.of(
            "Equipment Offerings",
            "Non-serialized Equipment",
            "Other Offerings"
    );
    private static final String PRICE_SHEET = "Price List Items";
    private static final String RELATION_SHEET = "Relations Overrides";
    private static final String VALUES_SHEET = "Available Values";
    private static final String CHARACTERISTICS_SHEET = "Characteristics Overrides";
    private static final String FLAT_RULES_SHEET = "Flat Rules";
    private static final DateTimeFormatter DOT_DATE = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private final String operatorName;
    private final LocalDate asOfDate;
    private final OffsetDateTime importedAt;
    private final DataFormatter formatter = new DataFormatter(Locale.ROOT);

    public CatalogWorkbookImporter(String operatorName, LocalDate asOfDate) {
        this.operatorName = operatorName;
        this.asOfDate = asOfDate;
        this.importedAt = OffsetDateTime.now(ZoneOffset.UTC).withNano(0);
    }

    public CatalogDocument importWorkbook(Path sourceFile) {
        try (InputStream input = Files.newInputStream(sourceFile);
             Workbook workbook = WorkbookFactory.create(input)) {
            List<ValidationIssue> warnings = new ArrayList<>();
            LinkedHashMap<String, CatalogProduct> productsByName = new LinkedHashMap<>();

            for (String sheetName : PRODUCT_SHEETS) {
                Sheet sheet = workbook.getSheet(sheetName);
                if (sheet == null) {
                    warnings.add(issue(Severity.WARNING, "SHEET_MISSING", "Expected product sheet is missing", sheetName, 0, ""));
                    continue;
                }
                importProducts(sourceFile, sheet, productsByName, warnings);
            }

            importPrices(workbook.getSheet(PRICE_SHEET), productsByName, warnings);
            importAvailableValues(workbook.getSheet(VALUES_SHEET), productsByName);
            importCharacteristicOverrides(workbook.getSheet(CHARACTERISTICS_SHEET), productsByName);
            importRelations(workbook.getSheet(RELATION_SHEET), productsByName);
            importFlatRules(workbook.getSheet(FLAT_RULES_SHEET), productsByName);

            List<CatalogProduct> products = new ArrayList<>(productsByName.values());
            products.forEach(this::finalizeProduct);

            List<CatalogCategory> categories = buildCategories(products);
            CatalogEnums enums = buildEnums(products);
            CatalogMetadata metadata = buildMetadata(sourceFile, workbook, products, warnings, enums);

            return CatalogDocument.builder()
                    .metadata(metadata)
                    .products(products)
                    .categories(categories)
                    .enums(enums)
                    .warnings(warnings)
                    .build();
        } catch (IOException e) {
            throw new CatalogImportException("Cannot read catalog workbook " + sourceFile, e);
        }
    }

    private void importProducts(Path sourceFile, Sheet sheet, Map<String, CatalogProduct> productsByName, List<ValidationIssue> warnings) {
        Map<String, Integer> headers = headers(sheet);
        for (int rowIndex = 2; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
            Row row = sheet.getRow(rowIndex);
            if (isBlank(row)) {
                continue;
            }

            int sourceRow = rowIndex + 1;
            String offeringId = cell(row, headers, "Offering Id");
            String name = firstNonBlank(cell(row, headers, "Display Name"), cell(row, headers, "Offering Name"));
            if (offeringId.isBlank() || name.isBlank()) {
                warnings.add(issue(Severity.ERROR, "PRODUCT_SKIPPED", "Product row does not have both id and name", sheet.getSheetName(), sourceRow, "Offering Id/Offering Name"));
                continue;
            }

            String productFamily = cell(row, headers, "Product Family");
            String category = deriveCategory(cell(row, headers, "Categories"), cell(row, headers, "Technical Categories"), productFamily);
            boolean technical = isTechnical(sheet.getSheetName(), productFamily, cell(row, headers, "Tags"));
            CatalogProduct product = CatalogProduct.builder()
                    .id(offeringId)
                    .code(firstNonBlank(cell(row, headers, "External Id"), cell(row, headers, "SKU ID"), offeringId))
                    .name(name)
                    .description(cell(row, headers, "Description"))
                    .category(category)
                    .type(toProductType(productFamily, cell(row, headers, "Offering Template"), category, technical))
                    .availableFrom(parseDate(cell(row, headers, "Available From")).orElse(null))
                    .availableTo(parseDate(cell(row, headers, "Available To")).orElse(null))
                    .status(deriveStatus(
                            cell(row, headers, "Available From"),
                            cell(row, headers, "Available To"),
                            cell(row, headers, "Eliminated From"),
                            cell(row, headers, "Archived From")
                    ))
                    .technical(technical)
                    .sourceSheet(sheet.getSheetName())
                    .sourceRow(sourceRow)
                    .sourceFile(sourceFile.getFileName().toString())
                    .importedAt(importedAt)
                    .build();

            addParameter(product, "SKU ID", cell(row, headers, "SKU ID"), true, false, false);
            addParameter(product, "Product Family", productFamily, true, false, false);
            addParameter(product, "Offering Template", cell(row, headers, "Offering Template"), false, false, false);
            addParameter(product, "Delivery Type", cell(row, headers, "Delivery Type"), true, false, true);
            addParameter(product, "Subsidy Amount", cell(row, headers, "Subsidy Amount"), true, false, false);
            addParameter(product, "Equipment Subsidy Commitment Period", cell(row, headers, "Equipment Subsidy Commitment Period"), true, false, true);

            if (productFamily.isBlank()) {
                product.getValidationIssues().add(issue(Severity.WARNING, "PRODUCT_FAMILY_MISSING", "Product family is missing", sheet.getSheetName(), sourceRow, "Product Family"));
            }
            if (product.getDescription() == null || product.getDescription().isBlank()) {
                product.getValidationIssues().add(issue(Severity.WARNING, "DESCRIPTION_MISSING", "Description is missing", sheet.getSheetName(), sourceRow, "Description"));
            }
            productsByName.put(name, product);
        }
    }

    private void importPrices(Sheet sheet, Map<String, CatalogProduct> productsByName, List<ValidationIssue> warnings) {
        if (sheet == null) {
            warnings.add(issue(Severity.WARNING, "SHEET_MISSING", "Price sheet is missing", PRICE_SHEET, 0, ""));
            return;
        }
        Map<String, Integer> headers = headers(sheet);
        for (int rowIndex = 2; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
            Row row = sheet.getRow(rowIndex);
            if (isBlank(row)) {
                continue;
            }
            int sourceRow = rowIndex + 1;
            String offeringName = cell(row, headers, "Offering Name");
            CatalogProduct product = productsByName.get(offeringName);
            if (product == null) {
                warnings.add(issue(Severity.WARNING, "PRICE_ORPHAN", "Price row references an unknown offering", PRICE_SHEET, sourceRow, "Offering Name"));
                continue;
            }

            BigDecimal value = parseDecimal(cell(row, headers, "Value")).orElse(BigDecimal.ZERO);
            if (value.signum() < 0) {
                product.getValidationIssues().add(issue(Severity.WARNING, "NEGATIVE_PRICE", "Negative price is usually a technical refund/reversal entry", PRICE_SHEET, sourceRow, "Value"));
            }
            PriceOption option = PriceOption.builder()
                    .id(cell(row, headers, "Price List Item Id"))
                    .component(firstNonBlank(cell(row, headers, "Price Component Specification"), "Price"))
                    .saleType(toSaleType(cell(row, headers, "Sale Type")))
                    .value(value.setScale(2, RoundingMode.HALF_UP).stripTrailingZeros())
                    .currency(firstNonBlank(cell(row, headers, "Currency"), "USD"))
                    .defaultPrice(isYes(cell(row, headers, "Default")))
                    .basePrice(isYes(cell(row, headers, "Base Price")))
                    .period(periodFrom(row, headers))
                    .validFrom(parseDate(cell(row, headers, "Available From")).orElse(null))
                    .validTo(parseDate(cell(row, headers, "Available To")).orElse(null))
                    .build();
            product.getPricingOptions().add(option);
        }
    }

    private void importAvailableValues(Sheet sheet, Map<String, CatalogProduct> productsByName) {
        if (sheet == null) {
            return;
        }
        Map<String, Integer> headers = headers(sheet);
        Map<String, Map<String, List<String>>> valuesByOffering = new LinkedHashMap<>();
        for (int rowIndex = 2; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
            Row row = sheet.getRow(rowIndex);
            if (isBlank(row)) {
                continue;
            }
            String offering = cell(row, headers, "Flat Offering");
            String characteristic = cell(row, headers, "Characteristic Involvement");
            String value = cell(row, headers, "Available Value");
            if (offering.isBlank() || characteristic.isBlank() || value.isBlank()) {
                continue;
            }
            valuesByOffering
                    .computeIfAbsent(offering, ignored -> new LinkedHashMap<>())
                    .computeIfAbsent(characteristic, ignored -> new ArrayList<>())
                    .add(value);
        }
        valuesByOffering.forEach((offering, values) -> {
            CatalogProduct product = productsByName.get(offering);
            if (product == null) {
                return;
            }
            values.forEach((name, allowedValues) -> addParameter(product, name, String.join(";", distinct(allowedValues)), true, false, true));
        });
    }

    private void importCharacteristicOverrides(Sheet sheet, Map<String, CatalogProduct> productsByName) {
        if (sheet == null) {
            return;
        }
        Map<String, Integer> headers = headers(sheet);
        for (int rowIndex = 2; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
            Row row = sheet.getRow(rowIndex);
            if (isBlank(row)) {
                continue;
            }
            CatalogProduct product = productsByName.get(cell(row, headers, "Flat Offering"));
            if (product == null) {
                continue;
            }
            String characteristic = cell(row, headers, "Characteristic Involvement");
            ProductParameter parameter = findOrCreateParameter(product, characteristic);
            parameter.setVisible(isYes(cell(row, headers, "Visible in OE/CPQ")));
            parameter.setMandatory(isYes(cell(row, headers, "Mandatory")));
            parameter.setModifiable(isYes(cell(row, headers, "Modifiable")));
        }
    }

    private void importRelations(Sheet sheet, Map<String, CatalogProduct> productsByName) {
        if (sheet == null) {
            return;
        }
        Map<String, CatalogProduct> byName = productsByName.values().stream()
                .collect(Collectors.toMap(CatalogProduct::getName, Function.identity(), (a, b) -> a, LinkedHashMap::new));
        Map<String, Integer> headers = headers(sheet);
        for (int rowIndex = 2; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
            Row row = sheet.getRow(rowIndex);
            if (isBlank(row)) {
                continue;
            }
            String parent = cell(row, headers, "Parent");
            String child = cell(row, headers, "Child");
            CatalogProduct product = byName.get(parent);
            if (product == null || child.isBlank()) {
                continue;
            }
            DependencyType type = "Exclude".equalsIgnoreCase(cell(row, headers, "Include Action"))
                    ? DependencyType.EXCLUDE
                    : DependencyType.INCLUDE;
            product.getDependencies().add(ProductDependency.builder()
                    .type(type)
                    .targetId(Optional.ofNullable(byName.get(child)).map(CatalogProduct::getId).orElse(null))
                    .targetName(child)
                    .min(parseInteger(cell(row, headers, "Min")).orElse(null))
                    .max(parseInteger(cell(row, headers, "Max")).orElse(null))
                    .sourceSheet(RELATION_SHEET)
                    .sourceRow(rowIndex + 1)
                    .build());
        }
    }

    private void importFlatRules(Sheet sheet, Map<String, CatalogProduct> productsByName) {
        if (sheet == null) {
            return;
        }
        Map<String, CatalogProduct> byName = productsByName.values().stream()
                .collect(Collectors.toMap(CatalogProduct::getName, Function.identity(), (a, b) -> a, LinkedHashMap::new));
        Map<String, Integer> headers = headers(sheet);
        for (int rowIndex = 2; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
            Row row = sheet.getRow(rowIndex);
            if (isBlank(row)) {
                continue;
            }
            String source = cell(row, headers, "Offerings/ Categories #1");
            CatalogProduct product = byName.get(source);
            if (product == null) {
                continue;
            }
            String target = firstNonBlank(cell(row, headers, "Offerings/ Categories #2"), cell(row, headers, "Auto-applied Offerings"));
            if (target.isBlank()) {
                continue;
            }
            DependencyType type = ruleType(cell(row, headers, "Rule Type"));
            product.getDependencies().add(ProductDependency.builder()
                    .type(type)
                    .targetId(Optional.ofNullable(byName.get(target)).map(CatalogProduct::getId).orElse(null))
                    .targetName(target)
                    .sourceSheet(FLAT_RULES_SHEET)
                    .sourceRow(rowIndex + 1)
                    .build());
            String message = cell(row, headers, "Action Message");
            if (!message.isBlank()) {
                product.getConditions().add(message);
            }
        }
    }

    private void finalizeProduct(CatalogProduct product) {
        List<PriceOption> visiblePrices = product.getPricingOptions().stream()
                .filter(price -> price.getValue() != null && price.getValue().signum() >= 0)
                .toList();
        if (visiblePrices.isEmpty()) {
            product.getValidationIssues().add(issue(Severity.WARNING, "PRICE_MISSING", "Product does not have a non-negative price option", product.getSourceSheet(), product.getSourceRow(), "Price List Items"));
        } else {
            PriceOption selected = visiblePrices.stream()
                    .filter(price -> price.isDefaultPrice() && price.isBasePrice())
                    .findFirst()
                    .orElse(visiblePrices.get(0));
            product.setPrice(selected.getValue());
            product.setCurrency(selected.getCurrency());
            product.setPeriod(selected.getPeriod());
        }
    }

    private CatalogMetadata buildMetadata(Path sourceFile, Workbook workbook, List<CatalogProduct> products, List<ValidationIssue> warnings, CatalogEnums enums) {
        Map<String, List<String>> filters = new TreeMap<>();
        filters.put("category", products.stream().map(CatalogProduct::getCategory).filter(Objects::nonNull).distinct().sorted().toList());
        filters.put("status", products.stream().map(product -> product.getStatus().name()).distinct().sorted().toList());
        filters.put("type", products.stream().map(product -> product.getType().name()).distinct().sorted().toList());
        filters.put("currency", enums.getCurrencies());

        int issueCount = warnings.size() + products.stream().mapToInt(product -> product.getValidationIssues().size()).sum();
        return CatalogMetadata.builder()
                .operatorName(operatorName)
                .sourceFile(sourceFile.getFileName().toString())
                .importedAt(importedAt)
                .productCount(products.size())
                .warningCount(issueCount)
                .sourceSheets(workbook.sheetIterator().hasNext()
                        ? PRODUCT_SHEETS.stream().filter(name -> workbook.getSheet(name) != null).collect(Collectors.toCollection(ArrayList::new))
                        : new ArrayList<>())
                .availableFilters(filters)
                .build();
    }

    private List<CatalogCategory> buildCategories(List<CatalogProduct> products) {
        Map<String, Long> counts = products.stream()
                .collect(Collectors.groupingBy(CatalogProduct::getCategory, TreeMap::new, Collectors.counting()));
        return counts.entrySet().stream()
                .map(entry -> CatalogCategory.builder()
                        .id(slug(entry.getKey()))
                        .name(entry.getKey())
                        .productCount(entry.getValue().intValue())
                        .build())
                .toList();
    }

    private CatalogEnums buildEnums(List<CatalogProduct> products) {
        CatalogEnums enums = new CatalogEnums();
        enums.setProductTypes(Arrays.stream(ProductType.values()).map(Enum::name).toList());
        enums.setStatuses(Arrays.stream(ProductStatus.values()).map(Enum::name).toList());
        enums.setSaleTypes(Arrays.stream(SaleType.values()).map(Enum::name).toList());
        enums.setDependencyTypes(Arrays.stream(DependencyType.values()).map(Enum::name).toList());
        enums.setCurrencies(products.stream()
                .map(CatalogProduct::getCurrency)
                .filter(Objects::nonNull)
                .distinct()
                .sorted()
                .toList());
        return enums;
    }

    private Map<String, Integer> headers(Sheet sheet) {
        Row headerRow = sheet.getRow(1);
        if (headerRow == null) {
            return Map.of();
        }
        Map<String, Integer> headers = new LinkedHashMap<>();
        for (int i = 0; i < headerRow.getLastCellNum(); i++) {
            String value = formatter.formatCellValue(headerRow.getCell(i)).trim();
            if (!value.isBlank()) {
                headers.put(value, i);
            }
        }
        return headers;
    }

    private String cell(Row row, Map<String, Integer> headers, String header) {
        Integer index = headers.get(header);
        if (index == null || row == null) {
            return "";
        }
        return formatter.formatCellValue(row.getCell(index)).trim();
    }

    private boolean isBlank(Row row) {
        if (row == null) {
            return true;
        }
        for (int i = 0; i < row.getLastCellNum(); i++) {
            if (!formatter.formatCellValue(row.getCell(i)).trim().isBlank()) {
                return false;
            }
        }
        return true;
    }

    private String deriveCategory(String categories, String technicalCategories, String family) {
        List<String> parts = split(firstNonBlank(categories, technicalCategories));
        List<String> filtered = parts.stream()
                .filter(value -> !value.equalsIgnoreCase("Equipment"))
                .toList();
        if (!filtered.isEmpty()) {
            return filtered.get(filtered.size() - 1);
        }
        return firstNonBlank(family, "Other");
    }

    private ProductStatus deriveStatus(String availableFrom, String availableTo, String eliminatedFrom, String archivedFrom) {
        if (parseDate(archivedFrom).isPresent()) {
            return ProductStatus.ARCHIVED;
        }
        if (parseDate(eliminatedFrom).isPresent()) {
            return ProductStatus.ELIMINATED;
        }
        Optional<LocalDate> from = parseDate(availableFrom);
        Optional<LocalDate> to = parseDate(availableTo);
        if (from.isPresent() && from.get().isAfter(asOfDate)) {
            return ProductStatus.FUTURE;
        }
        if (to.isPresent() && to.get().isBefore(asOfDate)) {
            return ProductStatus.EXPIRED;
        }
        return ProductStatus.ACTIVE;
    }

    private ProductType toProductType(String family, String template, String category, boolean technical) {
        String value = (firstNonBlank(family, template, category)).toLowerCase(Locale.ROOT);
        if (technical) {
            return ProductType.TECHNICAL;
        }
        if (value.contains("smartphone")) {
            return ProductType.SMARTPHONE;
        }
        if (value.contains("tablet")) {
            return ProductType.TABLET;
        }
        if (value.contains("watch")) {
            return ProductType.SMARTWATCH;
        }
        if (value.contains("router")) {
            return ProductType.ROUTER;
        }
        if (value.contains("postpaid")) {
            return ProductType.POSTPAID_PLAN;
        }
        if (value.contains("fiber")) {
            return ProductType.FIBER_PLAN;
        }
        if (value.contains("accessory")) {
            return ProductType.ACCESSORY;
        }
        if (value.contains("service")) {
            return ProductType.SERVICE;
        }
        if (value.contains("device")) {
            return ProductType.DEVICE;
        }
        return ProductType.OTHER;
    }

    private boolean isTechnical(String sheetName, String family, String tags) {
        String joined = (sheetName + " " + family + " " + tags).toLowerCase(Locale.ROOT);
        return joined.contains("technical") || joined.contains("hidden");
    }

    private SaleType toSaleType(String value) {
        String normalized = value.toLowerCase(Locale.ROOT);
        if (normalized.contains("cash")) {
            return SaleType.CASH;
        }
        if (normalized.contains("install")) {
            return SaleType.INSTALLMENT;
        }
        if (normalized.contains("subsid")) {
            return SaleType.SUBSIDY;
        }
        if (normalized.contains("postpaid") || normalized.contains("monthly")) {
            return SaleType.POSTPAID;
        }
        return SaleType.OTHER;
    }

    private DependencyType ruleType(String value) {
        String normalized = value.toLowerCase(Locale.ROOT);
        if (normalized.contains("conflict")) {
            return DependencyType.CONFLICTS_WITH;
        }
        if (normalized.contains("require")) {
            return DependencyType.REQUIRES;
        }
        return DependencyType.INCLUDE;
    }

    private String periodFrom(Row row, Map<String, Integer> headers) {
        String plan = cell(row, headers, "Installment Plan Name");
        if (!plan.isBlank()) {
            return plan;
        }
        return "one-time";
    }

    private void addParameter(CatalogProduct product, String name, String rawValues, boolean visible, boolean mandatory, boolean modifiable) {
        if (name == null || name.isBlank() || rawValues == null || rawValues.isBlank()) {
            return;
        }
        ProductParameter parameter = findOrCreateParameter(product, name);
        parameter.setVisible(parameter.isVisible() || visible);
        parameter.setMandatory(parameter.isMandatory() || mandatory);
        parameter.setModifiable(parameter.isModifiable() || modifiable);
        Set<String> values = new LinkedHashSet<>(parameter.getValues());
        values.addAll(split(rawValues));
        parameter.setValues(new ArrayList<>(values));
    }

    private ProductParameter findOrCreateParameter(CatalogProduct product, String name) {
        return product.getParameters().stream()
                .filter(parameter -> parameter.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElseGet(() -> {
                    ProductParameter parameter = ProductParameter.builder()
                            .name(name)
                            .visible(false)
                            .modifiable(false)
                            .build();
                    product.getParameters().add(parameter);
                    return parameter;
                });
    }

    private Optional<LocalDate> parseDate(String raw) {
        if (raw == null || raw.isBlank()) {
            return Optional.empty();
        }
        String value = raw.trim();
        List<DateTimeFormatter> formatters = List.of(DOT_DATE, DateTimeFormatter.ISO_LOCAL_DATE);
        for (DateTimeFormatter dateTimeFormatter : formatters) {
            try {
                return Optional.of(LocalDate.parse(value, dateTimeFormatter));
            } catch (DateTimeParseException ignored) {
                // Try the next supported catalog date format.
            }
        }
        return Optional.empty();
    }

    private Optional<BigDecimal> parseDecimal(String raw) {
        if (raw == null || raw.isBlank()) {
            return Optional.empty();
        }
        try {
            return Optional.of(new BigDecimal(raw.replace(",", "")));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    private Optional<Integer> parseInteger(String raw) {
        if (raw == null || raw.isBlank()) {
            return Optional.empty();
        }
        try {
            return Optional.of(new BigDecimal(raw).intValue());
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    private boolean isYes(String value) {
        return "yes".equalsIgnoreCase(value) || "true".equalsIgnoreCase(value);
    }

    private List<String> split(String value) {
        if (value == null || value.isBlank()) {
            return List.of();
        }
        return Arrays.stream(value.split(";"))
                .map(String::trim)
                .filter(part -> !part.isBlank())
                .toList();
    }

    private List<String> distinct(List<String> values) {
        return values.stream().filter(value -> value != null && !value.isBlank()).distinct().toList();
    }

    private String firstNonBlank(String... values) {
        return Arrays.stream(values)
                .filter(value -> value != null && !value.isBlank())
                .findFirst()
                .orElse("");
    }

    private ValidationIssue issue(Severity severity, String code, String message, String sourceSheet, int sourceRow, String field) {
        return ValidationIssue.builder()
                .severity(severity)
                .code(code)
                .message(message)
                .sourceSheet(sourceSheet)
                .sourceRow(sourceRow)
                .field(field)
                .build();
    }

    private String slug(String value) {
        return value == null ? "" : value.toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("^-|-$", "");
    }
}
