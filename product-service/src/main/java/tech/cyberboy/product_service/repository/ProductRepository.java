package tech.cyberboy.product_service.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import tech.cyberboy.product_service.model.Product;

public interface ProductRepository extends MongoRepository<Product, String> {
}
