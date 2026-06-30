package com.usk.repository;

import com.usk.entity.Product;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Repository for Product entity
 * Handles database operations for products
 * 
 * Inherited methods include:
 * - findById(id) - Find a product by ID
 * - list("query", params) - Find products with a query
 * - persist(product) - Save a product
 * - delete(product) - Delete a product
 */
@ApplicationScoped
public class ProductRepository implements PanacheRepository<Product> {
    // All methods are inherited from PanacheRepository
    // Custom methods can be added here
}
