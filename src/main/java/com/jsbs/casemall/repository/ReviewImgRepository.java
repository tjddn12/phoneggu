package com.jsbs.casemall.repository;

import com.jsbs.casemall.entity.Review;
import com.jsbs.casemall.entity.ReviewImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewImgRepository extends JpaRepository<ReviewImg, Long> {
    List<ReviewImg> findByReviewIdOrderByIdAsc(Long reviewNo);
    //: 리뷰 이미지 아이디의 오름차순으로 가져오는 쿼리 메서드
}