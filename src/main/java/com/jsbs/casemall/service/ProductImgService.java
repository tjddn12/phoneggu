package com.jsbs.casemall.service;

import com.jsbs.casemall.entity.ProductImg;
import com.jsbs.casemall.repository.ProductImgRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductImgService {

    @Value("${productImgLocation}")
    private String productImgLocation;

    private final ProductImgRepository productImgRepository;

    private final FileService fileService;

    public void saveProductImg(ProductImg productImg, MultipartFile productImgFile) throws Exception{
        String prImgOriginName = productImgFile.getOriginalFilename();
        String prImgName="";
        String imgUrl="";

        //파일 업로드
        if(!StringUtils.isEmpty(prImgOriginName)){
            prImgName = fileService.uploadFile(productImgLocation, prImgOriginName,
                    productImgFile.getBytes());
            //사용자가 상품의 이미지를 등록했다면 저장할 경로와 파일의 이름,
            //파일을 파일의 바이트 배열을
            //파일 업로드 파라미터로 uploadFile 매서드 호출
            //호출 결과 실제 저장되는 파일의 이름을 prImgName에 저장

            imgUrl="/images/"+prImgName;
            //저장한 상품 이미지를 불러올 경로를 설정
            //그래서 내부에서만 실제 경로를 사용하고 외부에서는 접근권한이 있는 경로로
            //사용하도록 리소스 연결작업을 해주었다.
            //즉 외부(브라우저를 통한 접속) 에서는 static하위 디렉토리인
            //images를 사용하도록 되어있다.
        }
        productImg.updateProductImg(prImgOriginName, prImgName, imgUrl);
        //원본이미지이름, 이미지이름, 이미지url 업데이트

        productImgRepository.save(productImg); // 테이블저장
    }

    //productImg 상품이미지 정보
    //클라이언트 MultipartFile productImgFile 업로드된 이미지 파일을 나타내는 객체 -> 원본파일이름을 가져온다.
    public void updateProductImg(Long prImgId, MultipartFile productImgFile) throws Exception {
        //prImgId 수정할 상품 이미지 식별자
        //prImgFile 업로드된 새로운 상품이미지 파일
        if(!productImgFile.isEmpty()){ // 업로드한 파일이 있는경우(새로운 이미지를 업로드한경우)
            ProductImg savedProductImg = productImgRepository.findById(prImgId).
                    orElseThrow(EntityNotFoundException::new);
            //기존 이미지 파일삭제
            if(!StringUtils.isEmpty(savedProductImg.getPrImgName())){
                fileService.deleteFile(productImgLocation +"/"+savedProductImg.getPrImgName());
            }

            String prImgOriginName = productImgFile.getOriginalFilename();
            //업로드된 이미지 원본파일명
            String prImgName = fileService.uploadFile(productImgLocation,
                    prImgOriginName, productImgFile.getBytes());
            //새로운파일업로드 - 업로드한 파일명을 -> prImgName 변수저장
            String imgUrl = "/images/" + prImgName;
            //이미지 url 을 /images 경로와 업로드된 이미지 이름을 합쳐생성
            savedProductImg.updateProductImg(prImgOriginName, prImgName, imgUrl);
            //updateProductImg 메서드를 호출하여 원본이미지 이름, 업로드된 이미지이름, 이미지 url 정보 업데이트
        }
    }

    public void deleteProductImg(Long prImgId) throws Exception {
        ProductImg productImg = productImgRepository.findById(prImgId)
                .orElseThrow(EntityNotFoundException::new);

        // 파일 삭제
        if (!StringUtils.isEmpty(productImg.getPrImgName())) {
            fileService.deleteFile(productImgLocation + "/" + productImg.getPrImgName());
        }

        // 이미지 엔티티 삭제
        productImgRepository.delete(productImg);
    }

}
