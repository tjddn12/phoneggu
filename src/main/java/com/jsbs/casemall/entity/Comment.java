package com.jsbs.casemall.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long commentNo;
    @Column(name = "member_id", nullable = false)
    private String memberId;
    @Column(name = "ref_board", nullable = false)
    private String refBoard;
    @Column(name = "ref_post_no", nullable = false)
    private int refPostNo;
    @Column(name = "comment_content", nullable = false)
    private String commentContent;
    @Column(name = "comment_reg_date", nullable = false)
    private LocalDateTime commentRegDate;
    @Column(name = "comment_chg_date", nullable = false)
    private LocalDateTime commentChgDate;
    @Column(name = "comment_del_date", nullable = false)
    private LocalDateTime commentDelDate;
    @Column(name = "comment_nest_level", nullable = false)
    private int commentNestLevel;
    @Column(name = "comment_nested_to")
    private Integer commentNestedTo; //: 상단 댓글 정보(NULL 허용)
    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users userId;
}