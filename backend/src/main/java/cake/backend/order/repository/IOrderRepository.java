package cake.backend.order.repository;

import cake.backend.auth.entity.User;
import cake.backend.order.entity.Order;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IOrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long userId);
}
