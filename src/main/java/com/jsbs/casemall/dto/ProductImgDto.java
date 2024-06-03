package com.jsbs.casemall.dto;

import com.jsbs.casemall.entity.ProductImg;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
public class ProductImgDto {
    private Long id;

    private String prImgName; //이미지 이름

    private String prImgOriginName; //원본 이미지 이름

    private String imgUrl;

    private String prMainImg; //대표 이미지 여부
    private static ModelMapper modelMapper = new ModelMapper();

    public static ProductImgDto of(ProductImg productImg){
        return modelMapper.map(productImg, ProductImgDto.class);
    }
    //ProductImg 엔티티를 ProductImgDto로 매핑
}
