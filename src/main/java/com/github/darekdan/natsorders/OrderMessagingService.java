package com.github.darekdan.natsorders;

import io.nats.client.*;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

@Service
@Slf4j
public class OrderMessagingService {

    private final SerializationService serializationService;

    // Thread-safe container for the NATS connection
    private final AtomicReference<Connection> connectionRef = new AtomicReference<>();

    // Ensures we only initialize the dispatcher once per application lifecycle
    private final AtomicBoolean dispatcherInitialized = new AtomicBoolean(false);

    private final String natsServer;
    private final Duration connectionTimeout;
    private final Duration reconnectWait;

    // Use Constructor Injection for configuration
    public OrderMessagingService(
            SerializationService serializationService,
            @Value("${nats.server:nats://localhost:4222}") String natsServer,
            @Value("${nats.connection-timeout:10}") int connectionTimeoutSeconds,
            @Value("${nats.reconnect-wait:5}") int reconnectWaitSeconds) {

        this.serializationService = serializationService;
        this.natsServer = natsServer;
        this.connectionTimeout = Duration.ofSeconds(connectionTimeoutSeconds);
        this.reconnectWait = Duration.ofSeconds(reconnectWaitSeconds);
    }

    /**
     * Initialize connection in PostConstruct to avoid blocking the Constructor.
     */
    @PostConstruct
    public void connect() throws InterruptedException {
        Options options = Options
                .builder()
                .server(natsServer)
                .connectionTimeout(connectionTimeout)
                .reconnectWait(reconnectWait)
                .maxReconnects(-1) // Unlimited reconnect attempts
                .connectionListener(this::handleConnectionEvents)
                .errorListener(new ErrorListener() { // Good practice to log async errors
                    @Override
                    public void errorOccurred(Connection conn, String error) {
                        log.error("NATS Error: {}", error);
                    }

                    @Override
                    public void exceptionOccurred(Connection conn, Exception exp) {
                        log.error("NATS Exception", exp);
                    }
                })
                .build();

        // Connect asynchronously so Spring startup isn't blocked if NATS is down
        Nats.connectAsynchronously(options, true);
    }

    /**
     * Cleanup resources when Spring shuts down.
     */
    @PreDestroy
    public void close() {
        Connection conn = connectionRef.get();
        if (conn != null) {
            try {
                log.info("Closing NATS connection...");
                conn.close();
            } catch (InterruptedException e) {
                Thread
                        .currentThread()
                        .interrupt();
                log.warn("Interrupted while closing NATS connection", e);
            }
        }
    }

    private void handleConnectionEvents(Connection conn, ConnectionListener.Events type) {
        // Atomically update the reference to the latest connection object
        connectionRef.set(conn);
        log.info("NATS Connection Event: {}", type);

        if (type == ConnectionListener.Events.CONNECTED) {
            // Only set up the subscription once.
            // NATS client automatically handles re-subscription on reconnects.
            if (dispatcherInitialized.compareAndSet(false, true)) {
                setupSubscription(conn);
            }
        }
    }

    public void sendOrder(Order order) {
        Connection conn = connectionRef.get();

        if (conn == null || conn.getStatus() != Connection.Status.CONNECTED) {
            throw new IllegalStateException("Cannot send order: NATS connection is unavailable.");
        }

        try {
            log.info("Sending order: {}", order);
            byte[] data = serializationService.serialize(order);
            conn.publish("orders", data);
        } catch (Exception e) {
            log.error("Failed to publish order", e);
            throw new RuntimeException("Failed to publish order", e);
        }
    }

    private void setupSubscription(Connection conn) {
        log.info("Setting up 'orders' subscription...");
        Dispatcher dispatcher = conn.createDispatcher(message -> {
            try {
                Order order = serializationService.deserialize(message.getData(), Order.class);
                log.info("Received order: {}", order);
            } catch (Exception e) {
                log.error("Failed to deserialize order", e);
            }
        });
        dispatcher.subscribe("orders");
    }

    public boolean isConnected() {
        Connection conn = connectionRef.get();
        return conn != null && conn.getStatus() == Connection.Status.CONNECTED;
    }
}
