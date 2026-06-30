package com.usk.resource;


import com.usk.dto.SearchProductResponseDto;
import com.usk.service.ProductService;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;

import java.util.List;

@Path("/products")
public class ProductResouce {
    @Inject
    ProductService productService;

    /**
     * Search products endpoint - Reactive
     * Returns Uni<List> for non-blocking execution
     * 
     * The @GET annotation automatically provides a reactive session
     */
    @GET
    @Path("/search")
    public Uni<List<SearchProductResponseDto>> searchProducts(
            @QueryParam("searchText") String searchText,
            @QueryParam("page") int page,
            @QueryParam("size") int size) {
        // Return the reactive Uni directly
        // Quarkus will handle the async execution and JSON serialization
        return productService.searchProducts(searchText, page, size);
    }
}
