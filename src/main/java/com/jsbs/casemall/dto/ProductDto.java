package com.jsbs.casemall.dto;

import com.jsbs.casemall.constant.ProductSellStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ProductDto {
    private Long prId;
    private Long categoriesId;
    private String prName;
    private String prDetail;
    private Long prPrice;
    private int prStock;
    private Long discount;
    private Long discountPrice;
    private ProductSellStatus productSellStatus;
    private LocalDateTime pr_regDate;
    private LocalDateTime pr_update;
}
