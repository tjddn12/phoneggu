package com.jsbs.casemall.service;

import com.jsbs.casemall.dto.MainProductDto;
import com.jsbs.casemall.dto.ProductFormDto;
import com.jsbs.casemall.dto.ProductImgDto;
import com.jsbs.casemall.dto.ProductSearchDto;
import com.jsbs.casemall.entity.Product;
import com.jsbs.casemall.entity.ProductImg;
import com.jsbs.casemall.repository.ProductImgRepository;
import com.jsbs.casemall.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {

    private final ProductImgRepository productImgRepository;
    private final ProductRepository productRepository;
    private final ProductImgService productImgService;

    public Long saveProduct(ProductFormDto productFormDto, List<MultipartFile> productImgFileList) throws Exception{
        //상품 등록
        Product product = productFormDto.createProduct();
        productRepository.save(product);

        //이미지등록
        for(int i = 0; i < productImgFileList.size(); i++){
            ProductImg productImg = new ProductImg();
            productImg.setProduct(product);

            if(i == 0){ //첫번째 등록한 이미지를 대표 이미지로 사용
                productImg.setPrMainImg("Y");
            }else{
                productImg.setPrMainImg("N");
            }
            productImgService.saveProductImg(productImg, productImgFileList.get(i));
        }
        return  product.getId();
    }

    public List<Product> getProductList(Long categoriesId){
        return productRepository.findByCategoriesId(categoriesId);
    }

    //상품데이터를 읽어오는 함수
    @Transactional(readOnly = true)
    public ProductFormDto getProductDtl(Long productId){
        List<ProductImg> productImgList =  productImgRepository.findByPrIdOrderByIdAsc(productId);
        List<ProductImgDto> productImgDtoList = new ArrayList<>();
        for(ProductImg productImg : productImgList){
            ProductImgDto productImgDto= ProductImgDto.of(productImg);
            productImgDtoList.add(productImgDto);
        }
        Product product = productRepository.findById(productId).
                orElseThrow(EntityNotFoundException::new);

        ProductFormDto productFormDto = ProductFormDto.of(product);
        productFormDto.setPrImgDtoList(productImgDtoList);
        return  productFormDto;
    }

    public Long updateProduct(ProductFormDto productFormDto, List<MultipartFile> productImgFileList) throws Exception {
        //상품수정
        Product product = productRepository.findById(productFormDto.getId())
                .orElseThrow(EntityNotFoundException::new);
        product.updateProduct(productFormDto);
        List<Long> productImgIds = productFormDto.getPrImgIds();
        //이미지번호
        //이미지 수정
        for(int i=0; i < productImgFileList.size(); i++){
            productImgService.updateProductImg(productImgIds.get(i),
                    productImgFileList.get(i));
        }
        return product.getId();
    }

    @Transactional(readOnly = true)
    public Page<Product> getAdminProductPage(ProductSearchDto productSearchDto, Pageable pageable){
        return productRepository.getAdminProductPage(productSearchDto, pageable);
    }
    //상품 조회조건 페이지 정보를 파라미터로 받아와서 상품 데이터를 조회하는
    //    getAdminproductpage 메소드 추가

    @Transactional(readOnly = true)
    public Page<MainProductDto> getMainProductPage(ProductSearchDto productSearchDto, Pageable pageable){
        return productRepository.getMainProductPage(productSearchDto, pageable);
    }
}
