package com.jsbs.casemall.entity;

import jakarta.persistence.*;
import lombok.ToString;

@Entity
@ToString
public class Answer extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;
    
    @Lob //: 255자가 넘는 String 타입일 경우 @Lob 어노테이션 추가
    private String contents;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;
    //기본 생성자
    public Answer(){

    }
    //생성자
    public Answer(Users user, Question question, String contents){
        this.user = user;
        this.question = question;
        this.contents = contents;
    }
}
