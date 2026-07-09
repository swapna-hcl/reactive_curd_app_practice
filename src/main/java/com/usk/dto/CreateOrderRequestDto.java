package com.usk.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.List;

/**
 * DTO (Data Transfer Object) for creating an order
 * This is what the client sends to our API
 * 
 * Example JSON:
 * {
 *   "userId": 1,
 *   "products": [
 *     {"productId": 101, "quantity": 2},
 *     {"productId": 102, "quantity": 3}
 *   ]
 * }
 */
public class CreateOrderRequestDto {

    // ID of the user placing the order
    @NotNull(message = "User ID is required")
    @Positive(message = "User ID must be positive")
    public Long userId;

    // List of products to order with their quantities
    @NotNull(message = "Products list is required")
    @NotEmpty(message = "Products list cannot be empty")
    @Valid  // Validates each product in the list
    public List<OrderProductDto> products;
    public String accountNumber;

    // Default constructor (required for JSON deserialization)
    public CreateOrderRequestDto() {
    }

    // Constructor with all fields
    public CreateOrderRequestDto(Long userId, List<OrderProductDto> products,String accountNumber) {
        this.userId = userId;
        this.products = products;
        this.accountNumber= accountNumber;
    }
}


