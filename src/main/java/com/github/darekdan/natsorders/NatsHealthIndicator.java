package com.github.darekdan.natsorders;

import org.springframework.boot.health.contributor.Health;
import org.springframework.boot.health.contributor.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class NatsHealthIndicator implements HealthIndicator {
    private final OrderMessagingService service;

    public NatsHealthIndicator(OrderMessagingService service) {
        this.service = service;
    }

    @Override
    public Health health() {
        return service.isConnected()
                ? Health.up().build()
                : Health.down().withDetail("reason", "NATS disconnected").build();
    }
}
