package com.jsbs.casemall.repository;

import com.jsbs.casemall.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long>,
        QuerydslPredicateExecutor<Product>, ProductRepositoryCustom{

    List<Product> findByProductNm(String prName);
    List<Product> findByCategoriesId(Long categoriesId);
    List<Product> findByProductNmOrProductDetail(String prName, String productDetail);
    List<Product> findByPriceLessThan(Long prPrice);
    List<Product> findByPriceLessThanOrderByPriceDesc(Long prPrice);

    //    @Query(value = "select * from product where product_detail Like %:productDetail% order by price desc", nativeQuery = true)
    //    List<Product> findByProductDetail(@Param("productDetail") String productDetail);

    @Query("select i from Product i where i.productDetail like " +
            "%:productDetail% order by i.price desc")
    List<Product> findByProductDetail(@Param("productDetail") String productDetail);

    @Query(value="select * from product i where i.product_detail like " +
            "%:productDetail% order by i.price desc", nativeQuery = true)
    List<Product> findByProductDetailByNative(@Param("productDetail") String productDetail);
}
