package com.jsbs.casemall.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDto {

    // 주문 정보 Dto

    // 가격,개수,상품명=>찾기
    private int amount; // 개수

    private Long orderID;

    private String orderName; // 상

    private String userName; // 주문한 고객이름

    private String email; // 이메일

    private String phone; // 전화번호




}
