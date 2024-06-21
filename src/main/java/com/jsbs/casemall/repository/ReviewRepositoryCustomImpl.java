package com.jsbs.casemall.repository;

import com.jsbs.casemall.dto.ReviewDto;
import com.jsbs.casemall.dto.ReviewFormDto;
import com.jsbs.casemall.entity.QReview;
import com.jsbs.casemall.entity.Review;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

public class ReviewRepositoryCustomImpl implements ReviewRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Autowired
    public ReviewRepositoryCustomImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    }
    @Override
    public Review getReviewDetails(Long reviewNo) {
        QReview review = QReview.review;

        Review reviewEntity = queryFactory.selectFrom(review)
                .where(review.reviewNo.eq(reviewNo))
                .fetchOne();

        if (reviewEntity != null) {
            return reviewEntity;
        } else {
            return null;
        }
    }
    //    @Override
//    public int checkReviewNoToEdit(Users userId, Product prName){
//        //수정할 리뷰 번호 찾는 로직 구현 필요
//        QReview review = QReview.review;
//
//        Long reviewNo = queryFactory.select(review.reviewNo)
//                .from(review)
//                .where(review.userId.eq(userId).and(review.prName.eq(prName)))
//                .fetchOne();
//
//        return reviewNo != null ? reviewNo.intValue() : 0;
//    } //: 필요시 추후 추가.
    @Override
    @Transactional
    public int updateAReview(ReviewFormDto reviewFormDto){
        //리뷰 업데이트 로직 구현 필요
        QReview review = QReview.review;

        long updated = queryFactory.update(review) //.update(): DB에서 업데이트 작업을 수행하며,
                //이 작업의 결과로 영향을 받은 행의 수를 반환.
                .where(review.reviewNo.eq(reviewFormDto.getId()))
                .set(review.revwTitle, reviewFormDto.getRevwTitle())
                .set(review.revwContent, reviewFormDto.getRevwContent())//: 제목과 내용, 평점만 수정하면 충분.
                .set(review.revwRatings, reviewFormDto.getRevwRatings())
                .execute(); //.execute(): QueryDSL에서 작성한 코드를 실제로 DB에 전송하고 실행하는 메서드

        return (int) updated; //: 업데이트된 행의 수 반환.
    }
//    @Override
//    public List<Review> findReviewsByUserId(String userId){
//        QReview review = QReview.review;
//
//        return queryFactory.selectFrom(review)
//                .where(review.userId.eq(userId))
//                .fetch();
//    }
//    @Override
//    public List<Review> findReviewsByProductId(String productId){
//        QReview review = QReview.review;
//
//        return queryFactory.selectFrom(review)
//                .where(review.product.eq(productId))
//                .fetch();
//    }
//    @Override
//    public int incrementReviewViewCount(int reviewNo){
//        QReview review = QReview.review;
//
//        long updatedCount = queryFactory.update(review)
//                .set(review.revwHits, review.revwHits.add(1))
//                .where(review.reviewNo.eq(reviewNo))
//                .execute();
//
//        return (int) updatedCount;
}