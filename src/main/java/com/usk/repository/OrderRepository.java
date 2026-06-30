package com.usk.repository;

import com.usk.entity.Order;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Repository for Order entity
 * Handles database operations for orders
 * 
 * This repository can be used to:
 * - Save new orders
 * - Find orders by ID
 * - Query orders by user, date, etc.
 */
@ApplicationScoped
public class OrderRepository implements PanacheRepository<Order> {
    // All basic CRUD methods are inherited
}


