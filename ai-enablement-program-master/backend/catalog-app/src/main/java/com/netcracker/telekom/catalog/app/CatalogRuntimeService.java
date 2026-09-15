package com.netcracker.telekom.catalog.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.netcracker.telekom.catalog.core.model.CatalogDocument;
import com.netcracker.telekom.catalog.core.service.CatalogService;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.Getter;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@ApplicationScoped
public class CatalogRuntimeService {
    private final CatalogConfig config;
    private final ObjectMapper mapper;

    @Getter
    private CatalogService catalogService;

    @Getter
    private CatalogDocument document;

    public CatalogRuntimeService(CatalogConfig config) {
        this.config = config;
        this.mapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @PostConstruct
    void loadCatalog() {
        try (InputStream input = open(config.dataPath())) {
            document = mapper.readValue(input, CatalogDocument.class);
            catalogService = new CatalogService(document);
        } catch (IOException e) {
            throw new IllegalStateException("Cannot load catalog data from " + config.dataPath(), e);
        }
    }

    private InputStream open(String dataPath) throws IOException {
        if (dataPath.startsWith("classpath:")) {
            String resourceName = dataPath.substring("classpath:".length());
            InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(resourceName);
            if (stream == null) {
                throw new IOException("Classpath resource not found: " + resourceName);
            }
            return stream;
        }
        return Files.newInputStream(Path.of(dataPath));
    }
}
