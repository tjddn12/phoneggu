package com.jsbs.casemall.dto;

import com.jsbs.casemall.entity.Article;
import com.jsbs.casemall.entity.Users;
import com.jsbs.casemall.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;

@ToString
@Getter
@Setter
public class ArticleForm {
    private Long id;
    private String title;
    private String content;
    private String userId; //: 질문 작성 유저 아이디
    private Integer commentCount;
    private LocalDateTime regTime;

    public ArticleForm(Long id, String title, String content, String userId, Integer commentCount, LocalDateTime regTime) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.userId = userId;
        this.commentCount = commentCount;
        this.regTime = regTime;
    }

    private UserRepository userRepository;

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Article toEntity() {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 아이디입니다."));
        return new Article(id, title, content, user, new ArrayList<>());
    }

    public Article toEntity(Users users) {
        return new Article(id, title, content, users, new ArrayList<>());
    }


}
