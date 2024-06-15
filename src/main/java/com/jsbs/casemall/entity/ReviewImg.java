package com.jsbs.casemall.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ReviewImg {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;
    @Lob //: 해당 엔티티 속성이 큰 객체 유형을 나타내는 것을 지정하는 JPA에서 사용되는 어노테이션
    private byte[] data;
    private String reviewImgName;
    private String reviewImgOriginName;
    private String imgUrl;
    private String reviewMainImg;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_no")
    private Review review;
}
