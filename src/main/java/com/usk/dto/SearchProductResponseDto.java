package com.usk.dto;

import java.util.List;

/**
 * Simple DTO for product information in search results
 * Contains only the basic product details we want to show
 */
public class SearchProductResponseDto {


    public List<ProductResponseDto> products;
    public int totalPages;

    public List<ProductResponseDto> getProducts() {
        return products;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }


    public void setProducts(List<ProductResponseDto> products) {
        this.products = products;
    }


    public long totalCount;

}
