package com.netcracker.telekom.catalog.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDependencyDto {
    private String type;
    private String targetId;
    private String targetName;
    private Integer min;
    private Integer max;
    private String sourceSheet;
    private int sourceRow;
}
