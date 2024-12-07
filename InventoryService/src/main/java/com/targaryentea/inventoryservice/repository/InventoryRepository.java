package com.targaryentea.inventoryservice.repository;

import com.targaryentea.inventoryservice.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    List<Inventory> findBySkuCodeIn(List<String> skuCode);

    Optional<Inventory> findBySkuCode(String skuCode);

    Inventory findByProductName(String productName);
}
