package com.jsbs.casemall.entity;

import com.jsbs.casemall.constant.ProductSell;
import com.jsbs.casemall.dto.ProductFormDto;
import com.jsbs.casemall.exception.OutOfStockException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(name = "product")
public class Product extends BaseEntity{

    @Id
    @Column(name = "pr_no")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id; //상품 번호

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
    private ProductSell prSell; //상품 판매 상태

    public void updateProduct(ProductFormDto productFormDto) {
        this.prName = productFormDto.getPrName();
        this.prPrice = productFormDto.getPrPrice();
        this.prStock = productFormDto.getPrStock();
        this.prDetail = productFormDto.getPrDetail();
        this.prSell = productFormDto.getPrSell();
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