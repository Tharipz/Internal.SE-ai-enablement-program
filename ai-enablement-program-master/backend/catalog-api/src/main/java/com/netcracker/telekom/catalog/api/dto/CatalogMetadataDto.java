package com.netcracker.telekom.catalog.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CatalogMetadataDto {
    private String operatorName;
    private String sourceFile;
    private OffsetDateTime importedAt;
    private int productCount;
    private int warningCount;
    private List<String> sourceSheets = new ArrayList<>();
    private Map<String, List<String>> availableFilters = new TreeMap<>();
}
