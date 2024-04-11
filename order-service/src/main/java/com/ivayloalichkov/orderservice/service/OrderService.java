package com.ivayloalichkov.orderservice.service;

import com.ivayloalichkov.orderservice.model.dto.OrderLineItemsDTO;
import com.ivayloalichkov.orderservice.model.dto.OrderRequestDTO;
import com.ivayloalichkov.orderservice.model.entity.Order;
import com.ivayloalichkov.orderservice.model.entity.OrderLineItems;
import com.ivayloalichkov.orderservice.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;

    private final WebClient webClient;

    public OrderService(OrderRepository orderRepository, WebClient webClient) {
        this.orderRepository = orderRepository;
        this.webClient = webClient;
    }

    public void placeOrder(OrderRequestDTO orderRequestDTO) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        List<OrderLineItems> orderLineItems = orderRequestDTO.getOrderLineItemsDTOList()
                .stream()
                .map(this::mapToDto).toList();
        order.setOrderLineItemsList(orderLineItems);
        //check if the item is available
        if (checkIsProductAvailable()) {
            this.orderRepository.save(order);
        } else {
            throw new IllegalArgumentException("Product in not available!");
        }
    }

    private Boolean checkIsProductAvailable() {
        return webClient.get()
                .uri("http://localhost:8082/api/inventory")
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();
    }

    private OrderLineItems mapToDto(OrderLineItemsDTO orderLineItemsDTO) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(orderLineItemsDTO.getPrice());
        orderLineItems.setQuantity(orderLineItemsDTO.getQuantity());
        orderLineItems.setSkuCode(orderLineItemsDTO.getSkuCode());
        return orderLineItems;
    }
}
