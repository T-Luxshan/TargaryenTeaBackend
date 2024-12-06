package com.targrayentea.orderservice.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderResponse {
    private String status;
    private String message;
    private String orderNumber;
    private LocalDate estimatedDelivery;

}
