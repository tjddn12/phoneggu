package com.jsbs.casemall.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jsbs.casemall.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class CommentDto {
    private Long id;
    @JsonProperty("article_id") //: JSON 데이터와 Java 객체 간 매핑을 돕는 Jackson 라이브러리 어노테이션
    private Long articleId;
    private String nickname;
    private String body;

    public static CommentDto createCommentDto(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getArticle().getId(), //: Article을 가져오고 거기서 Id만 필요하기 때문에 다시 .getId()
                comment.getNickname(),
                comment.getBody()
        );
    }
}
