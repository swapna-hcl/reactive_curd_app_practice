package com.usk.exception;

import com.usk.dto.ExceptionResponsedto;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

/**
 * Global Exception Handler
 * This class catches exceptions thrown anywhere in our application
 * and converts them to proper HTTP responses
 * 
 * Instead of showing ugly stack traces to users, we return
 * clean JSON error messages with appropriate HTTP status codes
 */
@ApplicationScoped
public class GlobalExceptionMapper {

    /**
     * Handles ProductNotFoundException
     * Returns HTTP 404 (Not Found) when a product doesn't exist
     */
    @ServerExceptionMapper
    public RestResponse<ExceptionResponsedto> ProductNotFoundException(ProductNotFoundException e) {
        Log.error("Product not found: " + e.getMessage());
        
        // Create error response
        ExceptionResponsedto dto = new ExceptionResponsedto();
        dto.setStatusCode(404);
        dto.setMessage(e.getMessage());
        
        // Return HTTP 404 with error details
        return RestResponse.status(RestResponse.Status.NOT_FOUND, dto);
    }

    /**
     * Handles UserNotFoundException
     * Returns HTTP 404 (Not Found) when a user doesn't exist
     */
    @ServerExceptionMapper
    public RestResponse<ExceptionResponsedto> UserNotFoundException(UserNotFoundException e) {
        Log.error("User not found: " + e.getMessage());
        
        // Create error response
        ExceptionResponsedto dto = new ExceptionResponsedto();
        dto.setStatusCode(404);
        dto.setMessage(e.getMessage());
        
        // Return HTTP 404 with error details
        return RestResponse.status(RestResponse.Status.NOT_FOUND, dto);
    }

    @ServerExceptionMapper
    public RestResponse<ExceptionResponsedto> UserAlreadyExistsException(UserAlreadyExistsException e){
        Log.error("USer already Exists" + e.getMessage());
        // Create error response
        ExceptionResponsedto dto = new ExceptionResponsedto();
        dto.setStatusCode(404);
        dto.setMessage(e.getMessage());

        // Return HTTP 404 with error details
        return RestResponse.status(RestResponse.Status.FOUND, dto);
    }

    @ServerExceptionMapper
    public RestResponse<ExceptionResponsedto> BadRequestException(BadRequestException e){
        Log.error("Bad request" + e.getMessage());

        // Create error response
        ExceptionResponsedto dto = new ExceptionResponsedto();
        dto.setStatusCode(400);
        dto.setMessage(e.getMessage());

        // Return HTTP 404 with error details
        return RestResponse.status(RestResponse.Status.BAD_REQUEST, dto);
    }


    @ServerExceptionMapper
    public RestResponse<ExceptionResponsedto> TransactionFailedException(TransactionFailedException e){
        Log.error("Transaction failed: " + e.getMessage());

        // Create error response
        ExceptionResponsedto dto = new ExceptionResponsedto();
        dto.setStatusCode(400);
        dto.setMessage(e.getMessage());

        // Return HTTP 404 with error details
        return RestResponse.status(RestResponse.Status.BAD_REQUEST, dto);
    }

}
