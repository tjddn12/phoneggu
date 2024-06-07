package com.jsbs.casemall.service;

import com.jsbs.casemall.dto.ReviewDto;

import java.util.List;

public interface ReviewService {
    List<ReviewDto> getReviewsByUserId(String userId);
    List<ReviewDto> getReviewsByProductId(String productId);
    ReviewDto writeReview(ReviewDto reviewDto);
    ReviewDto editReview(Long reviewNo, ReviewDto reviewDto);
    void deleteReview(Long reviewNo);
    int incrementReviewViewCount(int reviewNo);
}