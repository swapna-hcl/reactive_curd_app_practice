package com.usk.resource;

import com.usk.dto.CreateOrderRequestDto;
import com.usk.dto.OrderResponseDto;
import com.usk.service.OrderService;
import io.quarkus.security.Authenticated;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

import static jakarta.ws.rs.client.Entity.entity;


@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrderResource {


    // Inject the service that contains our business logic
    @Inject
    OrderService orderService;

    @GET
    @Path("/users/{userId}/orders/latest")
    public Uni<Response> getLatestOrdersByUser(@PathParam("userId") Long userId) {
        return orderService.getLatestOrdersByUserId(userId)
                .onItem().transform(orders -> Response.ok(orders).build())
                .onFailure().recoverWithItem(error -> {

                   throw new BadRequestException(error.getMessage());
                });
    }

    /**
     * Create a new order
     */
    @POST
    @Path("/orders")
    public Uni<Response> createOrder(@Valid CreateOrderRequestDto request) {

        return orderService.createOrder(request)
                .onItem().transform(orderResponse -> {
                    // If successful, return HTTP 201 (CREATED) with the order details
                    return Response
                        .status(Response.Status.CREATED)  // HTTP 201
                        .entity(orderResponse)  // Send the order response as JSON
                        .build();
                })
                .onFailure().recoverWithItem(error -> {
                    throw new BadRequestException(error.getMessage());
                });
        }



    /**
     * Get all orders
     * Returns a list of all orders in the system, mapped to OrderResponseDto format
     * Each order includes order details and its associated order items
     */
    @GET
    @Path("/orders")
    public Uni<Response> getAllOrders() {
        return orderService.getAllOrders()
                .onItem().transform(orders -> Response.ok(orders).build())
                .onFailure().recoverWithItem(error -> {
                    throw new BadRequestException(error.getMessage());
                });
    }

}



