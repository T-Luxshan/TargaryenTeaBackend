package com.targaryentea.productservice.repository;

import com.targaryentea.productservice.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>{

    boolean existsByName(String name);
    @Query("SELECT p.id, p.name, p.description, p.price, p.stock FROM Product p  GROUP BY  p.name, p.description")
    List<Product> findAllByName();

}
