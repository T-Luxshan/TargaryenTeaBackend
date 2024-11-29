package com.example.targaryentea.Controller;
import com.example.targaryentea.DTO.PaymentRequestDTO;
import com.example.targaryentea.DTO.StripeResponse;
import com.example.targaryentea.Service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller

@Slf4j
@RequestMapping("/api/v1")
public class PaymentController {
    @Autowired
        private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }
    @PostMapping("/checkout")
    public ResponseEntity<StripeResponse> checkoutProducts(@RequestBody PaymentRequestDTO paymentRequest ){
           StripeResponse stripeResponse= paymentService.checkoutProducts(paymentRequest);
           return ResponseEntity.ok(stripeResponse);
        }
}
