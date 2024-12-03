package com.targaryentea.productservice;

import com.targaryentea.productservice.controller.ProductController;
import com.targaryentea.productservice.dto.ProductRequest;
import com.targaryentea.productservice.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@ExtendWith(MockitoExtension.class)
@ComponentScan(basePackages = "com.targaryentea.productservice")
class ProductServiceApplicationTests {

    @Container
    static MySQLContainer<?> mySQLContainer=new MySQLContainer<>("mysql:8.0.34")
            .withDatabaseName("test_db")
            .withUsername("test_user")
            .withPassword("test_pass");

    //Testing HTTP endpoints using MockMvc
    @Autowired
    private MockMvc mockMvc;

    // convert POJA object to Json ,Json to POJA object
    @Autowired
    private ObjectMapper objectMapper;


    @Mock
    private ProductService productService;
    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry){
        dynamicPropertyRegistry.add("spring.datasource.url",mySQLContainer::getJdbcUrl);
        dynamicPropertyRegistry.add("spring.datasource.username",mySQLContainer::getUsername);
        dynamicPropertyRegistry.add("spring.datasource.password",mySQLContainer::getPassword);
    }
    //-----------------------------------------------------CREATE PRODUCT------------------------------------------
    @Test
    void validProductRequest() throws Exception {

        ProductRequest validProductRequest=createProductRequest();
        when(productService.createProduct(validProductRequest)).thenReturn ("Product created Successfully");
        String productRequestString =objectMapper.writeValueAsString(validProductRequest);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/product/create")
                        .contentType("application/json")
                        .content(productRequestString))
                .andExpect(status().isOk());
        verify(productService).createProduct(validProductRequest);
        }
    private ProductRequest createProductRequest() {
        return ProductRequest.builder()
                .id(null)
                .name("Tea")
                .description("Export from Srilanka")
                .price(100.00)
                .stock(20)
                .image_url("http://example.com/image.jpg")
                .build();
    }
    @Test
    void invalidProductRequest_Stock() throws Exception {
        ProductRequest badProductRequest=createBadProductRequestWithStock();
        when(productService.createProduct(badProductRequest)).thenReturn ("Product creation failed");
        String productRequestString =objectMapper.writeValueAsString(badProductRequest);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/product/create")
                        .contentType("application/json")
                        .content(productRequestString))
                .andExpect(status().isBadRequest());
        verify(productService).createProduct(badProductRequest);
    }

    private ProductRequest createBadProductRequestWithStock() {
        return ProductRequest.builder()
                .id(null)
                .name("Tea")
                .description("Export from Srilanka")
                .price(100.00)
                .stock(-1)
                .image_url("http://example.com/image.jpg")
                .build();
    }
    //-----------------------------------------------------VIEW PRODUCT------------------------------------------

    @Configuration
    static class TestConfig {
        @Bean
        public ObjectMapper objectMapper() {
            return new ObjectMapper();
        }
    }
    }


