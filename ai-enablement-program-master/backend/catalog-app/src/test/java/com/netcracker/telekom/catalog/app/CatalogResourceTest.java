package com.netcracker.telekom.catalog.app;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.notNullValue;

@QuarkusTest
class CatalogResourceTest {
    @Test
    void returnsCatalogProducts() {
        given()
                .queryParam("pageSize", 5)
                .when()
                .get("/api/catalog/products")
                .then()
                .statusCode(200)
                .body("total", equalTo(18))
                .body("items.size()", equalTo(5))
                .body("items[0].sourceSheet", notNullValue())
                .body("items[0].sourceRow", greaterThanOrEqualTo(3));
    }

    @Test
    void returnsProductDetailsById() {
        given()
                .when()
                .get("/api/catalog/products/100001")
                .then()
                .statusCode(200)
                .body("name", equalTo("iPhone 14 Midnight 128GB"))
                .body("sourceSheet", equalTo("Equipment Offerings"))
                .body("sourceRow", equalTo(3))
                .body("pricingOptions.size()", greaterThanOrEqualTo(1));
    }

    @Test
    void returnsNotFoundForUnknownProduct() {
        given()
                .when()
                .get("/api/catalog/products/not-found")
                .then()
                .statusCode(404);
    }
}
