package com.jsbs.casemall.repository;

import com.jsbs.casemall.constant.ProductCategory;
import com.jsbs.casemall.constant.ProductType;
import com.jsbs.casemall.entity.Product;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long>,
        QuerydslPredicateExecutor<Product>, ProductRepositoryCustom{

    List<Product> findByProductCategory(ProductCategory productCategory);
    List<Product> findByProductType(ProductType productType);
    List<Product> findAll(Sort sort);

    List<Product> findByProductCategoryAndProductType(ProductCategory category, ProductType type, Sort sort);

    List<Product> findByProductCategory(ProductCategory category, Sort sort);

    List<Product> findByProductType(ProductType type, Sort sort);
}
