package com.usk.repository;

import com.usk.entity.User;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Repository for User entity
 * Handles database operations for users
 * 
 * PanacheRepository gives us methods like:
 * - findById(id) - Find a user by ID
 * - list() - Get all users
 * - persist(user) - Save a user
 * - delete(user) - Delete a user
 */
@ApplicationScoped
public class UserRepository implements PanacheRepository<User> {
    // All methods are inherited from PanacheRepository
    // You can add custom query methods here if needed
}


