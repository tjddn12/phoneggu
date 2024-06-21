package com.jsbs.casemall.dto;

import com.jsbs.casemall.entity.Review;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class ReviewFormDto {

    private String imgUrl;

    private String productName;

    private Long id;

    private Long productId;

    @NotBlank(message = "제목은 필수 입력 값입니다.")
    private String revwTitle;

    @NotBlank(message = "내용은 필수 입력 값입니다.")
    private String revwContent;

    @NotNull(message = "평점은 필수 입력 값입니다.")
    private Integer revwRatings;

    private List<ReviewImgDto> reviewImgDtoList = new ArrayList<>();

    private List<Long> reviewImgIds =  new ArrayList<>();

    private static ModelMapper modelMapper = new ModelMapper();

    public Review createReview(){
        return modelMapper.map(this, Review.class);
    } //: ReviewFormDto => Review로 변환
    public static ReviewFormDto of (Review review){
        return modelMapper.map(review, ReviewFormDto.class);
    } //: Review => ReviewFormDto로 변환
}