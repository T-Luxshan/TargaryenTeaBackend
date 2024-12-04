package com.example.targaryentea.Controller;
import com.example.targaryentea.DTO.PaymentRequestDTO;
import com.example.targaryentea.DTO.StripeResponse;
import com.example.targaryentea.Service.PaymentService;
import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

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
    // Endpoint for retrieving the payment status using the session ID
    @PostMapping("/payment-status")
    public ResponseEntity<String> getPaymentStatus(@RequestBody Map<String, String> request) {
        try {
            String sessionId = request.get("sessionId");

            Stripe.apiKey = "sk_test_51PyunwGLTHhXTuKyPURcqUK8njY2lmQWKAUF0Tzl1pywVJx4qXLAlZVd8mykmTZByVN6spkT0gsLcnqThSGBYZsX00gqH4Jpie";
            if (sessionId == null || sessionId.isEmpty()) {
                return ResponseEntity.badRequest().body("Missing sessionId in request");
            }
            // Retrieve CheckoutSession
            Session session = Session.retrieve(sessionId);

            // Check payment status
            String paymentStatus = session.getPaymentStatus();
            if ("paid".equals(paymentStatus)) {
                return ResponseEntity.ok("Payment successful!");
            } else {
                return ResponseEntity.ok("Payment failed or incomplete.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving payment status: " + e.getMessage());
        }
    }

}
