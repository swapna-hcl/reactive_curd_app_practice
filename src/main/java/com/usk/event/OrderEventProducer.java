package com.usk.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.usk.dto.OrderEvent;
import com.usk.exception.BadRequestException;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

@ApplicationScoped
public class OrderEventProducer {

    @Inject
    @Channel("order-topic")
    Emitter<String> emitter;

    @Inject
    ObjectMapper objectMapper;


    public Uni<Void> publishOrderEvent(OrderEvent event){

        return Uni.createFrom().item(() -> {
            try{
                String json = objectMapper.writeValueAsString(event);
                emitter.send(json);
                System.out.println("Published event: " + json);
                return null;
            }catch (Exception e){
                throw new BadRequestException("Failed to publish order event");
            }
        });
    }


}
