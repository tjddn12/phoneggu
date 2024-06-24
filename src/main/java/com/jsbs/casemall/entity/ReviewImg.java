package com.jsbs.casemall.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "review_img")
public class ReviewImg extends BaseEntity {
    @Id
    @Column(name = "review_img_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id; //: 리뷰 이미지 번호

    private String imgName; //: 이미지 파일명
    private String oriImgName; //: 원본 이미지 파일명
    private String imgUrl; //: 이미지 조회 경로
//    private String repimgYn; //: 대표 이미지 여부

    @ManyToOne(fetch = FetchType.LAZY) //: 연관 엔티티를 실제 사용할 때만 로드
    @JoinColumn(name = "review_no")
    private Review review;

    public void updateReviewImg(String oriImgName, String imgName, String imgUrl){
        this.oriImgName = oriImgName;
        this.imgName = imgName;
        this.imgUrl = imgUrl;
    }
}