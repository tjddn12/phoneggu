package com.jsbs.casemall.repository;

import com.jsbs.casemall.dto.ReviewDto;
import com.jsbs.casemall.dto.ReviewFormDto;
import com.jsbs.casemall.entity.Product;
import com.jsbs.casemall.entity.Review;
import com.jsbs.casemall.entity.Users;

//JPARepository 메서드 구현을 막기 위한 인터페이스,
public interface ReviewRepositoryCustom {
    //리뷰 게시판 페이징 처리 메서드 선언 예정
    Review getReviewDetails(Long reviewNo);
    //    int checkReviewNoToEdit(Users userId, Product prName);
    int updateAReview(ReviewFormDto reviewFormDto);
    //    int incrementReviewViewCount(int reviewNo); //: 조회수 메서드, 추후 추가 or JS로 처리.
//    int checkCurrReviewNo();
////    int attachReviewImages(AttachmentDto attachment); //: 첨부 이미지는 뷰에서 처리
//    int postAReview(ReviewDto reviewDto);
////    String getPaymentNoByOrderNo(String orderNo);
//    //    int savePoints(Point point); //: 포인트 기능 추후 구현.
//    int incrementReviewViewCount(int reviewNo);
////    String getAttachmentByReviewNo(int reviewNo, int num);
//    ReviewDto getReviewDetails(int reviewNo);
////    int checkAttachedFiles(int reviewNo);
}