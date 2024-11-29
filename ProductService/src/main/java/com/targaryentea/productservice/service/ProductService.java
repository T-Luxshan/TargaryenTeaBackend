package com.targaryentea.productservice.service;

import com.targaryentea.productservice.dto.ProductRequest;
import com.targaryentea.productservice.entity.Product;
import com.targaryentea.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    public List<ProductRequest> getAllProducts() {
        List<Product> products = productRepository.findAll();

        ArrayList<ProductRequest> productRequests = new ArrayList<>();

        ProductRequest productRequest;
        for (Product product: products) {
            productRequest = ProductRequest.builder()
                    .id(product.getId())
                    .name(product.getName())
                    .description(product.getDescription())
                    .price(product.getPrice())
                    .stock(product.getStock())
                    .image_url(product.getImage_url())
                    .build();
            productRequests.add(productRequest);
        }
        return productRequests;
    }

    public String deleteProduct(Long id) {
        productRepository.deleteById(id);
        return "Product deleted";
    }

    public ProductRequest getProductById(Long id) {
        Optional<Product> productTemp = productRepository.findById(id);
        if(productTemp.isPresent()){
            Product product = productTemp.get();
            return ProductRequest.builder()
                    .id(product.getId())
                    .name(product.getName())
                    .description(product.getDescription())
                    .price(product.getPrice())
                    .stock(product.getStock())
                    .image_url(product.getImage_url())
                    .build();
        }
        return new ProductRequest();

    }
}
