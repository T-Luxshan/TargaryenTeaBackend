package com.example.targaryentea.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Unique identifier for each payment in your database
    private String paymentId; // The PayPal payment ID returned by PayPal
    private String payerId; // The PayPal payer ID returned by PayPal
    private Double amount; // Amount charged for the payment
    private String currency; // Currency of the payment (e.g., USD)


}
