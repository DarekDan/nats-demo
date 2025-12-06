package com.github.darekdan.natsorders;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@Slf4j
public class OrderController {

    private final OrderMessagingService orderMessagingService;

    public OrderController(OrderMessagingService orderMessagingService) {
        this.orderMessagingService = orderMessagingService;
    }

    @PostMapping("/simple")
    public ResponseEntity<String> sendOrder(@RequestBody Order order){
        orderMessagingService.sendOrder(order);
        return ResponseEntity.ok("Order sent successfully: " + order.orderId());
    }
}
