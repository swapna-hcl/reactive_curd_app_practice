package com.usk.resource;

import com.usk.dto.CreateOrderRequestDto;
import com.usk.dto.OrderResponseDto;
import com.usk.service.OrderService;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.logging.Logger;


@Path("/orders")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrderResource {

    private static final Logger LOG = Logger.getLogger(OrderResource.class);

    // Inject the service that contains our business logic
    @Inject
    OrderService orderService;

    /**
     * Create a new order
     */
    @POST
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



