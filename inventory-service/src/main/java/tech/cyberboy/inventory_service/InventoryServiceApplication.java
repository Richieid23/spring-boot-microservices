package tech.cyberboy.inventory_service;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import tech.cyberboy.inventory_service.model.Inventory;
import tech.cyberboy.inventory_service.repository.InventoryRepository;

@SpringBootApplication
public class InventoryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryServiceApplication.class, args);
	}

	@Bean
	public CommandLineRunner loadData(InventoryRepository inventoryRepository) {
		return args -> {
			Inventory inventory1 = new Inventory();
			inventory1.setSkuCode("iphone_15");
			inventory1.setQuantity(100);

			Inventory inventory2 = new Inventory();
			inventory2.setSkuCode("samsung_s24");
			inventory2.setQuantity(0);

			if (inventoryRepository.findBySkuCode(inventory1.getSkuCode()).isEmpty()) {
				inventoryRepository.save(inventory1);
			}

			if (inventoryRepository.findBySkuCode(inventory2.getSkuCode()).isEmpty()) {
				inventoryRepository.save(inventory2);
			}

		};
	}
}
