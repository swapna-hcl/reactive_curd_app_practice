package com.usk.RestClient;

import com.usk.dto.TransactionRequest;
import com.usk.dto.TransactionResponse;
import io.smallrye.mutiny.Uni;

import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/transaction")
@RegisterRestClient(baseUri = "http://localhost:8081/")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface TransactionClient {

    @POST
    Uni<TransactionResponse> createTransaction(TransactionRequest request);
}
