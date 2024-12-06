package com.targrayentea.orderservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDTO {
    private Long id;
    private String userId;
    private String orderNumber;
    private LocalDateTime orderDate;
    @NotBlank(message = "Product name cannot be empty")
    private List<OrderLineItemsDto> orderLineItemsDtoList;
}
