package com.jsbs.casemall.repository;

import com.jsbs.casemall.entity.QReview;
import com.jsbs.casemall.entity.Review;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ReviewRepositoryCustomImpl implements ReviewRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Autowired
    public ReviewRepositoryCustomImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    }
    @Override
    public List<Review> findReviewsByUserId(String userId){
        QReview review = QReview.review;

        return queryFactory.selectFrom(review)
                .where(review.userId.eq(userId))
                .fetch();
    }
    @Override
    public List<Review> findReviewsByProductId(String productId){
        QReview review = QReview.review;

        return queryFactory.selectFrom(review)
                .where(review.product.eq(productId))
                .fetch();
    }
    @Override
    public int incrementReviewViewCount(int reviewNo){
        QReview review = QReview.review;

        long updatedCount = queryFactory.update(review)
                .set(review.revwHits, review.revwHits.add(1))
                .where(review.reviewNo.eq(reviewNo))
                .execute();

        return (int) updatedCount;
    }
}