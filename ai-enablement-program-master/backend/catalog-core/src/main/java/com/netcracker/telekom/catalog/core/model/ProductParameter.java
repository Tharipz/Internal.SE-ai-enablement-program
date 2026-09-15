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
public class ProductParameter {
    private String name;

    @Builder.Default
    private List<String> values = new ArrayList<>();

    private boolean visible;
    private boolean mandatory;
    private boolean modifiable;
}
