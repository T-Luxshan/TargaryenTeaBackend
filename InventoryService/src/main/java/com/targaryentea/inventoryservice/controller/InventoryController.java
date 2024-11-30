package com.targaryentea.inventoryservice.controller;

import com.targaryentea.inventoryservice.dto.InventoryRequest;
import com.targaryentea.inventoryservice.dto.InventoryResponse;
import com.targaryentea.inventoryservice.repository.InventoryRepository;
import com.targaryentea.inventoryservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/inventory")
@RequiredArgsConstructor
public class InventoryController {

    final private InventoryService inventoryService;
    private final InventoryRepository inventoryRepository;

    @PostMapping
    public ResponseEntity<List<InventoryResponse>> isInStock(@RequestBody List<InventoryRequest> inventoryRequests){
        return ResponseEntity.ok(inventoryService.isInStock(inventoryRequests));
    }
}
