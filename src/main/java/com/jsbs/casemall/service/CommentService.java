package com.jsbs.casemall.service;

import com.jsbs.casemall.dto.CommentDto;

import java.util.List;
import java.util.Map;

public interface CommentService {
    List<CommentDto> getCommentsInPost(String refBoard, int refPostNo);
    List<CommentDto> getNestedCommentsInPost(String refBoard, int refPostNo);
    int writeAComment(CommentDto commentDto);
    int editAComment(int commentNo, String commentContent);
    int deleteAComment(int commentNo);
    int getTotalCommentNumber(String refBoard, int refPostNo);
    Map<String, String> checkInquiryPostWriter(int refPostNo);
    CommentDto checkParentComment(int commentNestedTo);
}