package com.netcracker.telekom.catalog.core.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CatalogProduct {
    private String id;
    private String code;
    private String name;
    private String description;
    private String category;
    private ProductType type;
    private ProductStatus status;
    private BigDecimal price;
    private String currency;
    private String period;
    private LocalDate availableFrom;
    private LocalDate availableTo;
    private boolean technical;

    @Builder.Default
    private List<PriceOption> pricingOptions = new ArrayList<>();

    @Builder.Default
    private List<ProductParameter> parameters = new ArrayList<>();

    @Builder.Default
    private List<String> conditions = new ArrayList<>();

    @Builder.Default
    private List<ProductDependency> dependencies = new ArrayList<>();

    private String sourceSheet;
    private int sourceRow;
    private String sourceFile;
    private OffsetDateTime importedAt;

    @Builder.Default
    private List<ValidationIssue> validationIssues = new ArrayList<>();
}
