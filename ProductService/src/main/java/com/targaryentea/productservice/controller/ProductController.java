package com.targaryentea.productservice.controller;

import com.targaryentea.productservice.dto.ProductRequest;
import com.targaryentea.productservice.service.ProductService;
import com.targrayentea.orderservice.dto.BestSellingDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("api/v1/product")
@RequiredArgsConstructor
@CrossOrigin("*")
public class ProductController {

    @Autowired
    private final ProductService productService;

    @PostMapping
    public ResponseEntity<String> createProduct(@Valid @RequestBody ProductRequest productRequest) {
            return ResponseEntity.ok(productService.createProduct(productRequest));

    }
    @GetMapping
    public ResponseEntity<List<ProductRequest>> getAllProducts(){
        return ResponseEntity.ok(productService.getAllProducts());
    }
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id){
        return ResponseEntity.ok(productService.deleteProduct(id));
    }

    @GetMapping("{id}")
    public ResponseEntity<ProductRequest> getProductById(@PathVariable Long id){
        return ResponseEntity.ok(productService.getProductById(id));
    }
    @PostMapping("/bestseller")
    public ResponseEntity<BestSellingDTO> getProductPrice(@RequestBody String productName){
        return ResponseEntity.ok(productService.getProductPrice(productName));
    }

}
