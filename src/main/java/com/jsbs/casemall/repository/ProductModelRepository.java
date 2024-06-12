package com.jsbs.casemall.repository;

import com.jsbs.casemall.entity.ProductModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductModelRepository extends JpaRepository<ProductModel, Long> {

    @Query("SELECT pm FROM ProductModel pm WHERE pm.product.id IS NOT NULL")
    List<ProductModel> findAllWithNonNullPrId();
}
