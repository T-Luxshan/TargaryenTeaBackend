package com.targaryentea.inventoryservice;

import com.targaryentea.inventoryservice.dto.InventoryRequest;
import com.targaryentea.inventoryservice.dto.InventoryResponse;
import com.targaryentea.inventoryservice.entity.Inventory;
import com.targaryentea.inventoryservice.repository.InventoryRepository;
import com.targaryentea.inventoryservice.service.InventoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@Testcontainers
@SpringBootTest
class InventoryServiceApplicationTests {
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
	private InventoryService inventoryService;
	@DynamicPropertySource
	static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
		dynamicPropertyRegistry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
		dynamicPropertyRegistry.add("spring.datasource.username", mySQLContainer::getUsername);
		dynamicPropertyRegistry.add("spring.datasource.password", mySQLContainer::getPassword);
	}
	@Autowired
	private InventoryRepository inventoryRepository;

// create Test database
	@BeforeEach
	void setup() {
		// Clean the database
		inventoryRepository.deleteAll();

		// Add test data to DB
		Inventory inventory = Inventory.builder()
				.skuCode("Tea_1")
				.quantity(20)
				.productName("Targaryen")
				.build();
		inventoryRepository.save(inventory);
	}

	@Test
	void checkStock() throws Exception {
		InventoryRequest inventoryRequest =createInventoryRequest();
		InventoryResponse inventoryResponse=createInventoryResponse();

		String requestJson = objectMapper.writeValueAsString(List.of(inventoryRequest));
		String responseJson = objectMapper.writeValueAsString(List.of(inventoryResponse));


		mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/inventory")
				.contentType("application/json")
				.content(requestJson))
            .andExpect(status().isOk())
				.andExpect(content().json(responseJson));
	}
	private InventoryRequest createInventoryRequest() {
		return InventoryRequest.builder()
				.skuCode("Tea_1")
				.quantity(10)
				.build();
	}
	private InventoryResponse createInventoryResponse() {
		return InventoryResponse.builder()
				.skuCode("Tea_1")
				.isInStock(true)
				.build();
	}


//--- Stock out -----------------
	@Test
	void checkStock_out() throws Exception {
		InventoryRequest stockoutInventoryRequest =createStockoutInventoryRequest();
		InventoryResponse stockoutInventoryResponse=createStockoutInventoryResponse();
		String requestJson = objectMapper.writeValueAsString(List.of(stockoutInventoryRequest));
		String responseJson = objectMapper.writeValueAsString(List.of(stockoutInventoryResponse));

		mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/inventory")
						.contentType("application/json")
						.content(requestJson))
				.andExpect(status().isOk())
				.andExpect(content().json(responseJson));
	}
	private InventoryRequest createStockoutInventoryRequest() {
		return InventoryRequest.builder()
				.skuCode("Tea_1")
				.quantity(30)
				.build();
	}
	private InventoryResponse createStockoutInventoryResponse() {
		return InventoryResponse.builder()
				.skuCode("Tea_1")
				.isInStock(false)
				.build();
	}

}
