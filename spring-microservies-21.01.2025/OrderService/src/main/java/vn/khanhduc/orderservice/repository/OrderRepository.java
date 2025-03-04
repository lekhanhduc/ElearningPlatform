package vn.khanhduc.orderservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.khanhduc.orderservice.model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
}
