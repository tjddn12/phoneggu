package com.jsbs.casemall.dto;

import lombok.Data;
import org.springframework.web.util.UriComponentsBuilder;
//상품 페이지 페이징 처리 dto
@Data
public class ProductCriteria {
    private int page;
    private int products; //: 페이지당 상품 수
    private String section; //: 머지 충돌시, 변수명 수정 예정.
    private String category; //: ", 변수명 수정 예정.
    private int min; //: 상품 최저가
    private int max; //: 상품 최고가
    private String sortBy; //: 상품 분류

    public ProductCriteria(){
        this(1, 8); //: 한 페이지에 상품 8개 진열
    }
    //    --------------------
    public ProductCriteria(int page, int products){
        this.page = page;
        this.products = products;
    }
    public String getListLink(){
        UriComponentsBuilder builder = UriComponentsBuilder.fromPath("")
                .queryParam("page", page)
                .queryParam("items", products)
                .queryParam("section", section)
                .queryParam("category", this.category) //: 선택적으로 카테고리에 접근
                .queryParam("min", this.min)
                .queryParam("max", this.max)
                .queryParam("sortBy", this.sortBy);

        return builder.toUriString();
    }
}