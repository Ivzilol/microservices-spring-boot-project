package com.ivailoalichkov.productservice.controller;


import com.ivailoalichkov.productservice.model.dto.ProductRequestDTO;
import com.ivailoalichkov.productservice.model.dto.ProductResponseDTO;
import com.ivailoalichkov.productservice.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {
    private final ProductService productService;
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createProduct(@RequestBody ProductRequestDTO productRequestDTO) {
        this.productService.createProduct(productRequestDTO);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponseDTO> productResponseDTOS() {
        return productService.getAllProducts();
    }
}
