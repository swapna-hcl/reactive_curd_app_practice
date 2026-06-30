package com.usk.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * DTO for a product in an order request
 * Represents one product with its quantity
 * 
 * Example JSON:
 * {
 *   "productId": 101,
 *   "quantity": 2
 * }
 */
public class OrderProductDto {

    // Which product to order
    @NotNull(message = "Product ID is required")
    @Positive(message = "Product ID must be positive")
    public Long productId;

    // How many units to order
    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be positive")
    public Integer quantity;

    // Default constructor
    public OrderProductDto() {
    }

    // Constructor with all fields
    public OrderProductDto(Long productId, Integer quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }
}


