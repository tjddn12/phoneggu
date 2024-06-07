package com.jsbs.casemall.entity;

import com.jsbs.casemall.constant.ProductCategory;
import com.jsbs.casemall.constant.ProductSellStatus;
import com.jsbs.casemall.constant.ProductType;
import com.jsbs.casemall.dto.ProductFormDto;
import com.jsbs.casemall.exception.OutOfStockException;
import jakarta.persistence.*;
import jdk.jfr.Enabled;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@Entity
@Table(name = "product")
public class Product extends BaseEntity{

    @Id
    @Column(name = "pr_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id; //상품 번호

    @Column(name = "pr_name", nullable = false, length = 100)
    private String prName; //상품 이름


    @Lob
    @Column(name = "pr_detail", nullable = false, length = 100)
    private String prDetail; //상품 설명

    @Column(name = "pr_price", nullable = false)
    private int prPrice; //상품 가격

    @Column(name = "pr_stock", nullable = false)
    private int prStock; //상품 재고

//    @Max(value = 100, message = "최대 할인율은 100입니다")
//    private int discount; //할인율
//
//    private double discountPrice; //할인 가격

    @Enumerated(EnumType.STRING)
    @Column(name = "product_sell_status", nullable = false)
    private ProductSellStatus productSellStatus; //상품 판매 상태

    @Enumerated(EnumType.STRING)
    @Column(name = "product_category")
    private ProductCategory productCategory; //카테고리

    @Enumerated(EnumType.STRING)
    @Column(name = "product_type")
    private ProductType productType; //상품 종류

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ProductImg> productImgList = new ArrayList<>(); // 상품 이미지 리스트 추가


    public void updateProduct(ProductFormDto productFormDto) {
        this.prName = productFormDto.getPrName();
        this.prPrice = productFormDto.getPrPrice();
        this.prStock = productFormDto.getPrStock();
        this.prDetail = productFormDto.getPrDetail();
        this.productCategory = productFormDto.getProductCategory();
        this.productSellStatus = productFormDto.getProductSellStatus();
        this.productType = productFormDto.getProductType();
    }

    public void removeStock(int prStock){
        int restStock = this.prStock - prStock;
        if(restStock < 0){
            throw new OutOfStockException("상품의 재고가 부족합니다. " +
                    "(현재 재고 수량 : "+ this.prStock + ")" );
        }
        this.prStock = restStock;
    }
    //재고가 0보다 적으면 예외 발생
    // 재고 - 주문수량 = 재고
    public void addStock(int prStock){
        this.prStock += prStock;
    }
}