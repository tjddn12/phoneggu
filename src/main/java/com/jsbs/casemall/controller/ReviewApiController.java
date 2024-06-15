package com.jsbs.casemall.controller;

import com.jsbs.casemall.dto.ReviewDto;
import com.jsbs.casemall.entity.Review;
import com.jsbs.casemall.entity.ReviewImg;
import com.jsbs.casemall.repository.ReviewRepository;
import com.jsbs.casemall.service.ReviewImgService;
import com.jsbs.casemall.service.ReviewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

//JSON 데이터 전송
@Slf4j
@RestController
@RequestMapping("/reviews")
public class ReviewApiController {
    private final ReviewService reviewService;
    private final ReviewRepository reviewRepository;
    private final ReviewImgService reviewImgService;

    @Autowired
    public ReviewApiController(ReviewService reviewService, ReviewRepository reviewRepository, ReviewImgService reviewImgService) {
        this.reviewService = reviewService;
        this.reviewRepository = reviewRepository;
        this.reviewImgService = reviewImgService;
    }
    @PostMapping("/submitRating")
    public ResponseEntity<String> submitRating(@RequestBody ReviewDto reviewDto){
        //평점을 reviewService를 통해 저장
        reviewService.saveRating(reviewDto);

        return ResponseEntity.ok("평점이 성공적으로 저장되었습니다.");
    }
    @PostMapping
    public Review createReview(@RequestBody Review review){
        review.setRevwRegDate(LocalDateTime.now()); //: 리뷰 등록 날짜 설정

        return reviewRepository.save(review);
    }
    @PostMapping("/add")
    public ResponseEntity<String> addReview(@RequestParam("review") String reviewContent,
                                            @RequestParam("files") List<MultipartFile> files){
        Review review = new Review();

        review.setRevwContent(reviewContent);

        List<ReviewImg> reviewImgs = new ArrayList<>();

        for(MultipartFile file : files){
            ReviewImg reviewImg = new ReviewImg();
            //파일 저장 로직 추가
            reviewImg.setFileName(file.getOriginalFilename());
            reviewImgs.add(reviewImg);
        }

        reviewService.saveReviewWithImages(review, reviewImgs);

        return ResponseEntity.ok("Review added successfully");
    }
}