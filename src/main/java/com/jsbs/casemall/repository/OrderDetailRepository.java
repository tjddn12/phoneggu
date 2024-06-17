package com.jsbs.casemall.repository;

import com.jsbs.casemall.entity.OrderDetail;
import com.jsbs.casemall.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderDetailRepository extends JpaRepository<OrderDetail,Long> {
    //
//    Optional<OrderDetail> findByIdAndOrder_Users_UserId(Long orderDetailId, String userId);



}
