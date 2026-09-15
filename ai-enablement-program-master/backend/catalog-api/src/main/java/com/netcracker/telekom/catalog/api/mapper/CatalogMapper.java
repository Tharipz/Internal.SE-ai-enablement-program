package com.netcracker.telekom.catalog.api.mapper;

import com.netcracker.telekom.catalog.api.dto.CatalogMetadataDto;
import com.netcracker.telekom.catalog.api.dto.CategoryDto;
import com.netcracker.telekom.catalog.api.dto.ProductDetailDto;
import com.netcracker.telekom.catalog.api.dto.ProductPageDto;
import com.netcracker.telekom.catalog.api.dto.ProductSummaryDto;
import com.netcracker.telekom.catalog.core.model.CatalogCategory;
import com.netcracker.telekom.catalog.core.model.CatalogMetadata;
import com.netcracker.telekom.catalog.core.model.CatalogProduct;
import com.netcracker.telekom.catalog.core.service.PageResult;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper
public interface CatalogMapper {
    @Mapping(target = "type", expression = "java(product.getType() == null ? null : product.getType().name())")
    @Mapping(target = "status", expression = "java(product.getStatus() == null ? null : product.getStatus().name())")
    @Mapping(target = "shortDescription", source = "description", qualifiedByName = "shorten")
    ProductSummaryDto toSummary(CatalogProduct product);

    @Mapping(target = "type", expression = "java(product.getType() == null ? null : product.getType().name())")
    @Mapping(target = "status", expression = "java(product.getStatus() == null ? null : product.getStatus().name())")
    ProductDetailDto toDetail(CatalogProduct product);

    CategoryDto toCategory(CatalogCategory category);

    CatalogMetadataDto toMetadata(CatalogMetadata metadata);

    List<CategoryDto> toCategories(List<CatalogCategory> categories);

    default ProductPageDto toPage(PageResult<CatalogProduct> result) {
        ProductPageDto dto = new ProductPageDto();
        dto.setItems(result.getItems().stream().map(this::toSummary).toList());
        dto.setPage(result.getPage());
        dto.setPageSize(result.getPageSize());
        dto.setTotal(result.getTotal());
        return dto;
    }

    @Named("shorten")
    default String shorten(String value) {
        if (value == null || value.isBlank()) {
            return "";
        }
        return value.length() <= 140 ? value : value.substring(0, 137) + "...";
    }
}
