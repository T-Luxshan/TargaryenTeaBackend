package com.targrayentea.orderservice.controller;

import com.targrayentea.orderservice.dto.OrderDTO;
import com.targrayentea.orderservice.dto.OrderLineItemsDto;
import com.targrayentea.orderservice.dto.OrderResponse;
import com.targrayentea.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> placeOrder(@RequestBody OrderDTO orderDTO){
        return ResponseEntity.ok(orderService.placeOrder(orderDTO));
    }

    @GetMapping
    public  ResponseEntity<List<OrderDTO>> getAllOrder(){
        return ResponseEntity.ok(orderService.getAllOrder());
    }
}
