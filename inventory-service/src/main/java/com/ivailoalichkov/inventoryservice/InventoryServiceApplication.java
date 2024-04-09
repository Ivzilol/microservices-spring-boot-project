package com.ivailoalichkov.inventoryservice;

import com.ivailoalichkov.inventoryservice.model.Inventory;
import com.ivailoalichkov.inventoryservice.repository.InventoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class InventoryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(InventoryServiceApplication.class, args);

    }

    @Bean
    public CommandLineRunner loadData(InventoryRepository inventoryRepository) {
        return args -> {
            if (inventoryRepository.count() == 0) {

                Inventory inventory = new Inventory();
                inventory.setSkuCode("pie_1");
                inventory.setQuantity(100);

                Inventory inventory2 = new Inventory();
                inventory2.setSkuCode("sweet_1");
                inventory2.setQuantity(0);

                inventoryRepository.save(inventory);
                inventoryRepository.save(inventory2);
            }
        };
    }
}
