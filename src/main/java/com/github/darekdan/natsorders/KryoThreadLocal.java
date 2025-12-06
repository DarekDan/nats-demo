package com.github.darekdan.natsorders;

import com.esotericsoftware.kryo.Kryo;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
public class KryoThreadLocal {

    private static final ThreadLocal<Kryo> kryoInstanceThreadLocal = ThreadLocal.withInitial(() -> {
        Kryo kryo = new Kryo();
        kryo.register(Order.class);
        kryo.register(BigDecimal.class);
        kryo.register(LocalDateTime.class);
        kryo.register(Order.OrderStatus.class);
        return kryo;
    });

    public Kryo get() {
        return kryoInstanceThreadLocal.get();
    }

    public void cleanup() {
        kryoInstanceThreadLocal.remove();
    }
}
