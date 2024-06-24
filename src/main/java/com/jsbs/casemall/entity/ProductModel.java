package com.jsbs.casemall.entity;

import com.jsbs.casemall.constant.ProductModelSelect;
import com.jsbs.casemall.exception.OutOfStockException;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@Table(name = "product_model")
public class ProductModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "model_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_model_select")
    private ProductModelSelect productModelSelect = ProductModelSelect.DEFAULT_MODEL; // 기본값 설정

    @Column(name = "pr_stock")
    private Integer prStock=0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pr_id")
    private Product product;

    public void removeStock(int prStock){
        int restStock = this.prStock - prStock;
        if(restStock < 0){
            throw new OutOfStockException("상품의 재고가 부족합니다. " +
                    "(현재 재고 수량 : "+ this.prStock + ")" );
        }
        this.prStock = restStock;
    }

    public void addStock(int prStock){
        this.prStock += prStock;
    }
}
