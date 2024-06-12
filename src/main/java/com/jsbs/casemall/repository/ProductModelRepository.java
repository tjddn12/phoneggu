package com.jsbs.casemall.repository;

import com.jsbs.casemall.entity.ProductModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductModelRepository extends JpaRepository<ProductModel, Long> {

    @Query("SELECT pm FROM ProductModel pm WHERE pm.product.id = :productId")
    List<ProductModel> findByProductId(@Param("productId") Long productId);
}
