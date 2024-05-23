package com.jsbs.casemall.repository;

import com.jsbs.casemall.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order,Long> {

}
