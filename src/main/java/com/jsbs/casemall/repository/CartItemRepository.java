package com.jsbs.casemall.repository;

import com.jsbs.casemall.entity.Cart;
import com.jsbs.casemall.entity.CartItem;
import com.jsbs.casemall.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem,Long> {

}
