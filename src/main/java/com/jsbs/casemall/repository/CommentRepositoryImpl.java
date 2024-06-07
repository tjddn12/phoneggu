package com.jsbs.casemall.repository;

import com.jsbs.casemall.entity.Comment;
import com.jsbs.casemall.entity.QComment;
import com.jsbs.casemall.entity.QUsers;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CommentRepositoryImpl implements CommentRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    //: QueryDSL 레포지토리 사용
    @Autowired
    public CommentRepositoryImpl(JPAQueryFactory queryFactory){
        this.queryFactory = queryFactory;
    }
    @Override
    public List<Comment> findCommentsByRefBoardAndRefPostNo(String refBoard, int refPostNo){
        QComment comment = QComment.comment;
        //: Q클래스 추후 생성 예정.
        return queryFactory.selectFrom(comment)
                .where(comment.refBoard.eq(refBoard)
                        .and(comment.refPostNo.eq(refPostNo))
                        .and(comment.commentNestLevel.eq(0)))
                .fetch();
    }
    @Override
    public List<Comment> findNestedCommentsByRefBoardAndRefPostNo(String refBoard, int refPostNo){
        QComment comment = QComment.comment;

        return queryFactory.selectFrom(comment)
                .where(comment.refBoard.eq(refBoard)
                        .and(comment.refPostNo.eq(refPostNo))
                        .and(comment.commentNestLevel.gt(0)))
                .fetch();
    }
    @Override
    public int updateCommentContent(int commentNo, String commentContent){
        QComment comment = QComment.comment;

        return (int) queryFactory.update(comment)
                .set(comment.commentContent, commentContent)
                .where(comment.commentNo.eq((long) commentNo))
                .execute(); //.execute(): QueryDSL을 사용하여 생성된 업데이트 쿼리를 실행하는 메서드
    }
    @Override
    public int countByRefBoardAndRefPostNo(String refBoard, int refPostNo){
        QComment comment = QComment.comment;

        return (int) queryFactory.selectFrom(comment)
                .where(comment.refBoard.eq(refBoard)
                        .and(comment.refPostNo.eq(refPostNo)))
                .fetchCount();
    }
    @Override
    public Map<String, String> findInquiryPostWriter(int refPostNo){
        QComment comment = QComment.comment;
        QUsers user = QUsers.users;

        return queryFactory.select(user.userId, user.name)
                .from(user)
                .join(comment).on(user.userId.eq(comment.userId.userId))
                .where(comment.refPostNo.eq(refPostNo))
                .fetch()
                .stream()
                .collect(Collectors.toMap(tuple -> tuple.get(user.userId), tuple -> tuple.get(user.name)));
    }
    @Override
    public Comment findParentComment(int commentNestedTo){
        QComment comment = QComment.comment;

        return queryFactory.selectFrom(comment)
                .where(comment.commentNo.eq((long) commentNestedTo))
                .fetchOne();
    }
}