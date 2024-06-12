package com.jsbs.casemall.controller;

import com.jsbs.casemall.dto.ReviewFormDto;
import com.jsbs.casemall.entity.Product;
import com.jsbs.casemall.entity.Review;
import com.jsbs.casemall.entity.Users;
import com.jsbs.casemall.repository.ProductRepository;
import com.jsbs.casemall.repository.UserRepository;
import com.jsbs.casemall.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.*;
//리뷰 게시판 페이지 매핑
@Slf4j
@RestController //: JSON 형식의 데이터 전송
@RequestMapping("/reviews")
public class ReviewController {
    private final ReviewService reviewService;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    //    private final ProductImgService productImgService; //: 이미지는 뷰에서 JS로 처리.
    @Autowired
    public ReviewController(ReviewService reviewService, UserRepository userRepository, ProductRepository productRepository){
        this.reviewService = reviewService;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }
    @GetMapping
    public String getAllReviews(Model model){
        List<Review> reviews = reviewService.getAllReviews();

        model.addAttribute("reviews", reviews);

        return "reviews";
    }
    @GetMapping("/reviewWrite")
    public String createReviewForm(Model model){
        model.addAttribute("review", new Review());

        return "reviewWrite";
    }
    @PostMapping
    public String createReview(@ModelAttribute Review review){
        reviewService.saveReview(review);

        return "redirect:/reviews";
    }
    @GetMapping("/{reviewNo}")
    public String getReviewByNo(@PathVariable Long reviewNo, Model model){
        model.addAttribute("review", reviewService.getReviewByNo(reviewNo).orElse(null));

        return "reviewDetail";
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

        return "reviews";
    }
    @GetMapping("/product/{prName}")
    public String getReviewsByPrName(@PathVariable String prName, Model model){
        List<Product> prList = productRepository.findByPrName(prName);

        model.addAttribute("reviews", reviewService.getReviewsByPrName(prList));

        return ;
    }


    @PutMapping("/{reviewNo}")
    public ResponseEntity<Review> updateReview(@PathVariable Long reviewNo, @RequestBody ReviewFormDto reviewFormDto){
        reviewFormDto.setReviewNo(reviewNo);

        Review updatedReview = reviewService.updateAReview(reviewFormDto);

        if(updatedReview != null){
            return ResponseEntity.ok(updatedReview); //: HTTP 상태코드 200 OK와 함께 updatedReview 객체를
            //응답 본문으로 포함하는 ResponseEntity 객체 생성.
        }else{
            return ResponseEntity.notFound().build(); //: HTTP 상태코드 400 Not Found와 함께 빈 응답 본문을
            //가진 ResponseEntity 객체 생성.
        }
    }
    @DeleteMapping("/{reviewNo}")
    public void deleteReview(@PathVariable Long reviewNo){
        reviewService.deleteReview(reviewNo);
    }
}