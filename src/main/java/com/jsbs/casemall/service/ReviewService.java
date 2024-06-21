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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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



    public List<ReviewsDto> getAllReviews(){
        List<ReviewsDto> list = new ArrayList<>();
        List<Review> reviews =  reviewRepository.findAll();
        for (Review review : reviews) {
            ReviewsDto dto = new ReviewsDto();
            dto.setReviewNo(review.getReviewNo());
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
        }
    }
    public void deleteReview(Long reviewNo){
        reviewRepository.deleteById(reviewNo);
    }
    //    public boolean updateReview(ReviewFormDto reviewFormDto){
//        int updatedRows = reviewRepositoryCustom.updateAReview(reviewFormDto);
//
//        return updatedRows > 0;
//    }
//    수정------------------
    public Long updateAReview(ReviewFormDto reviewFormDto, List<MultipartFile> reviewImgFileList) throws Exception {
        //상품 수정
        Review review = reviewRepository.findById(reviewFormDto.getId())
                .orElseThrow(EntityNotFoundException::new);

        review.update(reviewFormDto);

        List<Long> reviewImgIds = reviewFormDto.getReviewImgIds();
        //: 이미지 번호
        //이미지 수정
        for(int i = 0; i < reviewImgFileList.size(); i++){
            reviewImgService.updateReviewImg(reviewImgIds.get(i), reviewImgFileList.get(i));
        }

        return review.getReviewNo();
    }
    public void saveRating(ReviewDto reviewDto) {
        //새로운 Review 엔티티를 생성하고 평점 설정
        Review review = new Review();

        review.setRevwRatings(reviewDto.getRevwRatings());
        //DB에 저장
        reviewRepository.save(review);
    }
    public PageDto<Review> getReviewList(Criteria criteria){
        if(criteria == null){
            criteria = new Criteria();
        }
        Pageable pageable = PageRequest.of(criteria.getCurrentPageNo() - 1,
                criteria.getRecordsPerPage(), Sort.by("reviewNo").descending());
        Page<Review> reviewPage = reviewRepository.findAll(pageable);

        return new PageDto<>(
                reviewPage.getContent(),
                (int) reviewPage.getTotalElements(),
                criteria.getCurrentPageNo(),
                criteria.getRecordsPerPage()
        );
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
            log.info("리뷰겍체,{} , {}  {}  ",reviewNo,review,optionalReview.get()) ;
            ReviewImg reviewImg = reviewImgRepository.findByReview(review);

            return reviewImg.getImgUrl();
        }else{
            return "https://via.placeholder.com/100"; //: 리뷰가 존재하지 않을 경우 기본 이미지 URL 반환
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




}