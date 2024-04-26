package com.ivayloalichkov.orderservice.controller;

import com.ivayloalichkov.orderservice.model.dto.OrderRequestDTO;
import com.ivayloalichkov.orderservice.service.OrderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

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
    @TimeLimiter(name = "inventory")
    @Retry(name = "inventory")
    public CompletableFuture<String> placeOrder(@RequestBody OrderRequestDTO orderRequestDTO) {
        return CompletableFuture.supplyAsync(() -> this.orderService.placeOrder(orderRequestDTO));
    }

    public CompletableFuture<String> fallbackMethod(OrderRequestDTO orderRequestDTO, RuntimeException exception) {
        return CompletableFuture.supplyAsync(() -> "Something went wrong, please order letter!");
    }
}
