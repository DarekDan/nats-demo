package com.github.darekdan.natsorders;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record Order(String orderId, String customerId, BigDecimal amount, OrderStatus status,
                    LocalDateTime orderDateTime) {
    public enum OrderStatus {PENDING, PROCESSING, DELIVERED, DELIVERY_FAILED, CANCELED, COMPLETED}
}
