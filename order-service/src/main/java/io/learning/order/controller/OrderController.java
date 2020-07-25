package io.learning.order.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.learning.order.domain.Order;
import io.learning.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 
 * Exposes REST API Interface for interacting with OrderService.
 *
 * @author Anil Jaglan
 * @version 1.0
 */
@RestController
@Tag(name = "Orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/orders")
    @Operation(summary = "Create a new order")
    public Order createOrder(@RequestBody Order order) {
        return orderService.createOrder(order);
    }

    @GetMapping("/orders/{id}")
    @Operation(summary = "Get an order")
    public ResponseEntity<Order> getOrder(@PathVariable("id") Long orderId) {
        return orderService.getOrderById(orderId).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }

}
