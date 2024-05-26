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
    private Integer price;

    @QueryProjection //: QueryDSL 라이브러리에서 사용, JPA와 비슷한 방식으로 쿼리 작성 보조,
    //문자열 기반 쿼리 대신 Java 코드로 쿼리 작성, 컴파일 시점에 타입 안정성 보장.
    public MainProductDto(Long id, String prName, String prDetail, String imgUrl,
                       int price){
        this.id = id;
        this.prName = prName;
        this.prDetail = prDetail;
        this.imgUrl = imgUrl;
        this.price = price;
    } //: JPA에서 쿼리 결과를 이 클래스 생성자 매개변수에 매핑,
    //쿼리 결과를 통해 생성된 레코드의 값을 이 클래스의 필드에 자동으로 할당
}