package com.jsbs.casemall.dto;

import com.jsbs.casemall.entity.Article;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@AllArgsConstructor
@ToString
@Getter
@Setter
public class ArticleForm {
    private Long id;
    private String title;
    private String content;
    private Integer commentCount;
    private LocalDateTime regTime;

    public Article toEntity() {
        return new Article(id, title, content);
    }
}
