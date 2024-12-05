package com.targaryentea.inventoryservice;

import com.targaryentea.inventoryservice.entity.Inventory;
import com.targaryentea.inventoryservice.repository.InventoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
public class InventoryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryServiceApplication.class, args);
	}

	@Bean
	public CommandLineRunner loadData(InventoryRepository inventoryRepository){
		return args -> {
			Inventory cinnamon_tea = Inventory.builder()
					.productName("Cinnamon Tea")
					.skuCode("cinnamon_tea_0406")
					.quantity(1200)
					.build();

			Inventory mint_tea = Inventory.builder()
					.productName("Mint Tea")
					.skuCode("mint_tea_0912")
					.quantity(2000)
					.build();

			inventoryRepository.save(cinnamon_tea);
			inventoryRepository.save(mint_tea);
		};
	}



}
