package com.jsbs.casemall.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

//QnA 게시판 질문 엔티티
@Entity
@Getter
@Setter
public class Question extends BaseEntity {
    @Id
    @GeneratedValue
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    private String title;
    private String contents;

    @OneToMany(mappedBy = "question")
    @OrderBy("id ASC") //: 오름차순 정렬
    private List<Answer> answers;

    public Question(){

    }
    public Question(Users user, String title, String contents){
        this.user= user;
        this.title = title;
        this.contents = contents;
    }
    public void update(String title, String contents){
        this.title = title;
        this.contents = contents;
    }
    //시간 포맷 변경(yyyy.MM.dd HH:mm:ss): 추후 HTML에서 추가하면 충분.
}
