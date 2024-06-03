package com.jsbs.casemall.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MainProductDto {
    private Long id;

    private String prName;

    private String prDetail;

    private String imgUrl;

    private Integer prPrice;

//    private Integer discount; //할인율
//
//    private double discountPrice; //할인 가격

    @QueryProjection
    public MainProductDto(Long id, String prName, String prDetail, String imgUrl, Integer prPrice) {
        this.id = id;
        this.prName = prName;
        this.prDetail = prDetail;
        this.imgUrl = imgUrl;
        this.prPrice = prPrice;
    }
    // JPA 에서 쿼리결과를 이 클래스의 생성자 매개변수에 매핑
    //쿼리 결과를 통해 생성된 레코드의 값을 이 클래스의 필드에 자동으로 할당합니다.
}
