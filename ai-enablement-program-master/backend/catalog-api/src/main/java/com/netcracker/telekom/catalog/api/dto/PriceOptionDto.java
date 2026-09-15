package com.netcracker.telekom.catalog.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PriceOptionDto {
    private String id;
    private String component;
    private String saleType;
    private BigDecimal value;
    private String currency;
    private boolean defaultPrice;
    private boolean basePrice;
    private String period;
    private LocalDate validFrom;
    private LocalDate validTo;
}
