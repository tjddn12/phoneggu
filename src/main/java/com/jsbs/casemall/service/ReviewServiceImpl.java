package com.jsbs.casemall.service;

import com.jsbs.casemall.dto.ReviewDto;
import com.jsbs.casemall.entity.Review;
import com.jsbs.casemall.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;

    @Autowired
    public ReviewServiceImpl(ReviewRepository reviewRepository){
        this.reviewRepository = reviewRepository;
    }
    @Override
    public List<ReviewDto> getReviewsByUserId(String userId){
        List<Review> reviews = reviewRepository.findReviewsByUserId(userId);

        return reviews.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    @Override
    public List<ReviewDto> getReviewsByProductId(String productId){
        List<Review> reviews = reviewRepository.findReviewsByProductId(productId);

        return reviews.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    @Override
    public ReviewDto writeReview(ReviewDto reviewDto){
        Review review = convertToEntity(reviewDto);
        Review savedReview = reviewRepository.save(review);

        return convertToDto(savedReview);
    }
    @Override
    public ReviewDto editReview(Long reviewNo, ReviewDto reviewDto){
        Review existingReview = reviewRepository.findById(reviewNo)
                .orElseThrow(() -> new IllegalArgumentException("리뷰를 찾을 수 없습니다."));

        existingReview.setReviewNo(reviewDto.getReviewNo());
        existingReview.setOrderNo(reviewDto.getOrderNo());
        existingReview.setUserId(reviewDto.getUserId());
        existingReview.setRevwTitle(reviewDto.getRevwTitle());
        existingReview.setRevwContent(reviewDto.getRevwContent());
        existingReview.setRevwRegDate(reviewDto.getRevwRegDate());
        existingReview.setRevwHits(reviewDto.getRevwHits());
        existingReview.setRevwRatings(reviewDto.getRevwRatings());
        existingReview.setProduct(reviewDto.getProduct());

        Review updatedReview = reviewRepository.save(existingReview);

        return convertToDto(updatedReview);
    }
    @Override
    public void deleteReview(Long reviewNo){
        reviewRepository.deleteById(reviewNo);
    }
    private ReviewDto convertToDto(Review review) {
        ReviewDto dto = new ReviewDto();

        dto.setReviewNo(review.getReviewNo());
        dto.setOrderNo(review.getOrderNo());
        dto.setUserId(review.getUserId());
        dto.setRevwTitle(review.getRevwTitle());
        dto.setRevwContent(review.getRevwContent());
        dto.setRevwRegDate(review.getRevwRegDate());
        dto.setRevwHits(review.getRevwHits());
        dto.setRevwRatings(review.getRevwRatings());

        return dto;
    }
    private Review convertToEntity(ReviewDto dto){
        Review review = new Review();

        review.setReviewNo(dto.getReviewNo());
        review.setOrderNo(dto.getOrderNo());
        review.setRevwTitle(dto.getRevwTitle());
        review.setRevwContent(dto.getRevwContent());
        review.setRevwRegDate(dto.getRevwRegDate());
        review.setRevwHits(dto.getRevwRatings());
        review.setProduct(dto.getProduct());

        return review;
    }
    @Override
    public int incrementReviewViewCount(int reviewNo){
        return reviewRepository.incrementReviewViewCount(reviewNo);
    }
}