package com.usk.repository;

import com.usk.entity.Order;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

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

    public Uni<List<Order>> findLatestOrdersByUserId(Long userId, int limit) {
        return find("user.id = ?1 order by orderDate desc", userId)
                .page(Page.of(0, limit))
                .list();
    }
}


