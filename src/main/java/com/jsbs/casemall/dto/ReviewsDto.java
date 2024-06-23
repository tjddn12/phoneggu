package com.jsbs.casemall.dto;

import com.jsbs.casemall.entity.Review;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReviewsDto {
    private String productName;
    private String userid;
    private int revwRatings;
    private Long reviewNo;
    private String revwContent;
    private String revwTitle;
    private LocalDateTime regTime;

    public ReviewsDto(){}

    // Review 엔티티를 ReviewsDto로 변환하는 생성자
    public ReviewsDto(Review review) {
        this.productName = review.getPrId().getPrName();
        this.userid = review.getUserId().getUsername();
        this.revwRatings = review.getRevwRatings();
        this.reviewNo = review.getId();
        this.revwContent = review.getRevwContent();
        this.revwTitle = review.getRevwTitle();
        this.regTime = review.getRegTime();
    }
}
