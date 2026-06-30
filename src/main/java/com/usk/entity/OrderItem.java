package com.usk.entity;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import jakarta.persistence.*;

/**
 * OrderItem Entity - represents one line item in an order
 * 
 * Example: If an order has 2 laptops and 3 mice, 
 * there will be 2 OrderItem records:
 * - OrderItem 1: productId=laptop, quantity=2
 * - OrderItem 2: productId=mouse, quantity=3
 */
@Entity
@Table(name = "order_items")
public class OrderItem extends PanacheEntity {

    /**
     * The order this item belongs to
     * @ManyToOne: Many order items belong to one order
     * @JoinColumn: Creates a foreign key column "order_id"
     */
    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    public Order order;

    // Which product was ordered
    @Column(name = "product_id", nullable = false)
    public Long productId;

    // How many units were ordered
    @Column(nullable = false)
    public Integer quantity;

    // Price per unit at the time of ordering
    @Column(name = "unit_price", nullable = false)
    public Double unitPrice;

    // Total price for this line item (unitPrice × quantity)
    @Column(name = "item_total_price", nullable = false)
    public Double itemTotalPrice;
}

