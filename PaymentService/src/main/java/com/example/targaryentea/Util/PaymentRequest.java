package com.example.targaryentea.Util;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class PaymentRequest {
    private Double amount;
    private String currency;
    private String description;
//    private String firstName;
//    private String lastName;
    private String email;
    private String cardNumber;
    private String cardType;
    private Integer expiryMonth;
    private Integer expiryYear;
    private String cvv;
}
