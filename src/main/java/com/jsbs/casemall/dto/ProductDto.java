package com.jsbs.casemall.dto;

import com.jsbs.casemall.constant.ProductCategory;
import com.jsbs.casemall.constant.ProductSellStatus;
import lombok.Getter;
import lombok.Setter;

import javax.lang.model.type.PrimitiveType;
import java.time.LocalDateTime;

@Getter
@Setter
public class ProductDto {
    private Long prId;
    private String prName;
    private String prDetail;
    private int prPrice;
    private int prStock;
    //    private int discount;
//    private double discountPrice;
    private PrimitiveType primitiveType;
    private ProductCategory productCategory;
    private ProductSellStatus productSellStatus;
    private LocalDateTime regTime;
    private LocalDateTime updateTime;
}
