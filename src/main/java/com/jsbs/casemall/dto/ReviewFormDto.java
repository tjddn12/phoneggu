package com.jsbs.casemall.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewFormDto {
    private Long reviewNo;
    private String revwTitle;
    private String revwContent;
    private Integer revwRatings;
}