package com.jsbs.casemall.repository;

import com.jsbs.casemall.dto.CommentDto;
import com.jsbs.casemall.entity.Comment;

import java.util.List;
import java.util.Map;

public interface CommentRepositoryCustom {
    //: 커스텀 메서드를 선언하는 인터페이스
    List<Comment> findCommentsByRefBoardAndRefPostNo(String refBoard, int refPostNo);
    List<Comment> findNestedCommentsByRefBoardAndRefPostNo(String refBoard, int refPostNo);
    int updateCommentContent(int commentNo, String commentContent);
    int countByRefBoardAndRefPostNo(String refBoard, int refPostNo);
    Map<String, String> findInquiryPostWriter(int refPostNo); //: 작성자 아이디, 이름
    Comment findParentComment(int commentNestedTo);
}