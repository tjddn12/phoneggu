package com.jsbs.casemall.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class OrderDto {


    // 주문 정보 Dto
    // 주문을 할 아이템들
    // 가격,개수,상품명=>찾기

    private Long orderNo; // 주문 번호

    private String orderId; // 주문 아이디 (uuid로 생성된 )

    private List<OrderItemDto> items;
    private int totalPrice;    // 총  결제 금액
    private int count; // 개수

    // form 에서 받을 데이터

    // 주문 고객 정보를 담을것
    private String userName; // 주문한 고객이름

    private String email; // 이메일
    private String domain;

    private String phone; // 전화번호
 // 우편번호 기본주소 나머지주소
    private String pCode; // 우편번호
    private String loadAddress; // 도로
    private String lotAddress; //  지번
    private String detailAddress; // 상세

    private String tel1;
    private String tel2;
    private String tel3;

    public void tranceOther(String phone, String email){ // 변환
        this.tel1 = phone.substring(0,3); // 01036272884
        this.tel2 = phone.substring(3,7);
        this.tel3 = phone.substring(7);
        this.email =email.split("@")[0];
        this.domain = email.split("@")[1];
    }




    // 결제 성공시 넘길 내용
    private String payInfo; // 결제 수단




}
