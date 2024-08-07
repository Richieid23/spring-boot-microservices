package tech.cyberboy.order_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.cyberboy.order_service.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
