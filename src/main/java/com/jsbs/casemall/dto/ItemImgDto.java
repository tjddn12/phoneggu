package com.jsbs.casemall.dto;

import com.jsbs.casemall.entity.ProductImg;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
public class ItemImgDto {
    private Long id;

    private String prImgName;

    private String prImgOriginName;

    private String imgUrl;

    private String prMainImg;
    private static ModelMapper modelMapper = new ModelMapper();

    public static ItemImgDto of(ProductImg prImg){
        return modelMapper.map(prImg, ItemImgDto.class);
    }
}