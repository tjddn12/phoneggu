package com.jsbs.casemall.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
public class ReviewDto {
    private int reviewNo;
    //    private int optionNo; //: 필요시 추후 수정 가능.
    private int orderNo;
    private String userId; //: 타입은 추후에 getter, setter로 소환시 수정.
    private String revwTitle;
    private String revwContent;
    private LocalDateTime revwRegDate;
    //LocalDateTime: Date 타입에서 발전된 타입, 시간대가 제공됨.
    private int revwHits; //: 조회수
    private int revwRatings; //: 평점
    private String product;
//    private OrderDetailDto orderDetail; //: 주문 상세 정보, 추후 추가 가능.
    //    private DeliveryDto delivery; //: 배송 정보, 추후 추가 가능.
}