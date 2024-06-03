package com.jsbs.casemall.repository;

import com.jsbs.casemall.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long>,
        QuerydslPredicateExecutor<Product>, ProductRepositoryCustom{


    List<Product> findByPrName(String prName);
    List<Product> findByProductCategory(int prCategory);
    List<Product> findByProductType(int prType);
    List<Product> findByPrNameOrPrDetail(String prName, String prDetail);
    List<Product> findByPrPriceLessThan(Long prPrice);
    List<Product> findByPrPriceLessThanOrderByPrPriceDesc(Long prPrice);

    @Query("select i from Product i where i.prDetail like " +
            "%:prDetail% order by i.prPrice desc")
    List<Product> findByPrDetail(@Param("prDetail") String prDetail);

    @Query(value="select * from product i where i.prDetail like " +
            "%:prDetail% order by i.prPrice desc", nativeQuery = true)
    List<Product> findByPrDetailByNative(@Param("prDetail") String prDetail);
}
