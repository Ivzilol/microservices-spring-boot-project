package com.ivailoalichkov.inventoryservice.config;

import com.ivailoalichkov.inventoryservice.model.Inventory;
import com.ivailoalichkov.inventoryservice.repository.InventoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class LoadData implements CommandLineRunner{

    private final InventoryRepository inventoryRepository;

    public LoadData(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    @Override
    public void run(String... args) {
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
    }
}
