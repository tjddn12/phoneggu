package com.jsbs.casemall.repository;

import com.jsbs.casemall.constant.OrderStatus;
import com.jsbs.casemall.entity.Order;
import com.jsbs.casemall.entity.OrderDetail;
import com.jsbs.casemall.entity.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order,Long> {
    Optional<Order> findByOrderId(String orderId); // 주문아이디로 찾기
    List<Order> findByUsersAndOrderStatus(Users user, OrderStatus orderStatus);//
    List<Order> findByOrderIdAndOrderStatus(String orderId, OrderStatus orderStatus);//

    List<Order> findAllByOrderDateBetween(LocalDateTime startDate, LocalDateTime endDate);
}
