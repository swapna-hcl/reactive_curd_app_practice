package com.usk.service;

import com.usk.dto.*;
import com.usk.entity.Order;
import com.usk.entity.OrderItem;
import com.usk.entity.Product;
import com.usk.entity.User;
import com.usk.exception.ProductNotFoundException;
import com.usk.exception.UserNotFoundException;
import com.usk.repository.OrderRepository;
import com.usk.repository.ProductRepository;
import com.usk.repository.UserRepository;
import io.quarkus.hibernate.reactive.panache.Panache;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Service class that handles the business logic for order creation.
 * This is a beginner-friendly implementation with detailed comments.
 *
 * Simplified flow: Validate user → Load products → Create order
 */
@ApplicationScoped
public class OrderService {

    // Inject repositories - Quarkus automatically provides these
    @Inject
    OrderRepository orderRepository;

    @Inject
    ProductRepository productRepository;

    @Inject
    UserRepository userRepository;


    public Uni<OrderResponseDto> createOrder(CreateOrderRequestDto request) {
        return Panache.withTransaction(() -> {

            // STEP 1: Check if the user exists
            return checkUserExists(request.userId)
                .chain(user -> processOrder(request));
        });
    }

    /**
     * STEP 1: Verify the user exists in the database
     */
    private Uni<User> checkUserExists(Long userId) {
        // findById returns Uni<User> - a promise of a User object
        return userRepository.findById(userId)
            .onItem().transform(user -> {
                if (user == null) {
                    throw new UserNotFoundException("User with ID " + userId + " not found");
                }
                return user;
            });
    }

    /**
     * STEP 2: Process the entire order
     */
    private Uni<OrderResponseDto> processOrder(CreateOrderRequestDto request) {
        // First, collect all product IDs from the request
        List<Long> productIds = new ArrayList<>();
        for (OrderProductDto item : request.products) {
            productIds.add(item.productId);
        }

        // STEP 3: Fetch all products from database and create order
        return loadProducts(productIds)
            .chain(products -> createAndSaveOrder(request, products));
    }

    /**
     * STEP 3: Load all products from database
     */
    private Uni<List<Product>> loadProducts(List<Long> productIds) {
        // Query: "SELECT * FROM products WHERE id IN (productIds)"
        return productRepository.list("id in ?1", productIds)
            .onItem().transform(products -> {
                // Check if we found all products
                if (products.size() != productIds.size()) {
                     throw new ProductNotFoundException("One or more products not found");
                }
                return products;
            });
    }



    /**
     * Helper method to find a product by ID from a list
     */
    private Product findProductById(List<Product> products, Long productId) {
        for (Product product : products) {
            if (product.id.equals(productId)) {
                return product;
            }
        }
        return null;
    }

    /**
     * STEP 4: Create the order entity and save it
     */
    private Uni<OrderResponseDto> createAndSaveOrder(CreateOrderRequestDto request, List<Product> products) {
        // Create a new order object
        Order order = new Order();
        order.userId = request.userId;
        order.orderDate = LocalDateTime.now();
        order.orderItems = new ArrayList<>();

        double totalPrice = 0.0;

        // Create order items and calculate total price
        for (OrderProductDto requestedItem : request.products) {
            Product product = findProductById(products, requestedItem.productId);

            // Create an order item
            OrderItem orderItem = new OrderItem();
            orderItem.order = order;  // Link to parent order
            orderItem.productId = product.id;
            orderItem.quantity = requestedItem.quantity;
            orderItem.unitPrice = product.price;
            orderItem.itemTotalPrice = product.price * requestedItem.quantity;

            // Add to order
            order.orderItems.add(orderItem);

            // Add to total
            totalPrice += orderItem.itemTotalPrice;
        }

        order.totalPrice = totalPrice;

        // Save the order (orderItems will be saved automatically due to cascade)
        // Use persistAndFlush to ensure IDs are generated immediately
        return orderRepository.persistAndFlush(order)
            .chain(savedOrder -> {
                // Reload the order to get fresh data with populated IDs
                return orderRepository.findById(savedOrder.id);
            })
            .map(reloadedOrder -> buildResponse(reloadedOrder, products));
    }

    /**
     * STEP 5: Build the response object to send back to the client
     */
    private OrderResponseDto buildResponse(Order order, List<Product> products) {
        // Create response DTO
        OrderResponseDto response = new OrderResponseDto();
        response.orderId = order.id;
        response.userId = order.userId;
        response.totalPrice = order.totalPrice;
        response.orderDate = order.orderDate;
        response.orderItems = new ArrayList<>();

        // Add order items to response (only orderItemId and productId)
        for (OrderItem item : order.orderItems) {
            OrderItemDto itemDto = new OrderItemDto(item.id, item.productId);
            response.orderItems.add(itemDto);
        }


        return response;
    }
}


