package com.jsbs.casemall.repository;

import com.jsbs.casemall.entity.Review;
import com.jsbs.casemall.entity.ReviewImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewImgRepository extends JpaRepository<ReviewImg, Long> {
    List<ReviewImg> findByReviewOrderByIdAsc(Review reviewNo);
    ReviewImg findByIdAndReviewMainImg(Long id, String reviewMainImg);
    List<ReviewImg> findByReview(Review reviewNo);
}