package com.jsbs.casemall.repository;

import com.jsbs.casemall.entity.Cart;
import com.jsbs.casemall.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart,Long> {
    Cart  findByUser(Users user);

}
