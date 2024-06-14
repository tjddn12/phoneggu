package com.jsbs.casemall.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "review")
public class Review {
    //전반적인 타입 수정이 필요

    //ReviewDto에서 먼저 구현 후, Review(엔티티)에서 열 속성으로 제작
    //필드 -> GPT 참고
    //리뷰 번호, 리뷰 제목, 리뷰 내용, 평점,
    //리뷰 첨부 이미지, 리뷰 첨부 이미지 수, 주문 번호, (포인트 정보),
    //리뷰 조회수
    @Id
    @Column(name = "review_no")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewNo; //: 리뷰 번호
    //    @Column(name = "option_no", nullable = false)
//    private int optionNo; //: 옵션 번호, 필요시 추후 수정 가능.
    @ManyToOne
    @JoinColumn(name = "user_id", updatable = false)
    private Users userId; //: 사용자 엔티티
    @Column(name = "revw_title", nullable = false)
    private String revwTitle; //: 리뷰 제목
    @Column(name = "revw_content", nullable = false)
    private String revwContent; //: 리뷰 내용
    @Column(name = "revw_reg_date", nullable = false)
    private LocalDateTime revwRegDate; //: 리뷰 등록 날짜
    @Column(name = "revw_hits", nullable = false)
    private int revwHits; //: 조회수
    @Column(name = "revw_ratings", nullable = false)
    private int revwRatings; //: 평점
    //    @ManyToOne
//    @JoinColumn(name = "option_id")
//    private OptionDto option; //: 추후 필요시 수정.
    @ManyToOne
    @JoinColumn(name = "pr_name")
    private Product prName; //: 제품 엔티티
    //    @ManyToOne
//    @Column(name = "delivery_id")
//    private DeliveryDto delivery; //: 배송 엔티티, 추후 추가 가능.
//    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Attachment> attachmentDtoList; //: 첨부 파일 리스트, 이미지 첨부는 뷰에서 ProductImgDto로 같이 처리&삭제도 뷰에서 처리, 추후 수정 가능.
    //void 메서드는 명시
    //구체적인 메서드 -> ReviewRepository & ReviewService에서 구현
}