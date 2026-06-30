package com.usk.entity;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import jakarta.persistence.*;

/**
 * User Entity - represents a user in the system
 * 
 * PanacheEntity automatically gives us:
 * - An 'id' field (primary key)
 * - Basic CRUD methods like persist(), delete(), findById()
 */
@Entity
@Table(name = "users")
public class User extends PanacheEntity {

    // User's full name
    @Column(nullable = false)
    public String name;

    // User's email address (must be unique)
    @Column(nullable = false, unique = true)
    public String email;
}


