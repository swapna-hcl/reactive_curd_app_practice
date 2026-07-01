package com.usk.resource;

import com.usk.dto.CreateOrderRequestDto;
import com.usk.service.OrderService;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;


@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrderResource {

    private static final Logger LOG = Logger.getLogger(OrderResource.class);

    // Inject the service that contains our business logic
    @Inject
    OrderService orderService;

    @GET
    @Path("/users/{userId}/orders/latest")
    public Uni<Response> getLatestOrdersByUser(@PathParam("userId") Long userId) {
        LOG.info("Fetching latest 5 orders for user: " + userId);

        return orderService.getLatestOrdersByUserId(userId)
                .onItem().transform(orders -> Response.ok(orders).build())
                .onFailure().recoverWithItem(error -> {
                    LOG.error("Failed to fetch latest orders: " + error.getMessage(), error);

                    ErrorResponse errorResponse = new ErrorResponse();
                    errorResponse.error = error.getMessage();
                    errorResponse.timestamp = System.currentTimeMillis();

                    return Response
                            .status(Response.Status.BAD_REQUEST)
                            .entity(errorResponse)
                            .build();
                });
    }

    /**
     * Create a new order
     */
    @POST
    @Path("/orders")
    public Uni<Response> createOrder(@Valid CreateOrderRequestDto request) {
        LOG.info("Received order creation request for user: " + request.userId);

        return orderService.createOrder(request)
                .onItem().transform(orderResponse -> {
                    // If successful, return HTTP 201 (CREATED) with the order details
                    LOG.info("Order created successfully: " + orderResponse.orderId);
                    return Response
                        .status(Response.Status.CREATED)  // HTTP 201
                        .entity(orderResponse)  // Send the order response as JSON
                        .build();
                })
                .onFailure().recoverWithItem(error -> {
                    // Log the error
                    LOG.error("Failed to create order: " + error.getMessage(), error);

                    // Create error response with details
                    ErrorResponse errorResponse = new ErrorResponse();
                    errorResponse.error = error.getMessage();
                    errorResponse.timestamp = System.currentTimeMillis();

                    // Return HTTP 400 (BAD REQUEST) with error details
                    return Response
                        .status(Response.Status.BAD_REQUEST)  // HTTP 400
                        .entity(errorResponse)  // Include error message!
                        .build();
                });
        }

    /**
     * Simple error response class
     */
    public static class ErrorResponse {
        public String error;
        public long timestamp;
    }


}



