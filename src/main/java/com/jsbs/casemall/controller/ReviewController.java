package com.jsbs.casemall.controller;

import com.jsbs.casemall.dto.ReviewDto;
import com.jsbs.casemall.dto.ReviewFormDto;
import com.jsbs.casemall.entity.Product;
import com.jsbs.casemall.entity.Review;
import com.jsbs.casemall.entity.Users;
import com.jsbs.casemall.repository.ReviewRepository;
import com.jsbs.casemall.repository.UserRepository;
import com.jsbs.casemall.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.*;
//리뷰 게시판 페이지 매핑
@Slf4j
@Controller
//@RestController //: JSON 형식의 데이터 전송, 추후 필요시 수정.
@RequestMapping("/reviews")
public class ReviewController {
    private final ReviewService reviewService;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    //    private final ProductImgService productImgService; //: 이미지는 뷰에서 JS로 처리.
    @Autowired
    public ReviewController(ReviewService reviewService, UserRepository userRepository, ReviewRepository reviewRepository){
        this.reviewService = reviewService;
        this.userRepository = userRepository;
        this.reviewRepository = reviewRepository;
    }
    @GetMapping
    public String getAllReviews(Model model){
        List<Review> reviews = reviewService.getAllReviews();

        model.addAttribute("reviews", reviews);

        return "review/reviews";
    }
    @GetMapping("/reviewWrite")
    public String createReviewForm(Model model){
        //주문 내역에서 리뷰 작성시, 상품 이름&상품 사진을 받아오는 로직 구현
        model.addAttribute("review", new Review());

        return "review/reviewWrite";
    }
    @PostMapping
    public String createReview(@ModelAttribute Review review){
        reviewService.saveReview(review);

        return "redirect:/reviews";
    }
    @GetMapping("/{reviewNo}")
    public String getReviewByNo(@PathVariable Long reviewNo, Model model){
        model.addAttribute("review", reviewService.getReviewByNo(reviewNo).orElse(null));

        return "review/reviewDetail";
    }
    @GetMapping("/user/{userId}")
    public String getReviewsByUserId(@PathVariable String userId, Model model){
        Optional<Users> userOpt = userRepository.findByUserId(userId);

        if(userOpt.isPresent()){
            Users user = userOpt.get();

            model.addAttribute("reviews", reviewService.getReviewsByUserId(user));
        }else{
            model.addAttribute("reviews", List.of());
            model.addAttribute("error", "해당 사용자 ID를 찾을 수 없습니다.");
        }

        return "review/reviews";
    }
    @GetMapping("/product/{prName}")
    public String getReviewsByPrName(@PathVariable String prName, Model model){
        Product product = new Product();

        product.setPrName(prName);

        List<Review> reviews = reviewRepository.findReviewsByPrName(product);

        model.addAttribute("reviews", reviews);

        return "review/reviews";
    }
    @PutMapping("/{reviewNo}")
    public String updateReview(@PathVariable Long reviewNo, @ModelAttribute ReviewFormDto reviewFormDto){
        reviewFormDto.setReviewNo(reviewNo);
        reviewService.updateAReview(reviewFormDto);

        return "redirect:/reviews";
    }
    @DeleteMapping("/{reviewNo}")
    public String deleteReview(@PathVariable Long reviewNo){
        reviewService.deleteReview(reviewNo);

        return "redirect:/reviews";
    }
    @PostMapping("/submitRating")
    public ResponseEntity<String> submitRating(@RequestBody ReviewDto reviewDto){
        //평점을 reviewService를 통해 저장
        reviewService.saveRating(reviewDto);

        return ResponseEntity.ok("평점이 성공적으로 저장되었습니다.");
    }
}