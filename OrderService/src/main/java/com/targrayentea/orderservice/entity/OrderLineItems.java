package com.targrayentea.orderservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "order_line_items")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderLineItems {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String skuCode;
    private BigDecimal price;
    private Integer quantity;

}