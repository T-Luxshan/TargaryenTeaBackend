package com.targaryentea.productservice;

import com.targaryentea.productservice.dto.ProductRequest;
import com.targaryentea.productservice.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import com.targaryentea.productservice.dto.ProductRequest;
import com.targaryentea.productservice.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class ProductServiceApplicationTests {

    @Container
    static MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:5.7.34")
            .withDatabaseName("test_db")
            .withUsername("test_user")
            .withPassword("test_pass");
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ProductRepository productRepository;

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
        dynamicPropertyRegistry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
        dynamicPropertyRegistry.add("spring.datasource.username", mySQLContainer::getUsername);
        dynamicPropertyRegistry.add("spring.datasource.password", mySQLContainer::getPassword);
    }

    //---------------------------------------------------CREATE PRODUCT------------------------------------------
    //01.Valid Product request
    @Test
    void CreateProduct() throws Exception {
        ProductRequest validProductRequest = getProductRequest();
        String productRequestString = objectMapper.writeValueAsString(validProductRequest);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/product")
                        .contentType("application/json")
                        .content(productRequestString))
                .andExpect(status().isOk());
//        Assertions.assertEquals(1, productRepository.findAll().size());
    }

    private ProductRequest getProductRequest() {
        return ProductRequest.builder()
                .id(null)
                .name("Tea")
                .stock(10)
                .description("Import from China")
                .price(100.0)
                .image_url("http://example.com/image.jpg")
                .build();
    }

    //02.Check with missing parameter
    @Test
    void CreateBadProductWithNullName() throws Exception {
        ProductRequest invalidProductRequest = getBadProductWithNullName();
        String productRequestString = objectMapper.writeValueAsString(invalidProductRequest);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/product")
                        .contentType("application/json")
                        .content(productRequestString))
                .andExpect(status().isBadRequest()) // Expect 400 Bad Request
                .andExpect(result -> {
                    // Check for specific validation error message
                    String responseBody = result.getResponse().getContentAsString();
                    assertTrue(responseBody.contains("Product name cannot be empty"));
                });
    }

    private ProductRequest getBadProductWithNullName() {
        return ProductRequest.builder()
                .id(null)
                .name(null)
                .stock(10)
                .description("Import from China")
                .price(100.0)
                .image_url("http://example.com/image.jpg")
                .build();
    }

    //03.Check Description
    @Test
    void CreateBadProductWithInvalidDescription() throws Exception {
        ProductRequest invalidProductRequest = getBadProductWithInvalidDescription();
        String productRequestString = objectMapper.writeValueAsString(invalidProductRequest);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/product")
                        .contentType("application/json")
                        .content(productRequestString))
                .andExpect(status().isBadRequest()) // Expect 400 Bad Request
                .andExpect(result -> {
                    String responseBody = result.getResponse().getContentAsString();
                    assertTrue(responseBody.contains("Description must be between 10 and 500 characters"));
                });

    }

    private ProductRequest getBadProductWithInvalidDescription() {
        return ProductRequest.builder()
                .id(null)
                .name("Tea")
                .stock(10)
                .description("China")
                .price(100.0)
                .image_url("http://example.com/image.jpg")
                .build();
    }

    //04.Check Price
    @Test
    void CreateBadProductWithInvalidPrice() throws Exception {
        ProductRequest invalidProductRequest = getBadProductWithInvalidPrice();
        String productRequestString = objectMapper.writeValueAsString(invalidProductRequest);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/product")
                        .contentType("application/json")
                        .content(productRequestString))
                .andExpect(status().isBadRequest()) // Expect 400 Bad Request
                .andExpect(result -> {
                    // Check if the response body contains the expected validation error for 'stock'
                    String responseBody = result.getResponse().getContentAsString();
                    assertTrue(responseBody.contains("Price must be greater than 0"));
                });

    }


    //-----------------------------------------------------CREATE PRODUCT------------------------------------------
    private ProductRequest getBadProductWithInvalidPrice() {
        return ProductRequest.builder()
                .id(null)
                .name("Tea")
                .stock(10)
                .description("Import from China")
                .price(-10)
                .image_url("http://example.com/image.jpg")
                .build();
    }

    //03.Check Stock
    @Test
    void CreateBadProductWithInvalidStock() throws Exception {
        ProductRequest invalidProductRequest = getBadProductWithInvalidStock();
        String productRequestString = objectMapper.writeValueAsString(invalidProductRequest);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/product")
                        .contentType("application/json")
                        .content(productRequestString))
                .andExpect(status().isBadRequest()) // Expect 400 Bad Request
                .andExpect(result -> {
                    // Check if the response body contains the expected validation error for 'stock'
                    String responseBody = result.getResponse().getContentAsString();
                    assertTrue(responseBody.contains("Stock cannot be negative"));
                });

    }

    private ProductRequest getBadProductWithInvalidStock() {
        return ProductRequest.builder()
                .id(null)
                .name("Tea")
                .stock(-10)
                .description("Import from China")
                .price(100.0)
                .image_url("http://example.com/image.jpg")
                .build();
    }

    //-----------------------------------------------------VIEW ALL PRODUCT------------------------------------------
    @Test
    void viewAllProduct() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/product"))
                .andExpect(status().isOk());
//                .andExpect(jsonPath("$.size()").value(2));
    }
    @Test
    void viewAllWithoutProduct() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/product"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(0));
    }
    //-----------------------------------------------------RETRIEVE BY ID------------------------------------------
    @Test
    void getByProductId() throws Exception {
        //create product
        ProductRequest validProductRequest = getProductRequest();
        String productRequestString = objectMapper.writeValueAsString(validProductRequest);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/product")
                        .contentType("application/json")
                        .content(productRequestString))
                .andExpect(status().isOk());
        //get the id of created product
        Long productId= productRepository.findAll().get(0).getId();
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/product/{id}",productId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(productId))
                .andExpect(jsonPath("$.name").value("Tea"))
                .andExpect(jsonPath("$.description").value("Import from China"))
                .andExpect(jsonPath("$.stock").value(10))
                .andExpect(jsonPath("$.price").value(100.0))
                .andExpect(jsonPath("$.image_url").value("http://example.com/image.jpg"));

    }
    //-----------------------------------------------------RETRIEVE BY ID------------------------------------------
    @Test
    void deleteByProductId() throws Exception {
        //create product
        ProductRequest validProductRequest = getProductRequest();
        String productRequestString = objectMapper.writeValueAsString(validProductRequest);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/product")
                        .contentType("application/json")
                        .content(productRequestString))
                .andExpect(status().isOk());
        //get the id of created product
        Long productId= productRepository.findAll().get(0).getId();
        //call delete api
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/product/{id}",productId))
                .andExpect(status().isOk());

        Assertions.assertTrue(productRepository.findAllById(Collections.singleton(productId)).isEmpty());

    }
}
@Configuration
class TestConfig {
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}