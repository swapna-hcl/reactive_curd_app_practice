package com.usk.dto;

import io.smallrye.mutiny.Uni;

/**
 * Simple DTO for product information in search results
 * Contains only the basic product details we want to show
 */
public class SearchProductResponseDto {

    public Long id;          // Product ID
    public String name;      // Product name
    public Double price;
    public Uni<Object> totalPages; // Total number of pages
    public Uni<Object> totalCount; // Total number of products

    public Uni<Object> getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Uni<Object> totalPages) {
        this.totalPages = totalPages;
    }

    public Uni<Object> getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Uni<Object> totalCount) {
        this.totalCount = totalCount;
    }

    // Default constructor
    public SearchProductResponseDto() {
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
