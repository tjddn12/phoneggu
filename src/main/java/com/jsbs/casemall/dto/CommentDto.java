package com.jsbs.casemall.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentDto {
    private int commentNo;
    private String memberId;
    private String refBoard; //: 게시판 구분
    private int refPostNo; //: 게시글 번호
    private String commentContent;
    private LocalDateTime commentRegDate;
    private LocalDateTime commentChgDate;
    private LocalDateTime commentDelDate;
    private int commentNestLevel; //: 댓글 계층 구분
    private Integer commentNestedTo; //: 상단 댓글 정보(NULL 허용)
    private UserDto userId;
}