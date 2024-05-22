package com.jsbs.casemall.dto;

import com.jsbs.casemall.entity.ProductImg;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
public class ProductImgDto {
    private Long prImgNo;

    private String imgName;

    private String oriImgName;

    private String imgUrl;

    private String repImgYn;
    private static ModelMapper modelMapper = new ModelMapper();

    public static ProductImgDto of(ProductImg prImg){
        return modelMapper.map(prImg, ProductImgDto.class);
    }
}
