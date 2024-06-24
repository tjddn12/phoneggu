package com.jsbs.casemall.repository;

import com.jsbs.casemall.dto.ReviewFormDto;
import com.jsbs.casemall.entity.Order;
import com.jsbs.casemall.entity.Product;
import com.jsbs.casemall.entity.Review;
import com.jsbs.casemall.entity.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long>, ReviewRepositoryCustom {
    List<Review> findReviewsByUserId(Users userId);
    List<Review> findReviewsByPrId(Product product);
    Page<Review> findAll(Pageable pageable);
    Review getReviewDetails(Long reviewNo);
    int updateAReview(ReviewFormDto reviewFormDto);
}