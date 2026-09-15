package com.netcracker.telekom.catalog.core.service;

import com.netcracker.telekom.catalog.core.model.ProductStatus;
import com.netcracker.telekom.catalog.core.model.ProductType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CatalogQuery {
    private String search;
    private String category;
    private ProductStatus status;
    private ProductType type;
    private BigDecimal priceFrom;
    private BigDecimal priceTo;

    @Builder.Default
    private int page = 0;

    @Builder.Default
    private int pageSize = 20;
}
