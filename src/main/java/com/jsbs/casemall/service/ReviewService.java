package com.jsbs.casemall.service;

import com.jsbs.casemall.dto.ReviewDto;
import com.jsbs.casemall.dto.ReviewFormDto;
import com.jsbs.casemall.entity.Product;
import com.jsbs.casemall.entity.Review;
import com.jsbs.casemall.entity.Users;
import com.jsbs.casemall.repository.ReviewRepository;
import com.jsbs.casemall.repository.ReviewRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;
    private ReviewRepositoryCustom reviewRepositoryCustom;

    public List<Review> getAllReviews(){
        return reviewRepository.findAll();
    }
    public Optional<Review> getReviewByNo(Long reviewNo){
        return reviewRepository.findById(reviewNo);
    }
    public List<Review> getReviewsByUserId(Users userId){
        return reviewRepository.findReviewsByUserId(userId);
    }
    public List<Review> getReviewsByPrName(Product prName){
        return reviewRepository.findReviewsByPrName(prName);
    }
    public Review saveReview(Review review){
        return reviewRepository.save(review);
    }
    public void deleteReview(Long reviewNo){
        reviewRepository.deleteById(reviewNo);
    }
    //    public boolean updateReview(ReviewFormDto reviewFormDto){
//        int updatedRows = reviewRepositoryCustom.updateAReview(reviewFormDto);
//
//        return updatedRows > 0;
//    }
    @Transactional
    public Review updateAReview(ReviewFormDto reviewFormDto){
        int updatedRows = reviewRepositoryCustom.updateAReview(reviewFormDto);

        if(updatedRows > 0){
            return reviewRepositoryCustom.getReviewDetails(reviewFormDto.getReviewNo());
        }else{
            throw new RuntimeException("리뷰 수정 실패");
        }
    }

}