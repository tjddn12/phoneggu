package com.jsbs.casemall.entity;

import com.jsbs.casemall.constant.ProductSellStatus;
import com.jsbs.casemall.dto.ProductFormDto;
import com.jsbs.casemall.exception.OutOfStockException;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
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
    @Column(name = "pr_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id; //상품 번호

    @Column(name = "categories_id")
    private Long categoriesId; //카테고리 번호

    @Column(name = "pr_name", nullable = false, length = 100)
    private String prName; //상품 이름

    @Lob
    @Column(name = "pr_detail", nullable = false, length = 100)
    private String prDetail; //상품 설명

    @Column(name = "pr_price", nullable = false)
    private Long prPrice; //상품 가격

    @Column(name = "pr_stock", nullable = false)
    private int prStock; //상품 재고

    @Max(value = 100, message = "최대 할인율은 100입니다")
    private Long discount; //할인율

    private Long discountPrice; //할인 가격

    @Column(name = "pr_sellStatus")
    private ProductSellStatus productSellStatus; //상품 판매 상태

    public void updateProduct(ProductFormDto productFormDto) {
        this.prName = productFormDto.getPrName();
        this.prPrice = productFormDto.getPrPrice();
        this.prStock = productFormDto.getPrStock();
        this.prDetail = productFormDto.getPrDetail();
        this.categoriesId = productFormDto.getCategoriesId();
        this.productSellStatus = productFormDto.getProductSellStatus();
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