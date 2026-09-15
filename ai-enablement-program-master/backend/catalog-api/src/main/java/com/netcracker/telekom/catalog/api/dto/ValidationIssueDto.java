package com.netcracker.telekom.catalog.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidationIssueDto {
    private String severity;
    private String code;
    private String message;
    private String sourceSheet;
    private int sourceRow;
    private String field;
}
