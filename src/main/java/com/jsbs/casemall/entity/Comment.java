package com.jsbs.casemall.entity;

import com.jsbs.casemall.dto.CommentDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

//QnA 게시판 답변 엔티티
@Entity
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL) // 해당 댓글 엔티티 여러개가 하나의 Article에 연관된다!!
    @JoinColumn(name = "article_id") // article 테이블의 id를 가져올떄 name
    private Article article; // 댓글의 부모 게시글

    @Column
    private String nickname; //: 유저 ID로 수정 필요

    @Column
    private String body;

    public static Comment createComment(CommentDto dto, Article article) {
        // 예외 발생
        if(dto.getId() != null) // 받아온 데이터에 id가 있다면
            throw new IllegalArgumentException("댓글 생성 실패! 댓글의 id가 없어야 합니다.");

        if(dto.getArticleId() != article.getId()) // 요청url의 id(articleId)와 요청데이터에 article_id가 다르면
            throw new IllegalArgumentException("댓글 생성 실패! 게시글의 id가 잘못되었습니다.");

        // 엔티티 생성 및 반환
        return new Comment(
                dto.getId(),
                article,
                dto.getNickname(),
                dto.getBody()
        );
    }

    public void patch(CommentDto dto) {
        //예외 발생
        if(this.id != dto.getId()){
            throw new IllegalArgumentException("댓글 수정 실패! 잘못된 id가 입력되었습니다.");
        }
        //객체를 갱신
        if(dto.getNickname() != null){
            this.nickname = dto.getNickname();
        }
        if(dto.getBody() != null) {
            this.body = dto.getBody();
        }

    }
}
