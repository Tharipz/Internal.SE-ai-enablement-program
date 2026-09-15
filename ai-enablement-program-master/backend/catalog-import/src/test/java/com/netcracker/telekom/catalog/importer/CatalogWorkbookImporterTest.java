package com.netcracker.telekom.catalog.importer;

import com.netcracker.telekom.catalog.core.model.CatalogDocument;
import com.netcracker.telekom.catalog.core.model.CatalogProduct;
import com.netcracker.telekom.catalog.core.model.ProductStatus;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CatalogWorkbookImporterTest {
    @Test
    void importsProductsPricesWarningsAndTraceability() {
        Path workbook = locateWorkbook();
        CatalogWorkbookImporter importer = new CatalogWorkbookImporter("Netcracker Telekom", LocalDate.of(2026, 4, 26));

        CatalogDocument document = importer.importWorkbook(workbook);

        assertEquals(18, document.getProducts().size());
        assertEquals("Netcracker Telekom", document.getMetadata().getOperatorName());
        assertTrue(document.getMetadata().getWarningCount() > 0);

        CatalogProduct product = document.getProducts().stream()
                .filter(item -> "iPhone 14 Midnight 128GB".equals(item.getName()))
                .findFirst()
                .orElseThrow();
        assertEquals("Equipment Offerings", product.getSourceSheet());
        assertEquals(3, product.getSourceRow());
        assertEquals("USD", product.getCurrency());
        assertFalse(product.getPricingOptions().isEmpty());
        assertFalse(product.getDependencies().isEmpty());
    }

    @Test
    void derivesFutureAndExpiredStatusesFromAvailabilityDates() {
        CatalogDocument document = new CatalogWorkbookImporter("Netcracker Telekom", LocalDate.of(2026, 4, 26))
                .importWorkbook(locateWorkbook());

        CatalogProduct postpaidProduct = find(document, "Postpaid Unlimited 299");
        CatalogProduct expiredProduct = find(document, "iPhone 11 64GB White");

        assertEquals(ProductStatus.ACTIVE, postpaidProduct.getStatus());
        assertEquals(ProductStatus.EXPIRED, expiredProduct.getStatus());
    }

    private CatalogProduct find(CatalogDocument document, String name) {
        return document.getProducts().stream()
                .filter(item -> name.equals(item.getName()))
                .findFirst()
                .orElseThrow();
    }

    private Path locateWorkbook() {
        for (Path candidate : new Path[]{
                Path.of("../../catalog/mobile-operator-catalog.xlsx"),
                Path.of("../catalog/mobile-operator-catalog.xlsx"),
                Path.of("catalog/mobile-operator-catalog.xlsx")
        }) {
            if (Files.exists(candidate)) {
                return candidate;
            }
        }
        assertNotNull(null, "catalog/mobile-operator-catalog.xlsx not found");
        return Path.of("catalog/mobile-operator-catalog.xlsx");
    }
}
