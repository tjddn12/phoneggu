package com.jsbs.casemall.repository;

import com.jsbs.casemall.entity.Product;
import com.jsbs.casemall.entity.ProductModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductModelRepository extends JpaRepository<ProductModel, Long> {

    @Query("SELECT pm FROM ProductModel pm WHERE pm.product.id = :productId")
    List<ProductModel> findByProductId(@Param("productId") Long productId);

    @Modifying
    @Query("DELETE FROM ProductModel pm WHERE pm.product.id IS NULL")
    void deleteByPrIdIsNull();

    @Modifying
    @Query("DELETE FROM ProductModel pm WHERE pm.productModelSelect IS NULL")
    void deleteByProductModelSelectIsNull();

    @Modifying
    @Query("DELETE FROM ProductModel pm WHERE pm.product.id = :productId")
    void deleteByProductId(@Param("productId") Long productId);

    void deleteByProduct(Product product);
}
