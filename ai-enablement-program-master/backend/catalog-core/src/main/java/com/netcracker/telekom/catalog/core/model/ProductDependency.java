package com.netcracker.telekom.catalog.core.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDependency {
    private DependencyType type;
    private String targetId;
    private String targetName;
    private Integer min;
    private Integer max;
    private String sourceSheet;
    private int sourceRow;
}
