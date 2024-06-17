package com.jsbs.casemall.service;

import com.jsbs.casemall.entity.Review;
import com.jsbs.casemall.entity.ReviewImg;
import com.jsbs.casemall.repository.ReviewImgRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Transactional
public class ReviewImgService {
    @Value("${reviewImgLocation}")
    private String reviewImgLocation;
    //reviewImgLocation 변수에 c:/~/review 이미지 경로 할당
    private final ReviewImgRepository reviewImgRepository;
    private final FileService fileService;

    @Autowired
    public ReviewImgService(ReviewImgRepository reviewImgRepository, FileService fileService) {
        this.reviewImgRepository = reviewImgRepository;
        this.fileService = fileService;
    }

    public void saveReviewImg(ReviewImg reviewImg, MultipartFile reviewImgFile) throws Exception{
        //reviewImg: 리뷰 이미지 정보
        //MultipartFile reviewImgFile: 업로드된 이미지 파일을 나타내는 객체
        String oriImgName = reviewImgFile.getOriginalFilename();
        String imgName = "";
        String imgUrl = "";
        //파일 업로드
        if(!StringUtils.isEmpty(oriImgName)){
            imgName = fileService.reviewUploadFile(reviewImgLocation, oriImgName, reviewImgFile.getBytes());
            imgUrl = "/images/review/" + imgName;
        }
        //리뷰 이미지 정보 저장
        reviewImg.updateReviewImg(oriImgName, imgName, imgUrl);
        //원본 이미지 이름, 이미지 이름, 이미지 URL 업데이트
        reviewImgRepository.save(reviewImg);
    }
    public void updateReviewImg(Long reviewImgId, MultipartFile reviewImgFile) throws Exception{
        //reviewImgId: 수정할 리뷰 이미지 식별자
        //reviewImgFile: 업로드된 새로운 리뷰 이미지 파일
        if(!reviewImgFile.isEmpty()) { //: 업로드한 파일이 있는 경우(새로운 이미지를 업로드한 경우)
            ReviewImg savedReviewImg = reviewImgRepository.findById(reviewImgId)
                    .orElseThrow(EntityNotFoundException::new);
            //기존 이미지 파일 삭제
            if (!StringUtils.isEmpty(savedReviewImg.getImgName())) {
                fileService.deleteFile(reviewImgLocation + "/" + savedReviewImg.getImgName());
            }

            String oriImgName = reviewImgFile.getOriginalFilename();
            //: 업로드된 이미지 원본 파일명
            String imgName = fileService.reviewUploadFile(reviewImgLocation, oriImgName, reviewImgFile.getBytes());
            //: 새로운 파일 업로드 - 업로드한 파일명을 imgName 변수 저장
            String imgUrl = "/images/review/" + imgName;

            savedReviewImg.updateReviewImg(oriImgName, imgName, imgUrl);
            //: updateReviewImg 메서드를 호출하여 원본 이미지 이름, 업로드된 이미지 이름, 이미지 URL 정보 업데이트
        }
    }
}