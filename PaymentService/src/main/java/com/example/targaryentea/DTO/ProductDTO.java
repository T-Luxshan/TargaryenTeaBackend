package com.example.targaryentea.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor

public class ProductDTO {
    private String name;
    private long price;
    private int quantity;
    private String currency;

}
