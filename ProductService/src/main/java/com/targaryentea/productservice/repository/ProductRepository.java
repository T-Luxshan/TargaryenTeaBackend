package com.targaryentea.productservice.repository;

import com.targaryentea.productservice.dto.ProductResponse;
import com.targaryentea.productservice.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>{

    boolean existsByName(String name);
    @Query("SELECT new com.targaryentea.productservice.dto.ProductResponse(p.id, p.name, p.description, p.price, p.image_url) " +
            "FROM Product p WHERE p.id IN (SELECT MAX(p1.id) FROM Product p1 GROUP BY p1.name)")
    List<ProductResponse> findAllByName();


}
