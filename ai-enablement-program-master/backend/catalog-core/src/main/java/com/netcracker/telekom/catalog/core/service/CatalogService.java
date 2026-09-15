package com.netcracker.telekom.catalog.core.service;

import com.netcracker.telekom.catalog.core.model.CatalogCategory;
import com.netcracker.telekom.catalog.core.model.CatalogDocument;
import com.netcracker.telekom.catalog.core.model.CatalogMetadata;
import com.netcracker.telekom.catalog.core.model.CatalogProduct;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class CatalogService {
    private final CatalogDocument document;

    public CatalogService(CatalogDocument document) {
        this.document = document;
    }

    public PageResult<CatalogProduct> findProducts(CatalogQuery query) {
        CatalogQuery safe = normalizeQuery(query);
        List<CatalogProduct> filtered = document.getProducts().stream()
                .filter(product -> matchesSearch(product, safe.getSearch()))
                .filter(product -> safe.getCategory() == null || safe.getCategory().equalsIgnoreCase(product.getCategory()))
                .filter(product -> safe.getStatus() == null || safe.getStatus() == product.getStatus())
                .filter(product -> safe.getType() == null || safe.getType() == product.getType())
                .filter(product -> withinPrice(product.getPrice(), safe.getPriceFrom(), safe.getPriceTo()))
                .sorted(Comparator.comparing(CatalogProduct::getName, Comparator.nullsLast(String::compareToIgnoreCase)))
                .toList();

        int from = Math.min(safe.getPage() * safe.getPageSize(), filtered.size());
        int to = Math.min(from + safe.getPageSize(), filtered.size());
        return PageResult.<CatalogProduct>builder()
                .items(filtered.subList(from, to))
                .page(safe.getPage())
                .pageSize(safe.getPageSize())
                .total(filtered.size())
                .build();
    }

    public Optional<CatalogProduct> findProduct(String id) {
        if (id == null || id.isBlank()) {
            return Optional.empty();
        }
        return document.getProducts().stream()
                .filter(product -> id.equalsIgnoreCase(product.getId()) || id.equalsIgnoreCase(product.getCode()))
                .findFirst();
    }

    public List<CatalogCategory> categories() {
        return document.getCategories();
    }

    public CatalogMetadata metadata() {
        return document.getMetadata();
    }

    public CatalogDocument document() {
        return document;
    }

    private CatalogQuery normalizeQuery(CatalogQuery query) {
        CatalogQuery safe = query == null ? new CatalogQuery() : query;
        int page = Math.max(0, safe.getPage());
        int pageSize = Math.max(1, Math.min(100, safe.getPageSize()));
        safe.setPage(page);
        safe.setPageSize(pageSize);
        if (safe.getSearch() != null && safe.getSearch().isBlank()) {
            safe.setSearch(null);
        }
        if (safe.getCategory() != null && safe.getCategory().isBlank()) {
            safe.setCategory(null);
        }
        return safe;
    }

    private boolean matchesSearch(CatalogProduct product, String search) {
        if (search == null) {
            return true;
        }
        String needle = search.toLowerCase(Locale.ROOT);
        return contains(product.getName(), needle)
                || contains(product.getCode(), needle)
                || contains(product.getDescription(), needle)
                || contains(product.getCategory(), needle)
                || product.getParameters().stream().anyMatch(parameter ->
                contains(parameter.getName(), needle) || parameter.getValues().stream().anyMatch(value -> contains(value, needle)));
    }

    private boolean withinPrice(BigDecimal price, BigDecimal from, BigDecimal to) {
        if (price == null) {
            return from == null && to == null;
        }
        if (from != null && price.compareTo(from) < 0) {
            return false;
        }
        return to == null || price.compareTo(to) <= 0;
    }

    private boolean contains(String value, String needle) {
        return value != null && value.toLowerCase(Locale.ROOT).contains(needle);
    }
}
