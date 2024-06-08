package com.jsbs.casemall.service;

import com.jsbs.casemall.constant.ProductCategory;
import com.jsbs.casemall.constant.ProductType;
import com.jsbs.casemall.dto.*;
import com.jsbs.casemall.entity.Product;
import com.jsbs.casemall.entity.ProductImg;
import com.jsbs.casemall.repository.ProductImgRepository;
import com.jsbs.casemall.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;



@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductImgRepository productImgRepository;
    private final ProductRepository productRepository;
    private final ProductImgService productImgService;

    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

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

    //상품데이터를 읽어오는 함수
    @Transactional(readOnly = true)
    public ProductFormDto getProductDtl(Long prId){
        Product product = productRepository.findById(prId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        // Product 엔티티를 ProductFormDto로 변환하고 이미지 리스트를 설정
        ProductFormDto productFormDto = ProductFormDto.of(product);

        // 이미지 리스트를 prId 기준으로 가져오는 로직 추가
        List<ProductImg> productImgList = productImgRepository.findByIdOrderByIdAsc(prId);
        if (productImgList.isEmpty()) {
            log.warn("No product images found for ID: {}", prId); // 이미지가 없는 경우 경고 출력
        }

//        // ProductImg 엔티티를 ProductImgDto로 변환하여 리스트에 추가
//        List<ProductImgDto> productImgDtoList = new ArrayList<>();
//        for (ProductImg productImg : productImgList) {
//            ProductImgDto productImgDto = ProductImgDto.of(productImg);
//            productImgDtoList.add(productImgDto);
//        }

        List<ProductImgDto> productImgDtoList = product.getProductImgList().stream()
                .map(img -> {
                    ProductImgDto productImgDto = new ProductImgDto();
                    productImgDto.setImgUrl(img.getImgUrl());
                    return productImgDto;
                })
                .collect(Collectors.toList());

        // Product 엔티티를 가져와서 없으면 예외 발생



        productFormDto.setProductImgDtoList(productImgDtoList); // 이미지 리스트 설정

        return productFormDto;
    }

    public void updateProduct(ProductFormDto productFormDto, List<MultipartFile> productImgFileList) throws Exception {
        // 상품 엔티티 가져오기
        Product product = productRepository.findById(productFormDto.getId())
                .orElseThrow(EntityNotFoundException::new);

        // 상품 정보 업데이트
        product.updateProduct(productFormDto);

        // 상품 이미지가 비어있을 경우 처리
        if (productImgFileList.isEmpty()) {
            // 예외를 던지거나 적절한 기본 동작을 수행
            throw new IllegalArgumentException("Product image list cannot be empty");
        }

        // 상품 이미지 업데이트
        for (int i = 0; i < productImgFileList.size(); i++) {
            MultipartFile productImgFile = productImgFileList.get(i);
            // 이미지 파일 저장 또는 업데이트
        }

        productRepository.save(product);
    }

    @Transactional(readOnly = true)
    public Page<Product> getAdminProductPage(ProductSearchDto productSearchDto, Pageable pageable){
        return productRepository.getAdminProductPage(productSearchDto, pageable);
        //return productRepository.findAll(pageable);

    }
    //상품 조회조건 페이지 정보를 파라미터로 받아와서 상품 데이터를 조회하는
    //    getAdminproductpage 메소드 추가

    @Transactional(readOnly = true)
    public Page<MainProductDto> getMainProductPage(ProductSearchDto productSearchDto, Pageable pageable){
        return productRepository.getMainProductPage(productSearchDto, pageable);
    }

    @Transactional
    public void deleteProduct(Long prId) throws Exception {
        Product product = productRepository.findById(prId)
                .orElseThrow(() -> new EntityNotFoundException("상품이 존재하지 않습니다."));

        List<ProductImg> productImgList = productImgRepository.findByProductId(prId);

        // 상품에 연관된 이미지 삭제
        for (ProductImg productImg : productImgList) {
            productImgService.deleteProductImg(productImg.getId());
        }

        // 상품 삭제
        productRepository.delete(product);
    }

    public List<Product> getProductsByCategory(ProductCategory category) {
        return productRepository.findByProductCategory(category);
    }

    public List<Product> getProductsByType(ProductType type) {
        return productRepository.findByProductType(type);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll(); // 전체 상품을 반환하는 메서드
    }
}
