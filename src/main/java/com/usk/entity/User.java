package com.usk.entity;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User extends PanacheEntity {

    // id is inherited from PanacheEntity; do not redeclare it here

    // User's full name
    @Column(nullable = false)
    public String name;

    // User's email address (must be unique)
    @Column(nullable = false, unique = true)
    public String email;

    @Column(nullable = false)
    public String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    public Role role = Role.USER;

}
