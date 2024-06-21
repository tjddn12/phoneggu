package com.jsbs.casemall.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReviewsDto {
    private String productName;
    private String userid;
    private int revwRatings;
    private long reviewNo;
    private String revwContent;
    private String revwTitle;
    private LocalDateTime regTime;



}
