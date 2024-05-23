package com.jsbs.casemall.entity;

import com.jsbs.casemall.constant.ProductSell;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Entity
@Table(name = "product")
public class Product {

    @Id
    @Column(name = "pr_no")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long prNo; //상품 번호

    @Column(name = "categories_no")
    private int categoriesNo; //카테고리 번호

    @Column(name = "pr_name", nullable = false, length = 100)
    private String prName; //상품 이름

    @Lob
    @Column(name = "pr_detail", nullable = false, length = 100)
    private String prDetail; //상품 설명

    @Column(name = "pr_price", nullable = false)
    private int prPrice; //상품 가격

    @Column(name = "pr_stock", nullable = false)
    private int prStock; //상품 재고

    private double discount; //할인율

    @Column(name = "pr_sell")
    private ProductSell productsSell; //상품 판매 상태

    @Column(name = "pr_regDate")
    private LocalDateTime prRegDate; //상품 등록 시간

    @Column(name = "pr_update")
    private LocalDateTime prUpdate; //상품 수정 시간

}
