package com.targaryentea.productservice.service;

import com.targaryentea.inventoryservice.dto.InventoryRequest;
import com.targaryentea.inventoryservice.dto.NewProductInventoryRequest;
import com.targaryentea.productservice.dto.ProductRequest;
import com.targaryentea.productservice.entity.Product;
import com.targaryentea.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final WebClient.Builder webClientBuilder;

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
        String skuCode = generateSkuCode(productRequest.getName());
        NewProductInventoryRequest inventoryRequest = new NewProductInventoryRequest(productRequest.getName(),skuCode, productRequest.getStock());

        try{
            //Call inventory service, Place new order to stock.
            webClientBuilder.build().post()
                    .uri("http://InventoryService/api/v1/inventory/add")
                    .bodyValue(inventoryRequest)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            return "Product Added";
        }catch(Exception e){
         return "Product can not be  added in inventory service "+e.getMessage();
        }
    }
    private String generateSkuCode(String name) {
        int randomThreeDigits = 100 + (int) (Math.random() * 900);
        return name.toLowerCase().replace(" ", "_") + "_" + randomThreeDigits;

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
