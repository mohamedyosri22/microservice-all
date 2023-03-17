package com.spring.inventoryservice;

import com.spring.inventoryservice.model.Inventory;
import com.spring.inventoryservice.repository.InventoryRepo;
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
	public CommandLineRunner runner(InventoryRepo inventoryRepo){
		return args -> {
			Inventory inventory1 =new Inventory();
			inventory1.setQuantity(100);
			inventory1.setSkuCode("iphone_13");

			Inventory inventory2 =new Inventory();
			inventory2.setQuantity(0);
			inventory2.setSkuCode("iphone_13_red");

			inventoryRepo.save(inventory1);
			inventoryRepo.save(inventory2);
		};
	}
}
