package com.jsbs.casemall.controller;

import com.jsbs.casemall.dto.Criteria;
import com.jsbs.casemall.dto.PageDto;
import com.jsbs.casemall.dto.ReviewFormDto;
import com.jsbs.casemall.entity.Product;
import com.jsbs.casemall.entity.Review;
import com.jsbs.casemall.entity.Users;
import com.jsbs.casemall.repository.ReviewRepository;
import com.jsbs.casemall.repository.UserRepository;
import com.jsbs.casemall.service.*;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

//리뷰 게시판 뷰페이지 매핑
@Slf4j
@Controller
@RequestMapping("/reviews")
public class ReviewController {
    private final ReviewService reviewService;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewImgService reviewImgService;

    //    private final ProductImgService productImgService; //: 이미지는 뷰에서 JS로 처리.
    @Autowired
    public ReviewController(ReviewService reviewService, UserRepository userRepository, ReviewRepository reviewRepository, ReviewImgService reviewImgService){
        this.reviewService = reviewService;
        this.userRepository = userRepository;
        this.reviewRepository = reviewRepository;
        this.reviewImgService = reviewImgService;
    }
    @GetMapping(value = "/reviewWrite")
    public String reviewForm(Model model){
        model.addAttribute("reviewFormDto", new ReviewFormDto());

        return "review/reviewWrite";
    }
    @PostMapping("/reviewWrite")
    public String reviewNew(@Valid ReviewFormDto reviewFormDto, BindingResult bindingResult, Model model,
    @RequestParam("reviewImgFile") List<MultipartFile> reviewImgFileList){
        if(bindingResult.hasErrors()){
            return "review/reviewWrite";
        }
        if(reviewImgFileList.get(0).isEmpty() && reviewFormDto.getId() == null){
            model.addAttribute("errorMessage", "리뷰 이미지는 필수 입력값입니다.");

            return "review/reviewWrite";
        }

        try{
            reviewService.saveReview(reviewFormDto, reviewImgFileList);
        }catch(Exception e){
            model.addAttribute("errorMessage", "리뷰 등록 중 에러가 발생하였습니다.");

            return "review/reviewWrite";
        }

        return "redirect:/reviews";
    }
    @PostMapping(value = "/{reviewNo}")
    public String reviewUpdate(@Valid ReviewFormDto reviewFormDto, BindingResult bindingResult,
                               Model model, @RequestParam("reviewImgFile") List<MultipartFile> reviewImgFileList){
        if(bindingResult.hasErrors()){
            return "review/reviewWrite";
        }
        if(reviewImgFileList.get(0).isEmpty() && reviewFormDto.getId() == null){
            model.addAttribute("errorMessage", "리뷰 이미지는 필수 입력값입니다.");

            return "review/reviewWrite";
        }

        try{
            reviewService.updateAReview(reviewFormDto, reviewImgFileList);
        }catch(Exception e){
            model.addAttribute("errorMessage", "리뷰 수정 중 에러가 발생하였습니다.");

            return "review/reviewWrite";
        }

        return "redirect:/reviews";
    }
    //리뷰 게시판 페이징 처리 및 매개변수 전달
    @GetMapping
    public String getReviews(Criteria criteria, Model model){
        List<Review> reviews = reviewService.getAllReviews();
        Map<Long, String> reviewImages = new HashMap<>();
        PageDto<Review> pageDto = reviewService.getReviewList(criteria);
        List<Integer> pageNumbers = IntStream.rangeClosed(1, pageDto.getTotalRecordCount())
                .boxed()
                .collect(Collectors.toList());
        //페이징 처리
        model.addAttribute("pageDto", pageDto);
        model.addAttribute("pageNumbers", pageNumbers);

        for(Review review : reviews){
            String imageUrl = reviewService.getImgUrlByReviewNo(review.getReviewNo());

            reviewImages.put(review.getReviewNo(), imageUrl);
        }

        model.addAttribute("reviews", reviews);
        model.addAttribute("reviewImages", reviewImages);

        return "review/reviews";
    }
//    @PostMapping
//    public String createReview(@ModelAttribute Review review){
//        reviewService.saveReview(review);
//
//        return "redirect:/reviews";
//    }
    @GetMapping("/{reviewNo}")
    public String getReviewDetail(@PathVariable Long reviewNo, Model model){
        List<Review> reviews = reviewService.getAllReviews();
        Review review = reviewService.getReviewByNo(reviewNo).orElse(null);

        Map<Long, String> reviewImages = new HashMap<>();
        for(Review revw : reviews){
            String imageUrl = reviewService.getImgUrlByReviewNo(revw.getReviewNo());

            reviewImages.put(review.getReviewNo(), imageUrl);
        }

        model.addAttribute("review", review);
        model.addAttribute("reviewImages", reviewImages);

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
    @DeleteMapping("/{reviewNo}")
    public String deleteReview(@PathVariable Long reviewNo){
        reviewService.deleteReview(reviewNo);

        return "redirect:/reviews";
    }
}