package com.targrayentea.orderservice.repository;

import com.targrayentea.orderservice.dto.BestSellingDTO;
import com.targrayentea.orderservice.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT o.productName " +
            "FROM OrderLineItems o " +
            "GROUP BY o.skuCode, o.productName " +
            "ORDER BY COUNT(o.id) ASC")
    List<Object[]> getBestSellers();

}
