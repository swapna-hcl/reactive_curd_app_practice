package com.usk.dto;

public class AuthResponse {
    public String token;
    public String username;
    public String roles;

    public AuthResponse(String token, String username, String roles) {
        this.token = token;
        this.username = username;
        this.roles = roles;
    }
}