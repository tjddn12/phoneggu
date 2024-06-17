package com.jsbs.casemall.service;

import com.jsbs.casemall.constant.ProductCategory;
import com.jsbs.casemall.constant.ProductType;
import com.jsbs.casemall.dto.*;
import com.jsbs.casemall.entity.Product;
import com.jsbs.casemall.entity.ProductImg;
import com.jsbs.casemall.entity.ProductModel;
import com.jsbs.casemall.repository.ProductImgRepository;
import com.jsbs.casemall.repository.ProductModelRepository;
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
    private final ProductModelRepository productModelRepository;

    public Long saveProduct(ProductFormDto productFormDto, List<MultipartFile> productImgFileList) throws Exception {
        log.info("상품 등록 시작: {}", productFormDto);

        // 상품 등록
        Product product = productFormDto.createProduct();
        product = productRepository.save(product);
        productRepository.flush();

        log.info("상품 저장 완료: {}", product);

        // productFormDto에서 기종 정보와 재고 수량을 저장하는 로직 구현
        List<ProductModelDto> productModelDtoList = productFormDto.getProductModelDtoList();

        // 유효한 모델 추가
        for (ProductModelDto productModelDto : productModelDtoList) {
            //기종이 선택되지 않았어도 저장
            ProductModel productModel = new ProductModel();
            productModel.setProductModelSelect(productModelDto.getProductModelSelect());
            productModel.setPrStock(productModelDto.getPrStock());
            productModel.setProduct(product);
            product.addProductModel(productModel);
            log.info("유효한 상품 모델 추가 완료: {}", productModel);
        }

        // 이미지 등록
        for (int i = 0; i < productImgFileList.size(); i++) {
            ProductImg productImg = new ProductImg();
            productImg.setProduct(product);

            if (i == 0) {
                productImg.setPrMainImg("Y");
            } else {
                productImg.setPrMainImg("N");
            }
            productImgService.saveProductImg(productImg, productImgFileList.get(i));
        }

        log.info("상품 이미지 저장 완료");

        // 최종적으로 product를 다시 저장하여 null 값이 포함된 모델 삭제 반영
        productRepository.save(product);

        // pr_id가 null인 모델 삭제
        productModelRepository.deleteByPrIdIsNull();

        log.info("pr_id가 null인 모델 삭제 완료");

        return product.getId();
    }

    @Transactional(readOnly = true)
    public ProductFormDto getProductDtl(Long prId) {
        Product product = productRepository.findById(prId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        ProductFormDto productFormDto = ProductFormDto.of(product);

        List<ProductImg> productImgList = productImgRepository.findByProductId(prId);
        if (productImgList.isEmpty()) {
            log.warn("No product images found for ID: {}", prId);
        }

        List<ProductImgDto> productImgDtoList = productImgList.stream()
                .map(ProductImgDto::of)
                .collect(Collectors.toList());

        productFormDto.setProductImgDtoList(productImgDtoList);

        // 기종 정보 추가
        List<ProductModel> productModels = productModelRepository.findByProductId(prId);
        List<ProductModelDto> productModelDtoList = productModels.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        productFormDto.setProductModelDtoList(productModelDtoList);

        return productFormDto;
    }

    public void updateProduct(ProductFormDto productFormDto, List<MultipartFile> productImgFileList) throws Exception {
        // 상품 엔티티 가져오기
        Product product = productRepository.findById(productFormDto.getId())
                .orElseThrow(EntityNotFoundException::new);

        // 유효성 검사 및 모델 업데이트
        if (productFormDto.getProductModelDtoList() != null) {
            log.info("ProductModelDto 리스트가 있습니다. NULL 값 모델을 삭제합니다.");
            // NULL인 모델 삭제
            productModelRepository.deleteByProductModelSelectIsNull();
            log.info("NULL 값 모델이 삭제되었습니다.");

            // 유효성 검사 및 새 모델 추가
            List<ProductModelDto> validModelDtos = productFormDto.getProductModelDtoList().stream()
                    .filter(modelDto -> modelDto.getProductModelSelect() != null)
                    .collect(Collectors.toList());

            for (ProductModelDto modelDto : validModelDtos) {
                ProductModel productModel = new ProductModel();
                productModel.setProductModelSelect(modelDto.getProductModelSelect());
                productModel.setPrStock(modelDto.getPrStock());
                productModel.setProduct(product);
                productModelRepository.save(productModel); // 먼저 저장
                product.addProductModel(productModel);
                log.info("유효한 상품 모델 : {}", productModel);
            }
        }

        log.info("product : {}", product);

        // 상품 정보 업데이트
        product.updateProduct(productFormDto);

        // 기존 이미지를 유지하고 새로운 이미지 추가
        int currentSize = product.getProductImgList().size();
        int newSize = productImgFileList.size();
        log.info("기존 이미지 수: {}, 새 이미지 수: {}. 새로운 이미지를 추가합니다.", currentSize, newSize);

        for (int i = 0; i < newSize; i++) {
            ProductImg productImg = new ProductImg();
            productImg.setProduct(product);
            productImg.setPrMainImg((currentSize + i) == 0 ? "Y" : "N"); // 첫 번째 이미지만 메인으로 설정
            productImgService.saveProductImg(productImg, productImgFileList.get(i));
            product.addProductImg(productImg);
            log.info("새로운 이미지가 추가되었습니다. 인덱스: {}", (currentSize + i));
        }

        // NULL인 모델 삭제
        productModelRepository.deleteByProductModelSelectIsNull();
        log.info("NULL 값 모델이 삭제되었습니다.");

        productRepository.save(product);
        log.info("상품 정보가 성공적으로 업데이트되었습니다. 상품 ID: {}", product.getId());
    }


    @Transactional(readOnly = true)
    public Page<Product> getAdminProductPage(ProductSearchDto productSearchDto, Pageable pageable) {
        log.info("관리 페이지에서 페이징된 상품 목록을 가져옵니다. 페이징 정보: {}", pageable);
        return productRepository.getAdminProductPage(productSearchDto, pageable);
    }

    @Transactional(readOnly = true)
    public Page<MainProductDto> getMainProductPage(ProductSearchDto productSearchDto, Pageable pageable) {
        return productRepository.getMainProductPage(productSearchDto, pageable);
    }

    @Transactional
    public void deleteProduct(Long prId) throws Exception {
        log.info("삭제할 상품 ID: {}", prId);

        Product product = productRepository.findById(prId)
                .orElseThrow(() -> new EntityNotFoundException("상품이 존재하지 않습니다."));

        List<ProductImg> productImgList = productImgRepository.findByProductId(prId);

        log.info("연관된 이미지 수: {}", productImgList.size());

        // 상품에 연관된 이미지 삭제
        for (ProductImg productImg : productImgList) {
            log.info("삭제할 이미지 ID: {}", productImg.getId());
            productImgService.deleteProductImg(productImg.getId());
        }

        // 상품 삭제
        productRepository.delete(product);

        log.info("상품 삭제 완료: {}", prId);
    }

    public void deleteProductImage(Long imageId) {
        log.info("이미지 삭제 중, 이미지 ID: {}", imageId);
        try {
            productImgService.deleteProductImg(imageId);
            log.info("이미지 삭제 성공, 이미지 ID: {}", imageId);
        } catch (Exception e) {
            log.error("이미지 삭제 중 오류 발생, 이미지 ID: {}", imageId, e);
            throw new RuntimeException("이미지 삭제 중 오류가 발생했습니다.", e);
        }
    }

    public List<Product> getProductsByCategory(ProductCategory category) {
        return productRepository.findByProductCategory(category);
    }

    public List<Product> getProductsByType(ProductType type) {
        return productRepository.findByProductType(type);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    public List<ProductModelDto> getProductModelsByProductId(Long productId) {
        List<ProductModel> productModels = productModelRepository.findByProductId(productId);
        return productModels.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private ProductModelDto convertToDto(ProductModel productModel) {
        ProductModelDto productModelDto = new ProductModelDto();
        productModelDto.setId(productModel.getId());
        productModelDto.setProductModelSelect(productModel.getProductModelSelect());
        productModelDto.setPrStock(productModel.getPrStock());
        return productModelDto;
    }
}
