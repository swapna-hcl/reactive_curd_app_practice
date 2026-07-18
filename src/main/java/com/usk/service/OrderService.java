package com.usk.service;

import com.usk.RestClient.TransactionClient;
import com.usk.dto.*;
import com.usk.entity.Order;
import com.usk.entity.OrderItem;
import com.usk.entity.Product;
import com.usk.entity.User;
import com.usk.event.OrderEventProducer;
import com.usk.exception.ProductNotFoundException;
import com.usk.exception.TransactionFailedException;
import com.usk.exception.UserNotFoundException;
import com.usk.repository.OrderRepository;
import com.usk.repository.ProductRepository;
import com.usk.repository.UserRepository;
import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.quarkus.hibernate.reactive.panache.Panache;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    @RestClient
    TransactionClient transactionClient;

    @Inject
    OrderEventProducer producer;


    @ConfigProperty(name = "ecom.account.number")
    String toAccountNumber;


    public Uni<OrderResponseDto> createOrder(CreateOrderRequestDto request) {
        return Panache.withTransaction(() ->
            // STEP 1: Check if the user exists and then process order with the User entity
            checkUserExists(request.userId)
                .chain(user -> processOrder(request, user))
        );
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
    private Uni<OrderResponseDto> processOrder(CreateOrderRequestDto request, User user) {
        // First, collect all product IDs from the request
        List<Long> productIds = new ArrayList<>();
        for (OrderProductDto item : request.products) {
            productIds.add(item.productId);
        }

        // STEP 3: Fetch all products from database and create order
        return loadProducts(productIds)
            .chain(products -> createAndSaveOrder(request, products, user));
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
    private Uni<OrderResponseDto> createAndSaveOrder(CreateOrderRequestDto request, List<Product> products, User user) {
        // Create a new order object
        Order order = new Order();
        order.user = user;
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

        // Create transaction request
        TransactionRequest transactionRequest = new TransactionRequest();
        transactionRequest.setFromAccountNumber(request.accountNumber);
        transactionRequest.setToAccountNumber(toAccountNumber);
        transactionRequest.setAmount(totalPrice);

        // Chain the transaction call with order persistence
        return transactionClient.createTransaction(transactionRequest)
            .onItem().invoke(transactionResponse -> {
                // Transaction successful, set the transaction status
                order.transactionStatus = transactionResponse.getTransactionStatus();
            })
            .chain(transactionResponse -> {
                // Only save the order if transaction was successful
                return orderRepository.persistAndFlush(order).onItem().transformToUni(savedOrder -> {)
                    if (savedOrder == null) {
                        throw new TransactionFailedException("Failed to save order after successful transaction");
                    }
                    OrderEvent event = new OrderEvent();
                    event.orderId = savedOrder.id;
                    event.userId = savedOrder.user.id;
                    event.totalAmount = savedOrder.totalPrice;
                    event.orderDate = savedOrder.orderDate;
                    event.expectedDeliveryDate = savedOrder.orderDate.plusDays(7); // Example: 7 days delivery
                    event.paymentStatus = transactionResponse.transactionStatus;
                    event.orderStatus = "CREATED";
                    return producer.publishOrderEvent(event).onItem().transformToUni(v -> Uni.createFrom().item(savedOrder));
                });
            })
            .chain(savedOrder -> {
                // Reload the order to get fresh data with populated IDs
                return orderRepository.findById(savedOrder.id);
            })
            .map(reloadedOrder -> buildResponse(reloadedOrder, products))
            .onFailure().transform(failure -> {
                // If transaction fails, wrap it in our custom exception
                // This ensures the order is NOT created if transaction fails
                return new TransactionFailedException("Transaction Failed: " + failure.getMessage(), failure);
            });
    }

    /**
     * STEP 5: Build the response object to send back to the client
     */
    private OrderResponseDto buildResponse(Order order, List<Product> products) {
        // Create response DTO
        OrderResponseDto response = new OrderResponseDto();
        response.orderId = order.id;
        response.userId = order.user != null ? order.user.id : null;
        response.totalPrice = order.totalPrice;
        response.orderDate = order.orderDate;
        response.orderItems = new ArrayList<>();
        response.transactionStatus = order.transactionStatus != null ? order.transactionStatus.toString() : null;
        // Add order items to response (only orderItemId and productId)
        for (OrderItem item : order.orderItems) {
            OrderItemDto itemDto = new OrderItemDto(item.id, item.productId);
            response.orderItems.add(itemDto);
        }

        return response;
    }

     /**
      * Fetch all orders and map to OrderResponseDto
      * Returns a reactive stream of all orders in the database
      * Each Order entity is transformed to OrderResponseDto format
      */
     @WithSession
     public Uni<List<OrderResponseDto>> getAllOrders() {
         return orderRepository.listAll()
                 .map(orders -> orders.stream()
                         .map(order -> buildResponse(order, new ArrayList<>()))
                         .collect(Collectors.toList()));
     }

    @WithSession
    public Uni<List<OrderResponseDto>> getLatestOrdersByUserId(Long userId) {
        return checkUserExists(userId)
                .chain(user -> orderRepository.findLatestOrdersByUserId(userId, 5))
                .map(orders -> orders.stream()
                        .map(order -> buildResponse(order, new ArrayList<>()))
                        .collect(Collectors.toList()));
    }


}


