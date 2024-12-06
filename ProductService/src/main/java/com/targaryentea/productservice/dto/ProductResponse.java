package com.targaryentea.productservice.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ProductResponse {
    private Long id;
    private String name;
    private String description;
    private double price;
    private String image_url;
}
