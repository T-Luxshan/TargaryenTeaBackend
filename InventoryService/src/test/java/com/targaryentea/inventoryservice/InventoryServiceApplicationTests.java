package com.targaryentea.inventoryservice;

import com.targaryentea.inventoryservice.dto.InventoryRequest;
import com.targaryentea.inventoryservice.dto.InventoryResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.DynamicPropertyRegistrar;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import org.testcontainers.shaded.com.github.dockerjava.core.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
class InventoryServiceApplicationTests {
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
	@DynamicPropertySource
	static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry){
		dynamicPropertyRegistry.add("spring.datasource.url",mySQLContainer::getJdbcUrl);
		dynamicPropertyRegistry.add("spring.datasource.username",mySQLContainer::getUsername);
		dynamicPropertyRegistry.add("spring.datasource.password",mySQLContainer::getPassword);
	}
	@Test
	void ShouldCreateProduct() throws Exception {
		// create mock data---------------------------------------
	InventoryRequest inventoryRequest=createInventoryRequest(); // create inventory request bez this is the input of the api "api/v1/inventory"
	InventoryResponse inventoryResponse=createInventoryResponse();

	String inventoryRequestString =objectMapper.writeValueAsString(inventoryRequest); //content type json so convert request into string using ObjectMapper
	mockMvc.perform(post("api/v1/inventory")
				.contentType("application/json")
				.content(inventoryRequestString))
				.andExpect(status().isOk());
	}
	private InventoryRequest createInventoryRequest() {
		return InventoryRequest.builder()
				.skuCode("product-code-123")
				.quantity(10)
				.build();
	}
	private InventoryResponse createInventoryResponse() {
		return InventoryResponse.builder()
				.skuCode("product-code-123")
				.isInStock(true)
				.build();
	}

	// Add this Configuration class for ObjectMapper Bean bez spring autoconfiguration does not configure ObjectMapper
	@Configuration
	static class TestConfig {
		@Bean
		public ObjectMapper objectMapper() {
			return new ObjectMapper();
		}
	}

}
