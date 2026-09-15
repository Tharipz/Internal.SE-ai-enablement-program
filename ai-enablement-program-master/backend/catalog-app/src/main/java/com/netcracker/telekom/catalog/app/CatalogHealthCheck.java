package com.netcracker.telekom.catalog.app;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Readiness;

@Readiness
@ApplicationScoped
public class CatalogHealthCheck implements HealthCheck {
    private final CatalogRuntimeService runtimeService;

    public CatalogHealthCheck(CatalogRuntimeService runtimeService) {
        this.runtimeService = runtimeService;
    }

    @Override
    public HealthCheckResponse call() {
        int products = runtimeService.getDocument() == null ? 0 : runtimeService.getDocument().getProducts().size();
        return HealthCheckResponse.named("catalog-data")
                .status(products > 0)
                .withData("products", products)
                .build();
    }
}
