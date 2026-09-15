package com.netcracker.telekom.catalog.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductParameterDto {
    private String name;
    private List<String> values = new ArrayList<>();
    private boolean visible;
    private boolean mandatory;
    private boolean modifiable;
}
