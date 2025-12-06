package com.github.darekdan.natsorders;

import io.nats.client.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicBoolean;

@Service
@Slf4j
public class OrderMessagingService {

    private final AtomicBoolean subscribed = new AtomicBoolean(false);
    private final SerializationService serializationService;
    private volatile Connection natsConnection;
    @Value("${nats.server}")
    private String natsServer = "nats://localhost:4222";
    @Value("${nats.connection-timeout}")
    private int connectionTimeout = 10;
    @Value("${nats.reconnect-wait}")
    private int reconnectWait = 5;

    public OrderMessagingService(SerializationService serializationService) throws InterruptedException {
        this.serializationService = serializationService;
        Options options = Options
                .builder()
                .server(natsServer)
                .connectionTimeout(java.time.Duration.ofSeconds(connectionTimeout))
                .reconnectWait(java.time.Duration.ofSeconds(reconnectWait))
                .enableFastFallback()
                .connectionListener((c, e) -> {
                    natsConnection = c;
                    log.info("Connection event: {}", e);
                    if (e == ConnectionListener.Events.CONNECTED && subscribed.compareAndSet(false, true)) {
                        receiveOrder();
                    }
                })
                .build();
        Nats.connectAsynchronously(options, true);
    }

    public void sendOrder(Order order) {
        if (natsConnection == null || !natsConnection
                .getStatus()
                .equals(Connection.Status.CONNECTED)) {
            throw new IllegalStateException("Nats connection is not ready");
        }
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

    public boolean isConnected() {
        return natsConnection != null && natsConnection
                .getStatus()
                .equals(Connection.Status.CONNECTED);
    }
}
