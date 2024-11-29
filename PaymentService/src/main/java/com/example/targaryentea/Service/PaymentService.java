package com.example.targaryentea.Service;
import com.example.targaryentea.DTO.PaymentRequestDTO;
import com.example.targaryentea.DTO.ProductDTO;
import com.example.targaryentea.DTO.StripeResponse;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class PaymentService {

    //-> product name, amount, quantity,currency
    //-> return session id and url
    @Value("${stripe.secretKey}")
    private String secretkey;

    public StripeResponse checkoutProducts(PaymentRequestDTO paymentRequest ) {
        Stripe.apiKey = secretkey;
        // Getting from stripe JAR
        // Create Product Data
        List<SessionCreateParams.LineItem> lineItems=new ArrayList<>();
        for (ProductDTO product : paymentRequest.getProducts()) {
            SessionCreateParams.LineItem.PriceData.ProductData productData =
                    SessionCreateParams.LineItem.PriceData.ProductData.builder()
                            .setName(product.getName())
                            .build();

            // Create Price Data
            long amount = product.getPrice() * 100;
            SessionCreateParams.LineItem.PriceData priceData =
                    SessionCreateParams.LineItem.PriceData.builder()
                            .setCurrency(product.getCurrency() == null ? "USD" : product.getCurrency())
                            .setUnitAmount(amount)
                            .setProductData(productData)
                            .build();


            SessionCreateParams.LineItem lineItem =
                    SessionCreateParams.LineItem.builder()
                            .setQuantity((long) product.getQuantity()) // e.g., 2 items
                            .setPriceData(priceData)
                            .build();
            lineItems.add(lineItem);
        }
        SessionCreateParams.ShippingAddressCollection shippingAddressCollection =
                SessionCreateParams.ShippingAddressCollection.builder()
                        .addAllowedCountry(SessionCreateParams.ShippingAddressCollection.AllowedCountry.US)
                        .addAllowedCountry(SessionCreateParams.ShippingAddressCollection.AllowedCountry.CA)
                        .build();

        // Create ShippingAddressCollection
        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:8080/success")
                .setCancelUrl("http://localhost:8080/cancel")
                .setShippingAddressCollection(shippingAddressCollection)
                .addAllLineItem(lineItems)
                .putMetadata("I agree with your terms of service", "true")
                .build();

        try {
            Session session = Session.create(params);
            return StripeResponse.builder()
                    .status("SUCCESS")
                    .message("Payment session created")
                    .sessionId(session.getId())
                    .sessionUrl(session.getUrl())
                    .build();
        } catch (StripeException ex) {
            ex.printStackTrace();
            return StripeResponse.builder()
                    .status("FAILED")
                    .message("Error creating payment session: " + ex.getMessage())
                    .build();
        }

    }

}