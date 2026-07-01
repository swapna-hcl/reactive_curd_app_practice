package com.usk.resource;


import com.usk.dto.ProductResponseDto;
import com.usk.dto.SearchProductResponseDto;
import com.usk.service.ProductService;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/products")
public class ProductResouce {
    @Inject
    ProductService productService;


    @GET
    @Path("/search")
    public Uni<SearchProductResponseDto> searchProducts(
            @QueryParam("searchText") String searchText,
            @DefaultValue("1") @QueryParam("page") int page,
            @DefaultValue("10") @QueryParam("size") int size
    ) {
        // Return the reactive Uni directly
        // Quarkus will handle the async execution and JSON serialization
        return productService.searchProducts(searchText, page, size);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<List<ProductResponseDto>> getAllProducts(
            @DefaultValue("0") @QueryParam("page") int page,
            @DefaultValue("10") @QueryParam("size") int size
    ) {
        return productService.getAllProducts(page, size);
    }
}
