package com.example.targaryentea.DTO;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StripeResponse {
 private String status;
 private String message;
 private String sessionId;
 private String sessionUrl;

}
