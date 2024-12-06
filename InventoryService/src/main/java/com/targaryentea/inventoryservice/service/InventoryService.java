package com.targaryentea.inventoryservice.service;

import com.targaryentea.inventoryservice.dto.InventoryRequest;
import com.targaryentea.inventoryservice.dto.InventoryResponse;
import com.targaryentea.inventoryservice.dto.NewProductInventoryRequest;
import com.targaryentea.inventoryservice.entity.Inventory;
import com.targaryentea.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;

//    @Transactional(readOnly = true)

    public List<InventoryResponse> isInStock(List<InventoryRequest> inventoryRequests) {
        // Extract SKU codes from inventoryRequests
        List<String> skuCodes = inventoryRequests.stream()
                .map(InventoryRequest::getSkuCode)
                .toList();

        // Fetch inventory for the given SKU codes
        List<Inventory> inventories = inventoryRepository.findBySkuCodeIn(skuCodes);
        boolean allInStock = inventoryRequests.stream()
                .allMatch(inventoryRequest -> {
                    Inventory inventory = inventories.stream()
                            .filter(inv -> inv.getSkuCode().equals(inventoryRequest.getSkuCode()))
                            .findFirst()
                            .orElseThrow(() -> new IllegalArgumentException("SKU Code not found: " + inventoryRequest.getSkuCode()));
                    return inventory.getQuantity() >= inventoryRequest.getQuantity();
                });

        // If all items are in stock, decrement the quantities
        if (allInStock) {
            inventoryRequests.forEach(inventoryRequest -> {
                Inventory inventory = inventories.stream()
                        .filter(inv -> inv.getSkuCode().equals(inventoryRequest.getSkuCode()))
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("SKU Code not found: " + inventoryRequest.getSkuCode()));
                inventory.setQuantity(inventory.getQuantity() - inventoryRequest.getQuantity());
                inventoryRepository.save(inventory); // Save updated inventory
            });
        }
        return inventoryRequests.stream()
                .map(inventoryRequest -> {
                    Inventory inventory = inventories.stream()
                            .filter(inv -> inv.getSkuCode().equals(inventoryRequest.getSkuCode()))
                            .findFirst()
                            .orElseThrow(() -> new IllegalArgumentException("SKU Code not found: " + inventoryRequest.getSkuCode()));
//                    boolean isInStock =inventory.getQuantity()>=inventoryRequest.getQuantity();
//                    if(isInStock){
//                        inventory.setQuantity(inventory.getQuantity()-inventoryRequest.getQuantity());
//                        inventoryRepository.save(inventory);
//                    }

                return InventoryResponse.builder()
                        .skuCode(inventoryRequest.getSkuCode())
                        .isInStock(inventory.getQuantity() >= inventoryRequest.getQuantity())
                        .build();
                })
                .toList();

//       return inventoryRepository.findBySkuCodeIn(skuCodes).stream()
//               .map(inventory ->
//                   InventoryResponse.builder()
//                           .skuCode(inventory.getSkuCode())
//                           .isInStock(inventory.getQuantity() > inventoryRequests.getFirst().getQuantity())
//                           .build()
//               ).toList();
    }

    public boolean updateInventory(InventoryRequest inventoryRequest) {
        Optional<Inventory> inventoryOptional = inventoryRepository.findBySkuCode(inventoryRequest.getSkuCode());
        if (inventoryOptional.isPresent()) {
            Inventory inventory = inventoryOptional.get();
            inventory.setQuantity(inventory.getQuantity()+inventoryRequest.getQuantity());
            inventoryRepository.save(inventory);
            return true;
        }
        return false;
    }


    public boolean addNewInventory(NewProductInventoryRequest inventoryRequest) {
        try {
            boolean isEmpty =inventoryRepository.findBySkuCode(inventoryRequest.getSkuCode()).isEmpty();
            if(isEmpty) {
                Inventory inventory = new Inventory();
                inventory.setSkuCode(inventoryRequest.getSkuCode());
                inventory.setQuantity(inventoryRequest.getQuantity());
                inventory.setProductName(inventoryRequest.getProductName());
                inventoryRepository.save(inventory);

                return true;
            }
            else{
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String findSkuCode(String productName) {
        Inventory inventory = inventoryRepository.findByProductName(productName);
        return inventory != null ? inventory.getSkuCode() : null;
    }

    public Integer getEachStock(String productName) {
        Inventory inventory = inventoryRepository.findByProductName(productName);
        return inventory != null ? inventory.getQuantity() : 0;
    }
}

