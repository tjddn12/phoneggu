package com.jsbs.casemall.repository;

import com.jsbs.casemall.entity.Review;

import java.util.List;

public interface ReviewRepositoryCustom {
    List<Review> findReviewsByUserId(String userId);
    List<Review> findReviewsByProductId(String productId);
    int incrementReviewViewCount(int reviewNo);
//    int checkCurrReviewNo();
////    int attachReviewImages(AttachmentDto attachment); //: 첨부 이미지는 뷰에서 처리
//    int postAReview(ReviewDto reviewDto);
////    String getPaymentNoByOrderNo(String orderNo);
//    //    int savePoints(Point point); //: 포인트 기능 추후 구현.
//    int incrementReviewViewCount(int reviewNo);
////    String getAttachmentByReviewNo(int reviewNo, int num);
//    ReviewDto getReviewDetails(int reviewNo);
//    int checkReviewNoToEdit(String memberId, String orderNo, int optionNo);
//    int updateAReview(ReviewDto reviewDto);
////    int checkAttachedFiles(int reviewNo);
}