package vn.khanhduc.orderservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.khanhduc.orderservice.model.OrderDetail;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, String> {
}
