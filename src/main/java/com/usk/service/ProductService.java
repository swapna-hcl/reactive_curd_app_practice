package com.usk.service;

import com.usk.dto.ProductResponseDto;
import com.usk.dto.SearchProductResponseDto;
import com.usk.entity.Product;
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
    public Uni<SearchProductResponseDto> searchProducts(String searchText, int page, int size) {

        Uni<List<Product>> products = productRepository.searchProducts(searchText, page, size);

        Uni<Long> totalCount = productRepository.countProducts(searchText);

        return Uni.combine().all().unis(products, totalCount).asTuple()
                .onItem().transform(tuple -> {
                    List<Product> productslist = tuple.getItem1();
                    Long total_count = tuple.getItem2();
                    int totalPages = (int) Math.ceil((double) total_count / size);

                    SearchProductResponseDto response = new SearchProductResponseDto();
                    response.setProducts(productslist.stream()
                            .map(this::MapToResponse)
                            .toList());

                    response.setTotalCount(total_count);
                    response.setTotalPages(totalPages);
                    return response;
                });
    }

    private ProductResponseDto MapToResponse(Product product) {
        ProductResponseDto dto = new ProductResponseDto();
        dto.setProductId(product.id);
        dto.setProductName(product.name);
        dto.setProductPrice(product.price);
        return dto;
    }



    //get all products with pagination
    @WithSession
    public Uni<List<ProductResponseDto>> getAllProducts(int page, int size) {
        Uni<List<Product>> products = productRepository.findAll()
                .page(Page.of(page, size))
                .list();

        Uni<Long> totalCount = productRepository.count();

        return Uni.combine().all().unis(products, totalCount).asTuple()
                .onItem().transform(tuple -> {
                    List<Product> productslist = tuple.getItem1();
                    Long total_count = tuple.getItem2();
                    int totalPages = (int) Math.ceil((double) total_count / size);

                    return productslist.stream()
                            .map(this::MapToResponse)
                            .collect(Collectors.toList());
                });
    }
}
