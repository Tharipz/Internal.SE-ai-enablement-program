package com.netcracker.telekom.catalog.app;

import io.smallrye.config.ConfigMapping;

@ConfigMapping(prefix = "catalog")
public interface CatalogConfig {
    String dataPath();
}
