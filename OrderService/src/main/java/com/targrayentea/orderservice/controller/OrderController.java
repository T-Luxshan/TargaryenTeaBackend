package com.targrayentea.orderservice.controller;

import com.targrayentea.orderservice.dto.OrderDTO;
import com.targrayentea.orderservice.dto.OrderLineItemsDto;
import com.targrayentea.orderservice.dto.OrderResponse;
import com.targrayentea.orderservice.service.OrderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
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
//    @CircuitBreaker(name="inventory",fallbackMethod = "fallbackMethod")
//    @TimeLimiter(name="inventory")
    public ResponseEntity<OrderResponse> placeOrder(@RequestBody OrderDTO orderDTO){
        return ResponseEntity.ok(orderService.placeOrder(orderDTO));
    }
//    public ResponseEntity<OrderResponse> fallbackMethod(OrderDTO orderDTO, Throwable throwable) {
//        System.err.println("Fallback called due to: " + throwable.getMessage());
//
//        return ResponseEntity.ok(
//                OrderResponse.builder()
//                        .status("failed")
//                        .message("Unable to place the order due to service unavailability. Please try again later.")
//                        .orderNumber(null)
//                        .estimatedDelivery(null)
//                        .build()
//        );
//    }
    @GetMapping
    public  ResponseEntity<List<OrderDTO>> getAllOrder(){
        return ResponseEntity.ok(orderService.getAllOrder());
    }
}
