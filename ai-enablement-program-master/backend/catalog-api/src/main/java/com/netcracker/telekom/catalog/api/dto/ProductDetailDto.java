package com.netcracker.telekom.catalog.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetailDto {
    private String id;
    private String code;
    private String name;
    private String description;
    private String category;
    private String type;
    private String status;
    private BigDecimal price;
    private String currency;
    private String period;
    private LocalDate availableFrom;
    private LocalDate availableTo;
    private boolean technical;
    private List<PriceOptionDto> pricingOptions = new ArrayList<>();
    private List<ProductParameterDto> parameters = new ArrayList<>();
    private List<String> conditions = new ArrayList<>();
    private List<ProductDependencyDto> dependencies = new ArrayList<>();
    private String sourceSheet;
    private int sourceRow;
    private String sourceFile;
    private OffsetDateTime importedAt;
    private List<ValidationIssueDto> validationIssues = new ArrayList<>();
}
