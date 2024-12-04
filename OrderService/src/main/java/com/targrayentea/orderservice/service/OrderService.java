package com.targrayentea.orderservice.service;

import com.targrayentea.orderservice.dto.*;
import com.targrayentea.orderservice.entity.Order;
import com.targrayentea.orderservice.entity.OrderLineItems;
import com.targrayentea.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;

    public OrderResponse placeOrder(OrderDTO orderDTO) {

        List<OrderLineItems> orderLineItems = orderDTO.getOrderLineItemsDtoList().stream()
                .map(this::mapToDto)
                .toList();

        LocalDateTime now = LocalDateTime.now();

        Order order = Order.builder()
            .orderNumber(UUID.randomUUID().toString())
            .orderLineItemsList(orderLineItems)
            .orderDate(now)
            .userId(orderDTO.getUserId())
            .build();

//        List<String> skuCodes = order.getOrderLineItemsList()
//                                .stream()
//                                .map(OrderLineItems::getSkuCode)
//                                .toList();

        List<InventoryRequest> inventoryRequests = order.getOrderLineItemsList()
                .stream()
                .map(OrderLineItems ->
                    InventoryRequest.builder()
                            .skuCode(OrderLineItems.getSkuCode())
                            .quantity(OrderLineItems.getQuantity())
                            .build()

                )
                .toList();


//        Call inventory service, Place order only of product is in stock.
        InventoryResponse[] inventoryResponses =  webClientBuilder.build().post()
                .uri("http://inventory-service/api/v1/inventory")
                .bodyValue(inventoryRequests)
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();

        boolean allProductStock = Arrays.stream(inventoryResponses)
                .allMatch(InventoryResponse::isInStock);
        if(allProductStock){
            orderRepository.save(order);
            return OrderResponse.builder()
                    .status("success")
                    .message("Order placed successfully.")
                    .orderNumber(order.getOrderNumber())
                    .estimatedDelivery(now.plusDays(5).toLocalDate())
                    .build();
        } else {
            return OrderResponse.builder()
                    .status("failed")
                    .message("Order cannot be placed due to out of stock.")
                    .orderNumber(order.getOrderNumber())
                    .estimatedDelivery(null)
                    .build();
        }



    }

    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItems orderLineItems = OrderLineItems.builder()
                .price(orderLineItemsDto.getPrice())
                .quantity(orderLineItemsDto.getQuantity())
                .skuCode(orderLineItemsDto.getSkuCode())
                .productName(orderLineItemsDto.getProductName())
                .build();
        return orderLineItems;
    }

    public List<OrderDTO> getAllOrder() {
        List<Order> orders = orderRepository.findAll();

//        ArrayList<OrderDTO> orderDTOS = new ArrayList<>();

        List<OrderDTO> orderDTOs = orders.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());

        return orderDTOs;
    }

    private OrderDTO mapToDto(Order order) {

        ArrayList<OrderLineItemsDto> orderLineItemsDtos = new ArrayList<>();

        OrderLineItemsDto orderLineItemsDto;
        for (OrderLineItems orderLineItems: order.getOrderLineItemsList()) {
            orderLineItemsDto = OrderLineItemsDto.builder()
                    .id(orderLineItems.getId())
                    .skuCode(orderLineItems.getSkuCode())
                    .quantity(orderLineItems.getQuantity())
                    .price(orderLineItems.getPrice())
                    .build();
            orderLineItemsDtos.add(orderLineItemsDto);
        }

        return OrderDTO.builder()
                .userId(order.getUserId())
                .id(order.getId())
                .orderNumber(order.getOrderNumber())
                .orderDate(order.getOrderDate())
                .orderLineItemsDtoList(orderLineItemsDtos)
                .build();
    }




}
