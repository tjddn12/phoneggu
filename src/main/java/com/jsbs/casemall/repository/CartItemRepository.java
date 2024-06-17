package com.jsbs.casemall.repository;

import com.jsbs.casemall.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem,Long> {
    Optional<CartItem> findByProductAndProductModel(Product product, ProductModel productModel);

}
