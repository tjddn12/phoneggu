package com.jsbs.casemall.controller;

import com.jsbs.casemall.dto.ReviewFormDto;
import com.jsbs.casemall.dto.ReviewsDto;
import com.jsbs.casemall.entity.Review;
import com.jsbs.casemall.repository.ReviewImgRepository;
import com.jsbs.casemall.repository.ReviewRepository;
import com.jsbs.casemall.repository.UserRepository;
import com.jsbs.casemall.service.OrderService;
import com.jsbs.casemall.service.ReviewImgService;
import com.jsbs.casemall.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

//리뷰 게시판 뷰페이지 매핑
@Slf4j
@Controller
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewImgRepository reviewImgRepository;
    private final ReviewImgService reviewImgService;
    private final OrderService orderService;


    // 리뷰 쓰기
    @GetMapping(value = "/reviewWrite")
    public String reviewForm(@RequestParam long prId, Model model) {
        log.info("넘어온 주문 번호 : {} ", prId);

        ReviewFormDto dto = reviewService.getImgAndName(prId);


        model.addAttribute("reviewFormDto", new ReviewFormDto());
        model.addAttribute("product", dto);

        return "review/reviewWrite";
    }


    @PostMapping("/reviewWrite")
    public String reviewNew(@Valid ReviewFormDto reviewFormDto, BindingResult bindingResult, Model model, Principal principal,
                            @RequestParam("reviewImgFile") List<MultipartFile> reviewImgFileList) {
        String userId = principal.getName();
        log.info("넘어온값 : {} ",reviewFormDto.toString());

        if (bindingResult.hasErrors()) {
            log.info("포스트 매핑 오류 ");
            return "review/reviewWrite";
        }
        if (reviewImgFileList.get(0).isEmpty() && reviewFormDto.getId() == null) {
            model.addAttribute("errorMessage", "리뷰 이미지는 필수 입력값입니다.");

            return "review/reviewWrite";
        }

        try {
            reviewService.saveReview(reviewFormDto, reviewImgFileList, userId);
        } catch (Exception e) {
            log.info("문제 발생 {} " , e.getMessage());
            model.addAttribute("errorMessage", "리뷰 등록 중 에러가 발생하였습니다.");

            return "review/reviewWrite";
        }

        return "redirect:/reviews";
    }

    @PostMapping(value = "/{reviewNo}")
    public String reviewUpdate(@Valid ReviewFormDto reviewFormDto, BindingResult bindingResult,
                               Model model, @RequestParam("reviewImgFile") List<MultipartFile> reviewImgFileList) {
        if (bindingResult.hasErrors()) {

            return "review/reviewWrite";
        }
        if (reviewImgFileList.get(0).isEmpty() && reviewFormDto.getId() == null) {
            model.addAttribute("errorMessage", "리뷰 이미지는 필수 입력값입니다.");

            return "review/reviewWrite";
        }

        try {
            reviewService.updateAReview(reviewFormDto, reviewImgFileList);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "리뷰 수정 중 에러가 발생하였습니다.");

            return "review/reviewWrite";
        }

        return "redirect:/reviews";
    }

    //리뷰 게시판 페이징 처리 및 매개변수 전달
    @GetMapping
    public String list(Model model, @RequestParam(value = "page", defaultValue = "0") int page) {
        Page<Review> paging = this.reviewService.getReviewList(page);
        Page<ReviewFormDto> reviewFormDtos = paging.map(review -> {
            return new ReviewFormDto(
                    review.getReviewNo(),
                    reviewImgRepository.findByReview(review).getImgUrl(),
                    review.getRevwTitle(),
                    review.getRevwContent(),
                    review.getPrId().getPrName(),
                    review.getUserId().getName(),
                    review.getRevwRatings()
            );
        });
//        Map<Long, String> reviewImages = new HashMap<>();
//
//        for (ReviewFormDto review : reviewFormDtos) {
////            String imageUrl = reviewService.getImgUrlByReviewNo(review.getReviewNo());
//            reviewImages.put(review.getId(), review.getImgUrl());
////        }

            model.addAttribute("paging", reviewFormDtos);
//            model.addAttribute("reviewImages", reviewImages);

        return "review/reviews";
//        List<ReviewsDto> reviews = reviewService.getAllReviews();
//        PageDto<Review> pageDto = reviewService.getReviewList(criteria);
//        List<Integer> pageNumbers = IntStream.rangeClosed(1, pageDto.getTotalRecordCount())
//                .boxed()
//                .collect(Collectors.toList());
//        //페이징 처리
//        List<ReviewsDto> dto = new ArrayList<>();
//        model.addAttribute("pageDto", pageDto);
//        model.addAttribute("pageNumbers", pageNumbers);
//
//
//
//        model.addAttribute("reviews", reviews);
    }

    //    @PostMapping
//    public String createReview(@ModelAttribute Review review){
//        reviewService.saveReview(review);
//
//        return "redirect:/reviews";
//    }

//    @GetMapping("/{reviewNo}")
//    public String getReviewDetail(@PathVariable Long reviewNo, Model model) {
//        List<ReviewsDto> reviews = reviewService.getAllReviews();
//        Review review = reviewService.getReviewByNo(reviewNo).orElse(null);
//
//        Map<Long, String> reviewImages = new HashMap<>();
//        for (ReviewsDto revw : reviews) {
//            String imageUrl = reviewService.getImgUrlByReviewNo(revw.getReviewNo());
//
//            reviewImages.put(review.getReviewNo(), imageUrl);
//        }
//
//        model.addAttribute("review", review);
//        model.addAttribute("reviewImages", reviewImages);
//
//        return "review/reviewDetail";
//    }
    @GetMapping("/{reviewNo}")
    public String getReviewDetail(@PathVariable Long reviewNo, Model model) {
        List<ReviewsDto> reviews = reviewService.getAllReviews();
        ReviewsDto review = reviews.get(0);
        Map<Long, String> reviewImages = new HashMap<>();
        for (ReviewsDto revw : reviews) {
            String imageUrl = reviewService.getImgUrlByReviewNo(revw.getReviewNo());
            reviewImages.put(review.getReviewNo(), imageUrl);
        }

        model.addAttribute("review", review);
        log.info("시간 확인 :  {} " , review.getRegTime());
        model.addAttribute("reviewImages", reviewImages);

        return "review/reviewDetail";
    }

    //    @GetMapping("/user/{userId}")
//    public String getReviewsByUserId(@PathVariable String userId, Model model){
//        Optional<Users> userOpt = userRepository.findByUserId(userId);
//
//        if(userOpt.isPresent()){
//            Users user = userOpt.get();
//
//            model.addAttribute("reviews", reviewService.getReviewsByUserId(user));
//        }else{
//            model.addAttribute("reviews", List.of());
//            model.addAttribute("error", "해당 사용자 ID를 찾을 수 없습니다.");
//        }
//
//        return "review/reviews";
//    }
    @DeleteMapping("/{reviewNo}")
    public String deleteReview(@PathVariable Long reviewNo) {
        reviewService.deleteReview(reviewNo);

        return "redirect:/reviews";
    }
}