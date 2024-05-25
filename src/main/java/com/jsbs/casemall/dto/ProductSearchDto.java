package com.jsbs.casemall.dto;

import com.jsbs.casemall.constant.ProductSellStatus;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ProductSearchDto {
    private String searchDateType; //: 날짜 검색
    private ProductSellStatus searchSellStatus; //: 판매 상태 검색
    private String searchBy; //: 키워드 검색
    private String searchQuery;  // : 쿼리 검색
}
