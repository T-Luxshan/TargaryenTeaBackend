package com.targrayentea.orderservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {

    @NotBlank(message = "Product name cannot be empty")
    private List<OrderLineItemsDto> orderLineItemsDtoList;
}
