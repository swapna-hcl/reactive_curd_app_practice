package com.usk.repository;

import com.usk.entity.OrderItem;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Repository for OrderItem entity
 * Handles database operations for order items
 * 
 * Usually order items are saved automatically with orders (cascade),
 * but this repository can be used for specific queries.
 */
@ApplicationScoped
public class OrderItemRepository implements PanacheRepository<OrderItem> {
    // All basic CRUD methods are inherited
}


