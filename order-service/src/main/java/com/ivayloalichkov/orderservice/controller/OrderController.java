package com.ivayloalichkov.orderservice.controller;

import com.ivayloalichkov.orderservice.model.dto.OrderRequestDTO;
import com.ivayloalichkov.orderservice.service.OrderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CircuitBreaker(name = "inventory", fallbackMethod = "fallbackMethod")
    public String placeOrder(@RequestBody OrderRequestDTO orderRequestDTO){
        this.orderService.placeOrder(orderRequestDTO);
        return "Order Placed Successfully";
    }

    public String fallbackMethod(OrderRequestDTO orderRequestDTO, RuntimeException exception) {
        return "Something went wrong, please order letter!";
    }
}
