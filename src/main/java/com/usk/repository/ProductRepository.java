package com.usk.repository;

import com.usk.entity.Product;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

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
    public Uni<List<Product>> searchProducts(String searchText, int pageNumber, int pageSize) {
        return find("LOWER(name) LIKE LOWER(?1)", "%" + searchText + "%")
                .page(Page.of(pageNumber - 1, pageSize))
                .list();
    }

    public Uni<Long> countProducts(String searchText) {
        return count("LOWER(name) LIKE ?1", "%" + searchText.toLowerCase() + "%");
    }
}
