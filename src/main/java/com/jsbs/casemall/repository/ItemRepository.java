package com.jsbs.casemall.repository;

import com.jsbs.casemall.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Product, Long> {
}
