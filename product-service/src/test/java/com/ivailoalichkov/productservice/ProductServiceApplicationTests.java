package com.ivailoalichkov.productservice;

import com.ivailoalichkov.productservice.model.dto.ProductRequestDTO;
import com.ivailoalichkov.productservice.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class ProductServiceApplicationTests {

	@Container
	static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:7.0.8");

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ProductRepository productRepository;

	@DynamicPropertySource
	static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry){
		dynamicPropertyRegistry.add("spring.data.mongodb.uri",
				mongoDBContainer::getReplicaSetUrl);
	}

	@Test
	public void testCreateProduct() throws Exception {
		ProductRequestDTO productRequestDTO = getProductRequest();
		ObjectMapper objectMapper = new ObjectMapper();
		String productRequest = objectMapper.writeValueAsString(productRequestDTO);
		mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
				.contentType(MediaType.APPLICATION_JSON)
				.content(productRequest))
				.andExpect(status().isCreated());
		Assertions.assertEquals(1, this.productRepository.findAll().size());
	}

	private ProductRequestDTO getProductRequest() {
		return ProductRequestDTO.builder()
				.name("Баница")
				.description("Най вкусната баница")
				.price(BigDecimal.valueOf(12.22))
				.build();
	}


}
