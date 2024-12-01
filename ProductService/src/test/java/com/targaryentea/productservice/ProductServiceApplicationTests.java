package com.targaryentea.productservice;

import com.targaryentea.productservice.dto.ProductRequest;
import com.targaryentea.productservice.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class ProductServiceApplicationTests {

    @Container
    static MySQLContainer<?> mySQLContainer=new MySQLContainer<>("mysql:5.7.34")
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
    static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry){
        dynamicPropertyRegistry.add("spring.datasource.url",mySQLContainer::getJdbcUrl);
        dynamicPropertyRegistry.add("spring.datasource.username",mySQLContainer::getUsername);
        dynamicPropertyRegistry.add("spring.datasource.password",mySQLContainer::getPassword);
    }
    //-----------------------------------------------------CREATE PRODUCT------------------------------------------

    @Test
    void CreateProduct() throws Exception {
        ProductRequest validProductRequest=getProductRequest();
        String productRequestString =objectMapper.writeValueAsString(validProductRequest);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/product")
                        .contentType("application/json")
                        .content(productRequestString))
                .andExpect(status().isOk());
        Assertions.assertTrue(productRepository.findAll().size()==1);
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
//    @Test
//    void CreateBadProductStock() throws Exception {
//        ProductRequest invalidProductRequest=getBadProductRequestStock();
//        String productRequestString =objectMapper.writeValueAsString(invalidProductRequest);
//        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/product")
//                        .contentType("application/json")
//                        .content(productRequestString))
//                .andExpect(status().isBadRequest());
////        Assertions.assertTrue(productRepository.findAll().size()==1);
//    }
//    private ProductRequest getBadProductRequestStock() {
//        return ProductRequest.builder()
//                .id(null)
//                .name("Tea")
//                .stock(-10)
//                .description("Import from China")
//                .price(100.0)
//                .image_url("http://example.com/image.jpg")
//                .build();
//    }

}
@Configuration
class TestConfig {
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
    }