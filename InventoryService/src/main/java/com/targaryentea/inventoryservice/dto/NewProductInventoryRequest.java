package com.targaryentea.inventoryservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewProductInventoryRequest {
    private String productName;
    private String skuCode;
    private Integer quantity;;

}
