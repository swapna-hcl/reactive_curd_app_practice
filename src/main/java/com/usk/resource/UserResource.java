package com.usk.resource;

import com.usk.dto.LoginRequest;
import com.usk.dto.RegisterRequest;
import com.usk.service.UserService;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {
    @Inject
    UserService userService;

    @POST
    @Path("/register")
    @WithTransaction
    public Uni<Response> registerUser(RegisterRequest request){
        return userService.registerUser(request);
    }


    @POST
    @Path("/login")
    @WithSession
    public Uni<Response> loginUser(LoginRequest request){
        return userService.login(request);
    }

}
