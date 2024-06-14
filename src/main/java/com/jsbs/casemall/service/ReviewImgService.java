package com.jsbs.casemall.service;

import com.jsbs.casemall.dto.ReviewImgDto;
import com.jsbs.casemall.entity.ReviewImg;
import com.jsbs.casemall.repository.ReviewImgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewImgService {
    private final ReviewImgRepository reviewImgRepository;

    public void saveReviewImg(ReviewImg reviewImg) {
        reviewImgRepository.save(reviewImg);
    }
    public List<ReviewImgDto> getReviewImgsByReviewNo(Long reviewNo) {
        List<ReviewImg> reviewImgs = reviewImgRepository.findByReviewNoOrderByIdAsc(reviewNo);

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
}