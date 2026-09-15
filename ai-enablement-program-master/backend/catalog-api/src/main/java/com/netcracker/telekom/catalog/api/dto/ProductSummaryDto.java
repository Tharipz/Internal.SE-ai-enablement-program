package com.netcracker.telekom.catalog.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductSummaryDto {
    private String id;
    private String code;
    private String name;
    private String category;
    private String type;
    private String status;
    private BigDecimal price;
    private String currency;
    private String period;
    private String shortDescription;
    private boolean technical;
    private String sourceSheet;
    private int sourceRow;
}
