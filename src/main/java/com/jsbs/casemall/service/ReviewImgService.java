package com.jsbs.casemall.service;

import com.jsbs.casemall.dto.ReviewImgDto;
import com.jsbs.casemall.entity.Review;
import com.jsbs.casemall.entity.ReviewImg;
import com.jsbs.casemall.repository.ReviewImgRepository;
import com.jsbs.casemall.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewImgService {
    private final ReviewImgRepository reviewImgRepository;
    private final ReviewRepository reviewRepository;

    public void saveReviewImg(ReviewImg reviewImg) {
        reviewImgRepository.save(reviewImg);
    }
    public List<ReviewImgDto> getReviewImgsByReviewNo(Review reviewNo) {
        List<ReviewImg> reviewImgs = reviewImgRepository.findByReviewOrderByIdAsc(reviewNo);

        return reviewImgs.stream()
                .map(ReviewImgDto::of)
                .collect(Collectors.toList());
    }
    public ReviewImgDto getReviewImgById(Long id, String reviewMainImg) {
        ReviewImg reviewImg = reviewImgRepository.findByIdAndReviewMainImg(id, reviewMainImg);

        return ReviewImgDto.of(reviewImg);
    }
    public void deleteReviewImg(ReviewImg reviewImg){
        reviewImgRepository.delete(reviewImg);
    }

    public void saveReviewImages(List<MultipartFile> images) throws IOException {
        for(MultipartFile file : images){
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            ReviewImg reviewImg = new ReviewImg();

            reviewImg.setFileName(fileName);
            reviewImg.setData(file.getBytes());
            reviewImgRepository.save(reviewImg);
        }

    }
}