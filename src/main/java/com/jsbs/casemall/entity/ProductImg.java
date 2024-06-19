package com.jsbs.casemall.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "product_img")
public class ProductImg extends BaseEntity {

    @Id
    @Column(name = "pr_img_id")
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id; //상품 이미지 번호

    @Column(name = "pr_img_name")
    private String prImgName; //이미지 이름

    @Column(name = "pr_img_origin_name")
    private String prImgOriginName; //원본 이미지 이름

    @Column(name = "img_url", nullable = false)
    private String imgUrl; // 이미지 URL

    @Column(name = "pr_main_img")
    private String prMainImg; //대표 이미지 여부

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="pr_id")
    private Product product;

    public void updateProductImg(String prImgOriginName, String prImgName, String imgUrl) {
        this.prImgOriginName = prImgOriginName;
        this.prImgName = prImgName;
        this.imgUrl = imgUrl;
    }
}