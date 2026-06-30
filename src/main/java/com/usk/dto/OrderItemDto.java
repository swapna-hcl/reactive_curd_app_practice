package com.usk.dto;


public class OrderItemDto {

    // ID of this order item record
    public Long orderItemId;

    // ID of the product
    public Long productId;

//    // Name of the product (for easy reference)
//    public String productName;
//
//    // How many units were ordered
//    public Integer quantity;
//
//    // Price per unit at time of order
//    public Double unitPrice;
//
//    // Total for this item (unitPrice × quantity)
//    public Double itemTotalPrice;

    // Default constructor
    public OrderItemDto() {
    }

    // Constructor with all fields
    public OrderItemDto(Long orderItemId, Long productId) {
        this.orderItemId = orderItemId;
        this.productId = productId;

    }

}


