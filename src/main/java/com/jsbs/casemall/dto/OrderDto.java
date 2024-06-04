package com.jsbs.casemall.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class OrderDto {

    // 주문 정보 Dto

    // 가격,개수,상품명=>찾기
    private int amount; // 개수

    private int prPrice; // 상품 가격

    private String orderID;

    private List<String> orderName; // 상품이름

    private String userName; // 주문한 고객이름

    private String email; // 이메일

    private String phone; // 전화번호






}
