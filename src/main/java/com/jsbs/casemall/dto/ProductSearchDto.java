package com.jsbs.casemall.dto;

import com.jsbs.casemall.constant.ProductCategory;
import com.jsbs.casemall.constant.ProductSellStatus;
import com.jsbs.casemall.constant.ProductType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductSearchDto {
    private String searchDateType; // 현재시간과 상품등록일을 비교하여 조회

    private ProductSellStatus searchSellStatus; // 판매 상태 기준

    private ProductCategory searchCategory; //카테고리 종류

    private ProductType searchPrType; //상품 종류

    private String searchBy; // 상품 조회시 유형 prName:상품명

    private String searchQuery = ""; // 조회할 검색어를 저장할 변수
}
