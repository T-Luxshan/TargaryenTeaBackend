package com.example.targaryentea.DTO;

import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor

public class PaymentRequestDTO {
    private List<ProductDTO> products;
    private String successURL;
    private String rejectURL;

}
