package com.netcracker.telekom.catalog.core.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CatalogEnums {
    @Builder.Default
    private List<String> productTypes = new ArrayList<>();

    @Builder.Default
    private List<String> statuses = new ArrayList<>();

    @Builder.Default
    private List<String> saleTypes = new ArrayList<>();

    @Builder.Default
    private List<String> dependencyTypes = new ArrayList<>();

    @Builder.Default
    private List<String> currencies = new ArrayList<>();
}
