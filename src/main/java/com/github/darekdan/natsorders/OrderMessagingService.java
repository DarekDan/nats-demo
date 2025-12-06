package com.github.darekdan.natsorders;

import io.nats.client.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrderMessagingService {

    private Connection natsConnection;

    private final SerializationService serializationService;

    public OrderMessagingService(SerializationService serializationService) throws InterruptedException {
        this.serializationService = serializationService;
        Options options = Options
                .builder()
                .server("nats://localhost:4222")
                .connectionTimeout(java.time.Duration.ofSeconds(10))
                .reconnectWait(java.time.Duration.ofSeconds(5))
                .enableFastFallback()
                .connectionListener((c, e) -> {
                    natsConnection = c;
                    log.info("Connection event: {}", e);
                    receiveOrder();
                })
                .build();
        Nats.connectAsynchronously(options, true);
    }

    public void sendOrder(Order order) {
        log.info("Sending order: {}", order);
        natsConnection.publish("orders", serializationService.serialize(order));
    }

    private void receiveOrder() {
        Dispatcher dispatcher = natsConnection.createDispatcher(message -> {
            Order order = serializationService.deserialize(message.getData(), Order.class);
            log.info("Received order: {}", order);
        });
        dispatcher.subscribe("orders");
    }
}
