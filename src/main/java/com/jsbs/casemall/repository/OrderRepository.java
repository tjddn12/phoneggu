package com.jsbs.casemall.repository;

import com.jsbs.casemall.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order,Long> {
    Optional<Order> findByOrderId(String orderId); // 주문아이디로 찾기
}
