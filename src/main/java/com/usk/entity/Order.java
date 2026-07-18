package com.usk.entity;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import com.usk.entity.User;

/**
 * Order Entity - represents a customer order
 * 
 * An order contains:
 * - Who placed it (userId)
 * - When it was placed (orderDate)
 * - Total cost (totalPrice)
 * - List of items ordered (orderItems)
 */
@Entity
@Table(name = "orders")
public class Order extends PanacheEntity {

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    public User user;

    // Total price of the entire order
    @Column(name = "total_price", nullable = false)
    public Double totalPrice;

    // When the order was created
    @Column(name = "order_date", nullable = false)
    public LocalDate orderDate;

    /**
     * List of items in this order
     *
     * @OneToMany: One order has many order items
     * mappedBy = "order": The OrderItem entity has a field called "order" that links back
     * cascade = ALL: When we save/delete an order, save/delete its items too
     * fetch = EAGER: Load order items immediately when we load an order
     */
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    public List<OrderItem> orderItems = new ArrayList<>();
    public Object transactionStatus;

    /**
     * This method runs automatically before saving to database
     * It sets the order date if not already set
     */
    @PrePersist
    public void prePersist() {
        if (orderDate == null) {
            orderDate = LocalDate.from(LocalDateTime.now());
        }
    }
}


