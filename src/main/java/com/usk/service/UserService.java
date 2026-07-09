package com.usk.service;

import com.usk.dto.AuthResponse;
import com.usk.dto.LoginRequest;
import com.usk.dto.RegisterRequest;
import com.usk.entity.Role;
import com.usk.entity.User;
import com.usk.exception.BadRequestException;
import com.usk.exception.UserAlreadyExistsException;
import com.usk.repository.UserRepository;
import com.usk.security.PasswordUtil;
import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.smallrye.jwt.build.Jwt;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotAuthorizedException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

import java.time.Duration;
import java.time.Instant;
import java.util.Locale;

@ApplicationScoped
public class UserService {
    @Inject
    UserRepository userRepository;


    public Uni<Response> registerUser(RegisterRequest request) {
        if (request.name == null || request.name.isBlank() || request.email == null || request.email.isBlank() || request.password == null || request.password.isBlank()) {
            throw new BadRequestException("Bad request");
        }
        
        return userRepository.find("email", request.getEmail()).firstResult()
                .onItem().ifNotNull().failWith(() -> new UserAlreadyExistsException("User already exists"))
                .onItem().transformToUni(v -> {
                    // If null, proceed with registration
                    User user = new User();
                    user.name = request.getName();
                    user.email = request.getEmail();
                    user.password = PasswordUtil.hash(request.password);
                    user.role = Role.USER;
                    return userRepository.persist(user)
                            .map(v2 -> Response.status(Response.Status.CREATED)
                                    .entity(new AuthResponse("token", user.name, user.role.name()))
                                    .build());
                });
    }

    public Uni<Response> login(LoginRequest req) {
        if (req == null || req.email == null || req.password == null) {
            throw new BadRequestException("email and password are required");
        }
        
        return userRepository.findByEmail(req.email)
                .onItem().ifNull().failWith(() -> new NotAuthorizedException("Invalid credentials"))
                .onItem().transform(u -> {
                    // Validate password
                    if (u.password == null || !PasswordUtil.verify(req.password, u.password)) {
                        throw new NotAuthorizedException("Invalid credentials");
                    }
                    
                    // Validate user data
                    if (u.name == null || u.name.isBlank()) {
                        throw new BadRequestException("User name is not valid");
                    }
                    if (u.role == null) {
                        throw new BadRequestException("User role is not set");
                    }
                    
                    // Generate token and return response
                    String token = generateToken(u);
                    return Response.ok(new AuthResponse(token, u.name, u.role.name())).build();
                });
    }

    private String generateToken(User u){
        Instant now = Instant.now();
        String token  = Jwt.issuer("EcommerceAPIs").upn(u.name).subject("JWT token").groups(u.role.name())
                .issuedAt(now).expiresAt(now.plus(Duration.ofHours(1))).sign();
        return  token;
    }



}
