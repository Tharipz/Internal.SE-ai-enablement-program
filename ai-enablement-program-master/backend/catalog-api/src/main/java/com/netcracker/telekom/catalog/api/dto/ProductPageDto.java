package com.netcracker.telekom.catalog.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductPageDto {
    private List<ProductSummaryDto> items = new ArrayList<>();
    private int page;
    private int pageSize;
    private long total;
}
