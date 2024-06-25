package com.jsbs.casemall.service;

import com.jsbs.casemall.dto.*;
import com.jsbs.casemall.entity.*;
import com.jsbs.casemall.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ReviewImgRepository reviewImgRepository;
    private final ReviewImgService reviewImgService;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final FileService fileService;



    public List<ReviewsDto> getAllReviews(){
        List<ReviewsDto> list = new ArrayList<>();
        List<Review> reviews =  reviewRepository.findAll();
        for (Review review : reviews) {
            ReviewsDto dto = new ReviewsDto();

            dto.setReviewNo(review.getId());
            dto.setUserid(review.getUserId().getUserId());
            dto.setRevwTitle(review.getRevwTitle());
            dto.setRevwContent(review.getRevwContent());
            dto.setRegTime(review.getRegTime());
            String prName = productRepository.findById(review.getPrId().getId()).get().getPrName();
            dto.setProductName(prName);
            dto.setRevwRatings(review.getRevwRatings());
            list.add(dto);
        }
        return list;
    }
    public Optional<Review> getReviewByNo(Long reviewNo){
        return reviewRepository.findById(reviewNo);
    }
    public List<Review> getReviewsByUserId(Users userId){
        return reviewRepository.findReviewsByUserId(userId);
    }

    public List<Review> getReviewsByOrder(Product product){
        return reviewRepository.findReviewsByPrId(product);
    }

    public void saveReview(ReviewFormDto reviewFormDto, List<MultipartFile> reviewImgFileList,String id) throws Exception {
        // 사용자 찾기
        Users user = userRepository.findById(id).orElseThrow(()->new IllegalArgumentException("찾는 사용자가 없습니다"));
        Product product = productRepository.findById(reviewFormDto.getProductId()).orElseThrow(()->new IllegalArgumentException("상품이 없습니다"));
        log.info("들어온 이미지 리스트 : {} ",reviewImgFileList);

        //리뷰 등록
        Review review = reviewFormDto.createReview();
        review.setUserId(user);
        review.setPrId(product);


        reviewRepository.save(review);
        //이미지 등록
        for(int i = 0; i < reviewImgFileList.size(); i++){
            ReviewImg reviewImg = new ReviewImg();
            reviewImg.setReview(review);
            reviewImgService.saveReviewImg(reviewImg, reviewImgFileList.get(i));
            log.info("img : {} ",reviewImg);
        }
    }
    @Transactional
    public void deleteReview(Long reviewNo){
        //리뷰에 해당하는 이미지 삭제
        reviewImgRepository.deleteByReviewId(reviewNo);
        reviewRepository.deleteById(reviewNo);
    }
    //    public boolean updateReview(ReviewFormDto reviewFormDto){
//        int updatedRows = reviewRepositoryCustom.updateAReview(reviewFormDto);
//
//        return updatedRows > 0;
//    }
//    수정------------------
//    public Long updateAReview(ReviewFormDto reviewFormDto, List<MultipartFile> reviewImgFileList) throws Exception {
//        //상품 수정
//        Review review = reviewRepository.findById(reviewFormDto.getId())
//                .orElseThrow(EntityNotFoundException::new);
//
//        review.update(reviewFormDto);
//
//        List<Long> reviewImgIds = reviewFormDto.getReviewImgIds();
//        //: 이미지 번호
//        //이미지 수정
//        for(int i = 0; i < reviewImgFileList.size(); i++){
//            reviewImgService.updateReviewImg(reviewImgIds.get(i), reviewImgFileList.get(i));
//        }
//
//        return review.getId();
//    }
//    public void saveRating(ReviewDto reviewDto) {
//        //새로운 Review 엔티티를 생성하고 평점 설정
//        Review review = new Review();
//
//        review.setRevwRatings(reviewDto.getRevwRatings());
//        //DB에 저장
//        reviewRepository.save(review);
//    }
    public Page<Review> getReviewList(int page){
        List<Sort.Order> sorts = new ArrayList<>();

        sorts.add(Sort.Order.desc("regTime"));

        Pageable pageable = PageRequest.of(page, 10);

        return this.reviewRepository.findAll(pageable);
    }
    public void saveReviewAndImage(Review review, ReviewImg reviewImg) {
        reviewRepository.save(review);
        reviewImg.setReview(review);
        reviewImgRepository.save(reviewImg);
    }
    public void saveReviewWithImages(Review review, List<ReviewImg> reviewImgs){
        reviewRepository.save(review);

        for(ReviewImg img : reviewImgs){
            img.setReview(review);
            reviewImgRepository.save(img);
        }
    }

    public String getImgUrlByReviewNo(Long reviewNo) {
        Optional<Review> optionalReview = getReviewByNo(reviewNo);


        if(optionalReview.isPresent()){
            Review review = optionalReview.get();
            log.info("리뷰 객체,{} , {}  {}  ",reviewNo,review,optionalReview.get()) ;
            ReviewImg reviewImg = reviewImgRepository.findByReview(review);

            if (reviewImg != null) {
                return reviewImg.getImgUrl();
            } else {
                return "https://via.placeholder.com/100"; // 리뷰 이미지가 존재하지 않을 경우 기본 이미지 URL 반환
            }
        } else {
            return "https://via.placeholder.com/100"; // 리뷰가 존재하지 않을 경우 기본 이미지 URL 반환
        }
    }
    public ReviewFormDto getImgAndName(Long prId){
        Product product = productRepository.findById(prId).orElseThrow(()->new IllegalArgumentException("상품이 존재하지 않습니다"));

        ReviewFormDto reviewFormDto = new ReviewFormDto();
        reviewFormDto.setImgUrl(product.getProductImgList().get(0).getImgUrl()); // 대표사진
        reviewFormDto.setProductName(product.getPrName());
        reviewFormDto.setProductId(product.getId());
        log.info("해당 제품 아이디 : {} ",product.getId());
        return reviewFormDto;
    }

    public Review creation(String title, String content){
        Review review = new Review();

        review.setRevwTitle(title);
        review.setRevwContent(content);

        this.reviewRepository.save(review);

        return review;
    }

//    public Long updateReviewFromForm(String userId, ReviewFormDto form) {
//        Users users = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("유저가 없습니다"));
//
//        Review target = reviewRepository.findById(form.getId()).orElseThrow(() -> new IllegalArgumentException("게시글이 없습니다."));
//
//        target.setRevwContent(form.getRevwContent());
//        target.setRevwTitle(form.getRevwTitle());
//        target.setRevwRatings(form.getRevwRatings());
//        //이미지 업데이트 로직 추후 구현.
//        List<ReviewImg> reviewImgList = form.getReviewImgDtoList().stream()
//                        .map(dto -> {
//                            ReviewImg img = new ReviewImg();
//
//                            img.setOriImgName(dto.getRevwOriImgName());
//                            img.setImgName(dto.getRevwImgName());
//                            img.setImgUrl(dto.getImgUrl());
//                            img.setReview(target); //: Review 엔티티와 연결
//
//                            return img;
//                        })
//                .collect(Collectors.toList());
//        //기존의 리뷰 이미지들을 모두 삭제하고, 새로운 리뷰 이미지들을 설정
//        target.getReviewImgs().clear();
//        target.getReviewImgs().addAll(reviewImgList);
//
//        Review saved = reviewRepository.save(target);
//
//        return saved.getId();
//    }
//    @Transactional
//    public void updateReview(ReviewFormDto reviewFormDto, List<MultipartFile> reviewImgFileList, String userId) throws IOException {
//        // 1. 리뷰 정보 업데이트
//        Review review = reviewRepository.findById(reviewFormDto.getId())
//                .orElseThrow(() -> new IllegalArgumentException("해당 리뷰가 존재하지 않습니다. ID: " + reviewFormDto.getId()));
//
//        review.setRevwTitle(reviewFormDto.getRevwTitle());
//        review.setRevwContent(reviewFormDto.getRevwContent());
//        review.setRevwRatings(reviewFormDto.getRevwRatings());
//
//        // 2. 리뷰 이미지 업데이트
//        if (reviewImgFileList != null && !reviewImgFileList.isEmpty()) {
//            // 기존 이미지 삭제
//            List<ReviewImg> existingImages = review.getReviewImgs();
//            for (ReviewImg img : existingImages) {
//                reviewImgRepository.deleteByReviewId(img.getReview().getId());
//            }
//            existingImages.clear();
//
//            // 새로 업로드한 이미지 추가
//            for (MultipartFile file : reviewImgFileList) {
//                ReviewImg reviewImg = new ReviewImg();
//                try{
//                    reviewImgService.saveReviewImg(reviewImg, file);
//                }catch(Exception e){
//                    e.printStackTrace();
//                }
//
//                existingImages.add(reviewImg);
//            }
//        }
//
//        reviewRepository.save(review);
//    }

    public ReviewsDto getReviewById(Long reviewNo) {
        Review review = reviewRepository.findById(reviewNo).orElseThrow(() ->
                new IllegalArgumentException("리뷰를 찾을 수 없습니다: " + reviewNo));

        return new ReviewsDto(review);
    }
}