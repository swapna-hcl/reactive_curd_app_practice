package com.usk.dto;

import java.time.LocalDate;
import java.util.List;


public class OrderResponseDto {

    // The ID of the created order
    public Long orderId;
    
    // ID of the user who placed the order
    public Long userId;
    
    // Total price of the entire order
    public Double totalPrice;
    
    // When the order was created
    public LocalDate orderDate;

    public String transactionStatus;
    
    // List of items in the order with details
    public List<OrderItemDto> orderItems;

    // Default constructor
    public OrderResponseDto() {
    }

    // Constructor with all fields
    public OrderResponseDto(Long orderId, Long userId, Double totalPrice, 
                           LocalDate orderDate, List<OrderItemDto> orderItems) {
        this.orderId = orderId;
        this.userId = userId;
        this.totalPrice = totalPrice;
        this.orderDate = orderDate;
        this.orderItems = orderItems;
    }
}


