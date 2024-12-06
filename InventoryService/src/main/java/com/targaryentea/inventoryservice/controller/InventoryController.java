package com.targaryentea.inventoryservice.controller;

import com.targaryentea.inventoryservice.dto.InventoryRequest;
import com.targaryentea.inventoryservice.dto.InventoryResponse;
import com.targaryentea.inventoryservice.dto.NewProductInventoryRequest;
import com.targaryentea.inventoryservice.repository.InventoryRepository;
import com.targaryentea.inventoryservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/inventory")
@RequiredArgsConstructor
public class InventoryController {

    final private InventoryService inventoryService;

    @PostMapping
    public ResponseEntity<List<InventoryResponse>> isInStock(@RequestBody List<InventoryRequest> inventoryRequests){
        return ResponseEntity.ok(inventoryService.isInStock(inventoryRequests));
    }
    @PutMapping
    public ResponseEntity<String> updateInventory(@RequestBody InventoryRequest inventoryRequest){
        boolean updated=inventoryService.updateInventory(inventoryRequest);
        if(updated)
        return ResponseEntity.ok("Added Successfully");
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Inventory item not found");
    }
    @PostMapping("/add")
    public ResponseEntity<String> addNewInventory(@RequestBody NewProductInventoryRequest inventoryRequest){
        boolean updated=inventoryService.addNewInventory(inventoryRequest);
        if(updated)
            return ResponseEntity.ok("Added Successfully");
        else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to add inventory");
    }
}
