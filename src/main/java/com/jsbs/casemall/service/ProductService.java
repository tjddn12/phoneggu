package com.jsbs.casemall.service;

import com.jsbs.casemall.constant.ProductCategory;
import com.jsbs.casemall.constant.ProductModelSelect;
import com.jsbs.casemall.constant.ProductSellStatus;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
            productModel.setProductModelSelect(
                    productModelDto.getProductModelSelect() != null ? productModelDto.getProductModelSelect() : ProductModelSelect.DEFAULT_MODEL);
            productModel.setPrStock(productModelDto.getPrStock() != null ? productModelDto.getPrStock() : 0);
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

        // 재고 상태를 기반으로 판매 상태 업데이트
        updateProductSellStatus(product);
        productRepository.save(product); // 상태 업데이트 후 저장

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

        product.updateProduct(productFormDto);
        productRepository.save(product);

        // 기존 모델 삭제
        productModelRepository.deleteByProduct(product);

        // productFormDto에서 기종 정보와 재고 수량을 저장하는 로직 구현
        List<ProductModelDto> productModelDtoList = productFormDto.getProductModelDtoList();

        // 유효한 모델 추가
        for (ProductModelDto productModelDto : productModelDtoList) {
            //기종이 선택되지 않았어도 저장
            ProductModel productModel = new ProductModel();
            productModel.setProductModelSelect(
                    productModelDto.getProductModelSelect() != null ? productModelDto.getProductModelSelect() : ProductModelSelect.DEFAULT_MODEL);
            productModel.setPrStock(productModelDto.getPrStock() != null ? productModelDto.getPrStock() : 0);
            productModel.setProduct(product);
            product.addProductModel(productModel);
            log.info("유효한 상품 모델 추가 완료: {}", productModel);
        }

        log.info("product : {}", product);

        // pr_id가 null인 모델 삭제
        productModelRepository.deleteByPrIdIsNull();
        log.info("pr_id가 null인 모델 삭제 완료");

        // 상품 정보 업데이트
        product.updateProduct(productFormDto);

        // 새로운 이미지가 업로드된 경우에만 추가합니다.
        if (!productImgFileList.isEmpty()) {
            for (MultipartFile file : productImgFileList) {
                if (!file.isEmpty()) {
                    ProductImg productImg = new ProductImg();
                    productImg.setProduct(product);

                    if (product.getProductImgList().isEmpty()) {
                        productImg.setPrMainImg("Y");
                    } else {
                        productImg.setPrMainImg("N");
                    }
                    productImgService.saveProductImg(productImg, file);
                    product.addProductImg(productImg);
                    log.info("새로운 이미지가 추가되었습니다: {}", file.getOriginalFilename());
                }
            }
        }

        productRepository.save(product);
        log.info("상품 정보가 성공적으로 업데이트되었습니다. 상품 ID: {}", product.getId());

        // 재고 상태를 기반으로 판매 상태 업데이트
        updateProductSellStatus(product);
        productRepository.save(product); // 상태 업데이트 후 저장
    }

    @Transactional(readOnly = true)
    public Page<Product> getAdminProductPage(ProductSearchDto productSearchDto, int page) {
        Pageable pageable = PageRequest.of(page, 5);
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

    public List<Product> getListProductsByCategory(ProductCategory category) {
        return productRepository.findByProductCategory(category);
    }

    public List<Product> getListProductsByType(ProductType type) {
        return productRepository.findByProductType(type);
    }

    public List<Product> getAllListProducts() {
        return productRepository.findAll();
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

    // 재고 상태에 따른 판매 상태 업데이트 로직
    private void updateProductSellStatus(Product product) {
        boolean allStocksZero = product.getProductModelList().stream()
                .allMatch(model -> {
                    Integer stock = model.getPrStock();
                    log.info("ProductModel ID: {}, prStock: {}", model.getId(), stock);
                    return stock == null || stock == 0;
                });

        if (allStocksZero) {
            product.setProductSellStatus(ProductSellStatus.SOLD_OUT);
        } else {
            product.setProductSellStatus(ProductSellStatus.SELL);
        }
        log.info("Product ID: {}, ProductSellStatus: {}", product.getId(), product.getProductSellStatus());
        productRepository.save(product); // 상태 업데이트 후 저장
    }

    public List<Product> findAll(Sort sort) {
        List<Product> list = productRepository.findAll(sort);
        log.info("Products found: {}", list.size());
        return list;
    }
}
