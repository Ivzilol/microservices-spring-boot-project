package com.ivailoalichkov.inventoryservice.controller;

import com.ivailoalichkov.inventoryservice.model.dto.InventoryResponseDTO;
import com.ivailoalichkov.inventoryservice.service.InventoryService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService inventoryService;
    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<InventoryResponseDTO> isInStock(@RequestParam List<String> skuCode) {
        return this.inventoryService.isInStock(skuCode);
    }
}
