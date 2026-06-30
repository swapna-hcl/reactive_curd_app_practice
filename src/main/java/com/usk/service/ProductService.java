package com.usk.service;

import com.usk.dto.SearchProductResponseDto;
import com.usk.entity.Product;
import com.usk.exception.ProductNotFoundException;
import com.usk.repository.ProductRepository;
import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.quarkus.panache.common.Page;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class ProductService {
    @Inject
    ProductRepository productRepository;


    @WithSession
    public Uni<List<SearchProductResponseDto>> searchProducts(String searchText, int page, int size) {
        // Reactive query - returns Uni<List<Product>>
        return productRepository.find(
                        "LOWER(name) LIKE LOWER(?1)", "%" + searchText + "%")
                .page(Page.of(page, size))
                .list()  // Returns Uni<List<Product>>
                // Transform the result when it arrives (reactive transformation)
                .onItem().transform(products -> {

                    return products.stream()
                            .map(this::convertToResponseDto)
                            .collect(Collectors.toList());
                    });
    }

    private SearchProductResponseDto convertToResponseDto(Product product) {
        SearchProductResponseDto dto = new SearchProductResponseDto();
        dto.id = product.id;
        dto.name = product.name;
        dto.price = product.price;
        dto.totalPages = productRepository.count("LOWER(name) LIKE LOWER(?1)", "%" + product.name + "%")
                .onItem().transform(count -> (int) Math.ceil((double) count / 10)); // Assuming page size of 10
        dto.totalCount = productRepository.count("LOWER(name) LIKE LOWER(?1)", "%" + product.name + "%")
                .onItem().transform(count -> count.intValue());
        return dto;
    }

}
