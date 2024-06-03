package com.jsbs.casemall.dto;

import com.jsbs.casemall.constant.ProductSellStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ProductDto {
    private Long prNo;
    private int categoriesNo;
    private String prName;
    private String prDetail;
    private int prPrice;
    private int prStock;
    private double discount;
    private ProductSellStatus productsSell;
    private LocalDateTime pr_regDate;
    private LocalDateTime pr_update;
}
