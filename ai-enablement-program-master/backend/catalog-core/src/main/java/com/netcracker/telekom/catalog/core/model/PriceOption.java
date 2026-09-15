package com.netcracker.telekom.catalog.core.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PriceOption {
    private String id;
    private String component;
    private SaleType saleType;
    private BigDecimal value;
    private String currency;
    private boolean defaultPrice;
    private boolean basePrice;
    private String period;
    private LocalDate validFrom;
    private LocalDate validTo;
}
