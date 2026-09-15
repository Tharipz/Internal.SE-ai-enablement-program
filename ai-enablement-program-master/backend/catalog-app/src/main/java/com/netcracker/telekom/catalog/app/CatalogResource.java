package com.netcracker.telekom.catalog.app;

import com.netcracker.telekom.catalog.api.dto.CatalogMetadataDto;
import com.netcracker.telekom.catalog.api.dto.CategoryDto;
import com.netcracker.telekom.catalog.api.dto.ProductDetailDto;
import com.netcracker.telekom.catalog.api.dto.ProductPageDto;
import com.netcracker.telekom.catalog.api.mapper.CatalogMapper;
import com.netcracker.telekom.catalog.core.model.ProductStatus;
import com.netcracker.telekom.catalog.core.model.ProductType;
import com.netcracker.telekom.catalog.core.service.CatalogQuery;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.util.List;

@Path("/api/catalog")
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Catalog", description = "Netcracker Telekom demo catalog API")
public class CatalogResource {
    private final CatalogRuntimeService runtimeService;
    private final CatalogMapper mapper = Mappers.getMapper(CatalogMapper.class);

    public CatalogResource(CatalogRuntimeService runtimeService) {
        this.runtimeService = runtimeService;
    }

    @GET
    @Path("/products")
    @Operation(summary = "Search and filter catalog products")
    public ProductPageDto products(@QueryParam("search") String search,
                                   @QueryParam("category") String category,
                                   @QueryParam("status") String status,
                                   @QueryParam("type") String type,
                                   @QueryParam("priceFrom") BigDecimal priceFrom,
                                   @QueryParam("priceTo") BigDecimal priceTo,
                                   @QueryParam("page") @DefaultValue("0") int page,
                                   @QueryParam("pageSize") @DefaultValue("20") int pageSize) {
        CatalogQuery query = CatalogQuery.builder()
                .search(search)
                .category(category)
                .status(parseEnum(status, ProductStatus.class, "status"))
                .type(parseEnum(type, ProductType.class, "type"))
                .priceFrom(priceFrom)
                .priceTo(priceTo)
                .page(page)
                .pageSize(pageSize)
                .build();
        return mapper.toPage(runtimeService.getCatalogService().findProducts(query));
    }

    @GET
    @Path("/products/{id}")
    @Operation(summary = "Get product details")
    public ProductDetailDto product(@PathParam("id") String id) {
        return runtimeService.getCatalogService()
                .findProduct(id)
                .map(mapper::toDetail)
                .orElseThrow(() -> new NotFoundException("Product not found: " + id));
    }

    @GET
    @Path("/categories")
    @Operation(summary = "List catalog categories")
    public List<CategoryDto> categories() {
        return mapper.toCategories(runtimeService.getCatalogService().categories());
    }

    @GET
    @Path("/metadata")
    @Operation(summary = "Get catalog import metadata and available filters")
    public CatalogMetadataDto metadata() {
        return mapper.toMetadata(runtimeService.getCatalogService().metadata());
    }

    private <T extends Enum<T>> T parseEnum(String value, Class<T> enumType, String parameter) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return Enum.valueOf(enumType, value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid " + parameter + " value: " + value);
        }
    }
}
