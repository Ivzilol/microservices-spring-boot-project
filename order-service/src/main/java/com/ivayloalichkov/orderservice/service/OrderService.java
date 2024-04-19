package com.ivayloalichkov.orderservice.service;

import com.ivayloalichkov.orderservice.event.OrderPlacedEvent;
import com.ivayloalichkov.orderservice.model.dto.InventoryResponseDTO;
import com.ivayloalichkov.orderservice.model.dto.OrderLineItemsDTO;
import com.ivayloalichkov.orderservice.model.dto.OrderRequestDTO;
import com.ivayloalichkov.orderservice.model.entity.Order;
import com.ivayloalichkov.orderservice.model.entity.OrderLineItems;
import com.ivayloalichkov.orderservice.repository.OrderRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class OrderService {
    private final OrderRepository orderRepository;

    private static final String INVENTORY_URL = "http://inventory-service/api/inventory";

    private final WebClient.Builder webClientBuilder;

    private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

    public OrderService(OrderRepository orderRepository, WebClient.Builder webClientBuilder, KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate) {
        this.orderRepository = orderRepository;
        this.webClientBuilder = webClientBuilder;
        this.kafkaTemplate = kafkaTemplate;
    }

    public String placeOrder(OrderRequestDTO orderRequestDTO) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        List<OrderLineItems> orderLineItems = orderRequestDTO.getOrderLineItemsDTOList()
                .stream()
                .map(this::mapToDto).toList();
        order.setOrderLineItemsList(orderLineItems);
        boolean allProductsInStock = isAllProductsInStock(order);
        if (Boolean.TRUE.equals(allProductsInStock)) {
            this.orderRepository.save(order);
            OrderPlacedEvent orderPlacedEvent = new OrderPlacedEvent();
            orderPlacedEvent.setOrderNumber(order.getOrderNumber());
            kafkaTemplate.send("notificationTopic", orderPlacedEvent);
            return "Order Placed Successfully";
        } else {
            throw new IllegalArgumentException("Product in not available!");
        }
    }

    private boolean isAllProductsInStock(Order order) {
        List<String> skuCodes = order.getOrderLineItemsList()
                .stream()
                .map(OrderLineItems::getSkuCode)
                .toList();
        InventoryResponseDTO[] inventoryResponsesArray = webClientBuilder.build().get()
                .uri(INVENTORY_URL, uriBuilder ->
                        uriBuilder.queryParam("skuCode", skuCodes).build())
                .retrieve()
                .bodyToMono(InventoryResponseDTO[].class)
                .block();
        return Arrays.stream(inventoryResponsesArray)
                .allMatch(InventoryResponseDTO::getIsInStock);
    }


    private OrderLineItems mapToDto(OrderLineItemsDTO orderLineItemsDTO) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(orderLineItemsDTO.getPrice());
        orderLineItems.setQuantity(orderLineItemsDTO.getQuantity());
        orderLineItems.setSkuCode(orderLineItemsDTO.getSkuCode());
        return orderLineItems;
    }
}
