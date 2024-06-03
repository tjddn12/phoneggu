package com.jsbs.casemall.repository;

import com.jsbs.casemall.entity.ProductImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductImgRepository extends JpaRepository<ProductImg, Long> {

    List<ProductImg> findByIdOrderByIdAsc(Long prId);
    //상품이미지 아이디의 오름차순으로 가져오는 쿼리 메소드

    ProductImg findByIdAndPrMainImg(Long prId, String prMainImg);
    //주문상품의 대표이미지를 보여주기 위한 쿼리

    List<ProductImg> findByProductId(Long productId);
}
