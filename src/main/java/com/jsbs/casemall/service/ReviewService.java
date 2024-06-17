package com.jsbs.casemall.service;

import com.jsbs.casemall.dto.Criteria;
import com.jsbs.casemall.dto.PageDto;
import com.jsbs.casemall.dto.ReviewDto;
import com.jsbs.casemall.dto.ReviewFormDto;
import com.jsbs.casemall.entity.Product;
import com.jsbs.casemall.entity.Review;
import com.jsbs.casemall.entity.ReviewImg;
import com.jsbs.casemall.entity.Users;
import com.jsbs.casemall.repository.ReviewImgRepository;
import com.jsbs.casemall.repository.ReviewRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ReviewImgRepository reviewImgRepository;
    private final ReviewImgService reviewImgService;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository, ReviewImgRepository reviewImgRepository, ReviewImgService reviewImgService){
        this.reviewRepository = reviewRepository;
        this.reviewImgRepository = reviewImgRepository;
        this.reviewImgService = reviewImgService;
    }
    public List<Review> getAllReviews(){
        return reviewRepository.findAll();
    }
    public Optional<Review> getReviewByNo(Long reviewNo){
        return reviewRepository.findById(reviewNo);
    }
    public List<Review> getReviewsByUserId(Users userId){
        return reviewRepository.findReviewsByUserId(userId);
    }
    public List<Review> getReviewsByPrName(Product prName){
        return reviewRepository.findReviewsByPrName(prName);
    }
    public Long saveReview(ReviewFormDto reviewFormDto, List<MultipartFile> reviewImgFileList) throws Exception {
        //리뷰 등록
        Review review = reviewFormDto.createReview();
        reviewRepository.save(review);
        //이미지 등록
        for(int i = 0; i < reviewImgFileList.size(); i++){
            ReviewImg reviewImg = new ReviewImg();

            reviewImg.setReview(review);
            reviewImgService.saveReviewImg(reviewImg, reviewImgFileList.get(i));
        }

        return review.getReviewNo();
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
            ReviewImg reviewImg = reviewImgRepository.findByReview(review);

            return reviewImg.getImgUrl();
        }else{
            return "https://via.placeholder.com/100"; //: 리뷰가 존재하지 않을 경우 기본 이미지 URL 반환
        }
    }
}