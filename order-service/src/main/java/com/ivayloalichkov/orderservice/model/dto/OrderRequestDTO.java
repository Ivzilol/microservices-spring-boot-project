package com.ivayloalichkov.orderservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequestDTO {

    private List<OrderLineItemsDTO> orderLineItemsDTOList;
}
