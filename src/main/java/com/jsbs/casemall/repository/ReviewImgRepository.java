package com.jsbs.casemall.repository;

import com.jsbs.casemall.entity.Review;
import com.jsbs.casemall.entity.ReviewImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewImgRepository extends JpaRepository<ReviewImg, Long> {
    ReviewImg findByReview(Review review);
//    List<ReviewImg> findByReviewReviewNoOrderByIdAsc(Long reviewNo);
    void deleteByReviewId(Long reviewId);
    //: 리뷰 이미지 아이디의 오름차순으로 가져오는 쿼리 메서드
}