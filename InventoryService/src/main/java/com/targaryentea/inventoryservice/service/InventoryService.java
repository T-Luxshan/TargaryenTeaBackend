package com.targaryentea.inventoryservice.service;

import com.targaryentea.inventoryservice.entity.Inventory;
import com.targaryentea.inventoryservice.repository.InventoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    @Transactional(readOnly = true)
    public Boolean isInStock(String skuCode) {
        Optional<Inventory> inventory =  inventoryRepository.findBySkuCode(skuCode);
        return inventory.isPresent();
    }
}
