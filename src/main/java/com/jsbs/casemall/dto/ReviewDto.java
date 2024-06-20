package com.jsbs.casemall.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReviewDto {
    private Long reviewNo;
    private String userId; //: 타입은 추후에 getter, setter로 소환시 수정., 엔티티에 대한 의존성을 줄이기 위해
    //Users의 식별자만을 사용
    private String revwTitle;
    private String revwContent;
    private LocalDateTime revwRegDate; //: 등록 시간
    //LocalDateTime: Date 타입에서 발전된 타입, 시간대가 제공됨.
//    private int revwHits; //: 조회수, JS로 처리 or 추후 추가 가능.
    private int revwRatings; //: 평점
    private String order; //: 주문
//    private OrderDetailDto orderDetail; //: 주문 상세 정보, 추후 추가 가능.
    //    private DeliveryDto delivery; //: 배송 정보, 추후 추가 가능.
} //: DTO에서는 의존성을 줄이기 위해 엔티티 객체 대신 해당 엔티티의 식별자만을 포함해야 함.,
//DTO <-> 엔티티 상호간 변환 메서드 추가 필요.