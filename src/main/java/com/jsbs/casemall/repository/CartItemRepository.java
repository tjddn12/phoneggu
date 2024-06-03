package com.jsbs.casemall.repository;

import com.jsbs.casemall.entity.Cart;
import com.jsbs.casemall.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem,Long> {
}
