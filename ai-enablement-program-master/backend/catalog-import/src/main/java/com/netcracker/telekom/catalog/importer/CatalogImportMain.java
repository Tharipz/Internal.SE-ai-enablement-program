package com.netcracker.telekom.catalog.importer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.netcracker.telekom.catalog.core.model.CatalogDocument;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;

public class CatalogImportMain {
    private static final String DEFAULT_OPERATOR = "Netcracker Telekom";

    public static void main(String[] args) throws IOException {
        if (args.length < 2 || args.length > 3) {
            System.err.println("Usage: CatalogImportMain <input-xlsx> <output-json> [as-of-date:yyyy-MM-dd]");
            System.exit(2);
        }

        Path input = Path.of(args[0]);
        Path output = Path.of(args[1]);
        LocalDate asOfDate = args.length == 3 ? LocalDate.parse(args[2]) : LocalDate.now();

        CatalogWorkbookImporter importer = new CatalogWorkbookImporter(DEFAULT_OPERATOR, asOfDate);
        CatalogDocument document = importer.importWorkbook(input);

        Files.createDirectories(output.getParent());
        ObjectMapper mapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .enable(SerializationFeature.INDENT_OUTPUT);
        mapper.writeValue(output.toFile(), document);

        System.out.printf("Imported %d products from %s into %s%n",
                document.getProducts().size(), input, output);
    }
}
