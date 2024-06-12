package com.jsbs.casemall.repository;

import com.jsbs.casemall.entity.Product;
import com.jsbs.casemall.entity.Review;
import com.jsbs.casemall.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long>, ReviewRepositoryCustom {
    List<Review> findReviewsByUserId(Users userId);
    List<Review> findReviewsByPrName(Product prName);
}