package com.jsbs.casemall.dto;

import com.jsbs.casemall.entity.ReviewImg;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
public class ReviewImgDto {
    private Long id;
    private String reviewImgName;
    private String reviewImgOriginName;
    private String imgUrl;
    private String reviewMainImg;

    public static ReviewImgDto of (ReviewImg reviewImg){
        ModelMapper modelMapper = new ModelMapper();
        //ModelMapper: 객체간 필드 값을 자동으로 매핑해주는 라이브러리
        return modelMapper.map(reviewImg, ReviewImgDto.class);



    }
}
