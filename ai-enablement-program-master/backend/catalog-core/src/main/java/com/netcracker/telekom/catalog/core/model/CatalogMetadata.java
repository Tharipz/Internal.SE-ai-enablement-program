package com.netcracker.telekom.catalog.core.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CatalogMetadata {
    private String operatorName;
    private String sourceFile;
    private OffsetDateTime importedAt;
    private int productCount;
    private int warningCount;

    @Builder.Default
    private List<String> sourceSheets = new ArrayList<>();

    @Builder.Default
    private Map<String, List<String>> availableFilters = new TreeMap<>();
}
