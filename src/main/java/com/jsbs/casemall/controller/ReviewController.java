package com.jsbs.casemall.controller;

import com.jsbs.casemall.dto.ReviewDto;
import com.jsbs.casemall.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;;
import java.util.*;

//리뷰 게시판 페이지 매핑
@Slf4j
@RestController
@RequestMapping("/review")
public class ReviewController {
    private final ReviewService reviewService;
    private final ProductImgService productImgService;

    @Autowired
    public ReviewController(ReviewService reviewService, ProductImgService productImgService){
        this.reviewService = reviewService;
        this.productImgService = productImgService;
    }
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReviewDto>> getReviewsByUserId(@PathVariable String userId) {
        List<ReviewDto> reviews = reviewService.getReviewsByUserId(userId);

        return ResponseEntity.ok(reviews);
    }
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ReviewDto>> getReviewsByProductId(@PathVariable String productId){
        List<ReviewDto> reviews = reviewService.getReviewsByProductId(productId);

        return ResponseEntity.ok(reviews);
    }
    @PostMapping("/write")
    public ResponseEntity<ReviewDto> writeReview(@RequestBody ReviewDto reviewDto){
        ReviewDto createdReview = reviewService.writeReview(reviewDto);

        return ResponseEntity.ok(createdReview);
    }
    @PatchMapping("/{reviewNo}")
    public ResponseEntity<ReviewDto> editReview(@PathVariable Long reviewNo, @RequestBody ReviewDto reviewDto){
        ReviewDto updatedReview = reviewService.editReview(reviewNo, reviewDto);

        return ResponseEntity.ok(updatedReview);
    }
    @DeleteMapping("/{reviewNo}")
    public ResponseEntity<ReviewDto> deleteReview(@PathVariable Long reviewNo){
        reviewService.deleteReview(reviewNo);

        return ResponseEntity.noContent().build();
    }
    //리뷰 조회수 증가
    @GetMapping("/read")
    @ResponseBody
    public void readReviewDetails(@ModelAttribute("no") int reviewNo, Model model){
        int count = reviewService.incrementReviewViewCount(reviewNo);
//        if(count == 1){
//            log.info("리뷰 조회수 + 1");
//        }
//        model.addAttribute() //: 뷰페이지에 count 값 전달.
    }
}