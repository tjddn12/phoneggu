package com.jsbs.casemall.entity;

import com.jsbs.casemall.dto.ReviewFormDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "review")
public class Review extends BaseEntity{
    //ReviewDto에서 먼저 구현 후, Review(엔티티)에서 열 속성으로 제작
    //필드 -> GPT 참고
    //리뷰 번호, 리뷰 제목, 리뷰 내용, 평점,
    //리뷰 첨부 이미지, 리뷰 첨부 이미지 수, 주문 번호, (포인트 정보),
    //리뷰 조회수
    @Id
    @Column(name = "review_no")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //: 리뷰 번호
    //    @Column(name = "option_no", nullable = false)
//    private int optionNo; //: 옵션 번호, 필요시 추후 수정 가능.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", updatable = false)
    private Users userId; //: 사용자 엔티티

    @Column(name = "revw_title", nullable = false)
    private String revwTitle; //: 리뷰 제목

    @Column(name = "revw_content", nullable = false)
    private String revwContent; //: 리뷰 내용

    @Column(name = "file_name")
    private String filename; //: 파일 이름
    
    @Column(name = "file_path")
    private String filepath; //: 파일 저장 경로
//    @OneToMany(mappedBy = "review", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
//    @OrderBy("id asc")
//    private List<ReviewImg> reviewImgs;
    @Column(name = "revw_hits", nullable = false)
    private int revwHits; //: 조회수

    @Column(name = "revw_ratings", nullable = false)
    private int revwRatings; //: 평점

    @ManyToOne
    @JoinColumn(name = "pr_id")
    private Product prId; //: 주문 정보(상품명, 이미지) 추출

    @OneToMany(mappedBy = "review", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<ReviewImg> reviewImgs;

    public void update(ReviewFormDto reviewFormDto){
        this.id = reviewFormDto.getId();
        this.revwTitle = reviewFormDto.getRevwTitle();
        this.revwContent = reviewFormDto.getRevwContent();
        this.revwRatings = reviewFormDto.getRevwRatings();
    }
}