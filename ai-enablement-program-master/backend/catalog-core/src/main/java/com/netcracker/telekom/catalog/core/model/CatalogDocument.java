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
public class CatalogDocument {
    @Builder.Default
    private CatalogMetadata metadata = new CatalogMetadata();

    @Builder.Default
    private List<CatalogProduct> products = new ArrayList<>();

    @Builder.Default
    private List<CatalogCategory> categories = new ArrayList<>();

    @Builder.Default
    private CatalogEnums enums = new CatalogEnums();

    @Builder.Default
    private List<ValidationIssue> warnings = new ArrayList<>();
}
