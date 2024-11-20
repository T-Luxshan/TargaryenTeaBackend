package com.targaryentea.productservice.service;

import com.targaryentea.productservice.dto.ProductRequest;
import com.targaryentea.productservice.entity.Product;
import com.targaryentea.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.URI;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public String createProduct(ProductRequest productRequest) {

        if(productRepository.existsByName(productRequest.getName())){
            throw new IllegalArgumentException("Product with the same name already exists");

        }
            Product product = Product.builder()
                    .name(productRequest.getName())
                    .description(productRequest.getDescription())
                    .price(productRequest.getPrice())
                    .stock(productRequest.getStock())
                    .image_url(productRequest.getImage_url())
                    .build();
            productRepository.save(product);
            return "Product Created";




    }
}
