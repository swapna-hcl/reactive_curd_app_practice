package com.usk.exception;

/**
 * Exception thrown when a user is not found in the database
 * This is a custom exception that extends RuntimeException
 */
public class UserNotFoundException extends RuntimeException {
    
    // Constructor that takes user ID and creates a meaningful error message
    public UserNotFoundException(String userId) {
        super("User not found with ID: " + userId);
    }
}


