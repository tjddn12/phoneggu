package com.jsbs.casemall.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "product_img")
public class ProductImg  {

    @Id
    @Column(name = "pr_img_no")
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long prImgNo; //상품 이미지 번호

    @Column(name = "pr_img_name")
    private String prImgName; //이미지 이름

    @Column(name = "pr_img_orgin_name")
    private String prImgOriginName; //원본 이미지 이름

    private String imgUrl; //이미지 주소

    @Column(name = "pr_main_img")
    private String prMainImg; //대표 이미지

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="pr_no")
    private Product product;

    public ProductImg(String prImgOrginName, String prImgName, String imgUrl) {
        this.prImgOriginName = prImgOrginName;
        this.prImgName = prImgName;
        this.imgUrl = imgUrl;
    }
}
