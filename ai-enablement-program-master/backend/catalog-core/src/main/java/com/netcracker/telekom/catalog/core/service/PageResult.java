package com.netcracker.telekom.catalog.core.service;

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
public class PageResult<T> {
    @Builder.Default
    private List<T> items = new ArrayList<>();

    private int page;
    private int pageSize;
    private long total;
}
