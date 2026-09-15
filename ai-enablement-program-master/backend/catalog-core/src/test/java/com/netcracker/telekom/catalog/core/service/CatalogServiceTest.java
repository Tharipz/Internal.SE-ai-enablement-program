package com.netcracker.telekom.catalog.core.service;

import com.netcracker.telekom.catalog.core.model.CatalogDocument;
import com.netcracker.telekom.catalog.core.model.CatalogProduct;
import com.netcracker.telekom.catalog.core.model.ProductStatus;
import com.netcracker.telekom.catalog.core.model.ProductType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CatalogServiceTest {
    @Test
    void filtersProductsBySearchCategoryStatusAndPrice() {
        CatalogService service = new CatalogService(CatalogDocument.builder()
                .products(List.of(
                        product("1", "NT FiberPhone X1", "Smartphones", ProductType.SMARTPHONE, ProductStatus.ACTIVE, "799"),
                        product("2", "NT 5G Home Router Pro", "Routers", ProductType.ROUTER, ProductStatus.ACTIVE, "299"),
                        product("3", "NT Legacy Phone Mini", "Smartphones", ProductType.SMARTPHONE, ProductStatus.EXPIRED, "99")
                ))
                .build());

        PageResult<CatalogProduct> result = service.findProducts(CatalogQuery.builder()
                .search("phone")
                .category("Smartphones")
                .status(ProductStatus.ACTIVE)
                .priceFrom(new BigDecimal("500"))
                .priceTo(new BigDecimal("900"))
                .build());

        assertEquals(1, result.getTotal());
        assertEquals("NT FiberPhone X1", result.getItems().get(0).getName());
    }

    @Test
    void returnsEmptyPageWhenPriceFilterExcludesProductsWithoutPrice() {
        CatalogService service = new CatalogService(CatalogDocument.builder()
                .products(List.of(CatalogProduct.builder()
                        .id("no-price")
                        .name("No Price Product")
                        .category("Other")
                        .type(ProductType.OTHER)
                        .status(ProductStatus.ACTIVE)
                        .build()))
                .build());

        PageResult<CatalogProduct> result = service.findProducts(CatalogQuery.builder()
                .priceFrom(BigDecimal.ONE)
                .build());

        assertTrue(result.getItems().isEmpty());
    }

    private CatalogProduct product(String id, String name, String category, ProductType type, ProductStatus status, String price) {
        return CatalogProduct.builder()
                .id(id)
                .name(name)
                .category(category)
                .type(type)
                .status(status)
                .price(new BigDecimal(price))
                .build();
    }
}
