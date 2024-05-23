package com.jsbs.casemall.dto;

import com.jsbs.casemall.entity.ProductImg;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
public class ProductImgDto {
    private Long id;

    private String prImgName;

    private String prImgOriginName;

    private String imgUrl;

    private String prMainImg;
    private static ModelMapper modelMapper = new ModelMapper();

    public static ProductImgDto of(ProductImg prImg){
        return modelMapper.map(prImg, ProductImgDto.class);
    }
}