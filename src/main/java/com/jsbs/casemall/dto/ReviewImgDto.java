package com.jsbs.casemall.dto;

import com.jsbs.casemall.entity.ReviewImg;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
public class ReviewImgDto {
    private Long id;
    private String revwImgName;
    private String revwOriImgName;
    private String imgUrl;
//    private String repImgYn; //: 대표 이미지 여부, 추후 필요시 추가
    private static ModelMapper modelMapper = new ModelMapper();
    //ModelMapper: 객체간 필드 값을 자동으로 매핑해주는 라이브러리
    public static ReviewImgDto of (ReviewImg reviewImg){
        return modelMapper.map(reviewImg, ReviewImgDto.class);
    } //: ReviewImg 엔티티를 ReviewImgDto로 매핑
}
