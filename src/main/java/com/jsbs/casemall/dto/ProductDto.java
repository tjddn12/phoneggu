package com.jsbs.casemall.dto;

import com.jsbs.casemall.constant.ProductCategory;
import com.jsbs.casemall.constant.ProductSellStatus;
import com.jsbs.casemall.constant.ProductType;
import com.jsbs.casemall.entity.Product;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import javax.lang.model.type.PrimitiveType;
import java.time.LocalDateTime;

@Getter
@Setter
public class ProductDto {
    private Long prId;
    private String prName;
    private String prDetail;
    private int prPrice;
    //    private int discount;
//    private double discountPrice;
    private ProductType productType;
    private ProductCategory productCategory;
    private ProductSellStatus productSellStatus;
    private LocalDateTime regTime;
    private LocalDateTime updateTime;

    private static ModelMapper modelMapper = new ModelMapper();

    public static ProductDto of(Product product) {
        return modelMapper.map(product, ProductDto.class);
    }
}
