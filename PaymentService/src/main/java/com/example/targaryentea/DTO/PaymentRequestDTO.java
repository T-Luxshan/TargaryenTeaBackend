package com.example.targaryentea.DTO;

import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor

public class PaymentRequestDTO {
//    private Long amount;
//    private String currency;
//    private Long quantity;
//    private String name;
    private List<ProductDTO> products;
}
