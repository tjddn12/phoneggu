package com.jsbs.casemall.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

//QnA 게시판 질문
@AllArgsConstructor
@ToString
@Entity
@Setter
@Getter
@NoArgsConstructor
public class Article extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column // 2.Column 어노테이션 추가
    private String title;
    @Column
    private String content;
    //유저 정보 추가 필요
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users user; //: 질문 작성 아이디

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    public void patch(Article article) {

        if(article.title != null) {
            this.title = article.title;
        }
        if(article.content != null) {
            this.content = article.content;
        }
    }
}