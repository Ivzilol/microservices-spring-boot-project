package com.ivailoalichkov.productservice.service;

import com.ivailoalichkov.productservice.model.dto.ProductRequestDTO;
import com.ivailoalichkov.productservice.model.dto.ProductResponseDTO;
import com.ivailoalichkov.productservice.model.entity.Product;
import com.ivailoalichkov.productservice.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    public void createProduct(ProductRequestDTO productRequestDTO){
        Product product = Product.builder()
                .name(productRequestDTO.getName())
                .description(productRequestDTO.getDescription())
                .price(productRequestDTO.getPrice())
                .build();
        this.productRepository.save(product);
        log.info("Product {} is saved", product.getId());
    }
    public List<ProductResponseDTO> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream().map(this::mapToProductResponseDTO).toList();
    }
    private ProductResponseDTO mapToProductResponseDTO(Product product) {
        return ProductResponseDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .build();
    }

}
