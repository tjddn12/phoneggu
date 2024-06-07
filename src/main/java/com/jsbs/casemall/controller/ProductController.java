package com.jsbs.casemall.controller;

import ch.qos.logback.classic.Logger;
import com.jsbs.casemall.dto.ProductFormDto;
import com.jsbs.casemall.dto.ProductSearchDto;
import com.jsbs.casemall.entity.Product;
import com.jsbs.casemall.service.ProductService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    @Autowired
    private final ProductService productService;

    // 새 상품 등록 폼을 반환
    @GetMapping("/admin/product/new")
    public String productForm(Model model) {
        model.addAttribute("productFormDto", new ProductFormDto());
        return "product/productForm";
    }

    // 새 상품 등록 처리
    @PostMapping("/admin/product/new")
    public String productNew(@Valid ProductFormDto productFormDto, BindingResult bindingResult,
                             Model model, @RequestParam("productImgFile") List<MultipartFile> productImgFileList) {
        if (bindingResult.hasErrors()) {
            return "product/productForm"; // 유효성 검사 오류 시 폼으로 이동
        }
        if (productImgFileList.get(0).isEmpty() && productFormDto.getId() == null) {
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값 입니다.");
            return "product/productForm"; // 첫번째 이미지가 없을 경우 에러 메시지와 함께 폼으로 이동
        }
        try {
            productService.saveProduct(productFormDto, productImgFileList); // 상품 저장
        } catch (Exception e) {
            model.addAttribute("errorMessage", "상품 등록 중 에러가 발생하였습니다.");
            return "product/productForm"; // 저장 중 에러 발생 시 에러 메시지와 함께 폼으로 이동
        }
        return "redirect:/";
    }

    // 상품 상세 정보 조회
    @GetMapping(value = "/admin/product/{prId}")
    public String productDtl(@PathVariable("prId") Long prId, Model model) {
        try {
            ProductFormDto productFormDto = productService.getProductDtl(prId); // 상품 정보 조회
            model.addAttribute("productFormDto", productFormDto); // 모델에 상품 정보 추가
        } catch (EntityNotFoundException e) {
            model.addAttribute("errorMessage", "존재하지 않는 상품입니다.");
            model.addAttribute("productFormDto", new ProductFormDto());
            return "product/productForm"; // 상품이 존재하지 않을 경우 에러 메시지와 함께 폼으로 이동
        }
        return "product/productForm";
    }

    // 상품 수정 처리
    @PostMapping(value = "/admin/product/{prId}")
    public String productUpdate(@PathVariable Long prId,
                                @Valid ProductFormDto productFormDto, BindingResult bindingResult,
                                Model model, @RequestParam("productImgFile") List<MultipartFile> productImgFileList) {

        log.debug("Updating product with ID: {}", prId);

        if (bindingResult.hasErrors()) {
            log.error("Binding errors: {}", bindingResult.getAllErrors());
            return "product/productForm"; // 유효성 검사 오류 시 폼으로 이동
        }

        if (productImgFileList.isEmpty() && productFormDto.getId() == null) {
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값 입니다.");
            log.error("First product image is missing");
            return "product/productForm"; // 첫번째 이미지가 없을 경우 에러 메시지와 함께 폼으로 이동
        }

        try {
            log.debug("Calling productService.updateProduct with ProductFormDto: {}", productFormDto);
            productService.updateProduct(productFormDto, productImgFileList); // 상품 업데이트
        } catch (Exception e) {
            model.addAttribute("errorMessage", "상품 수정 중 에러가 발생하였습니다.");
            log.error("Error updating product", e);
            return "product/productForm"; // 업데이트 중 에러 발생 시 에러 메시지와 함께 폼으로 이동
        }
        return "redirect:/admin/product/management";
    }


    // 상품 관리 페이지 및 검색 처리
    @GetMapping(value = {"/admin/product/management", "/admin/product/management/{page}"})
    public String productManage(ProductSearchDto productSearchDto,
                                @PathVariable(name = "prId", required = false) Long prId,
                                @PathVariable(name = "page", required = false) Integer page, Model model) {

        Pageable pageable = PageRequest.of(page != null ? page : 0, 5);
        Page<Product> management = productService.getAdminProductPage(productSearchDto, pageable);

        if (prId != null) {
            ProductFormDto productFormDto = productService.getProductDtl(prId); // 상품 정보 조회
            model.addAttribute("product", productFormDto); // 모델에 상품 정보 추가
        }

        model.addAttribute("management", management);
        model.addAttribute("productSearchDto", productSearchDto);
        model.addAttribute("maxPage", 5);

        return "product/productManagement";
    }

    // 상품 삭제 처리
    @PostMapping("/admin/product/delete/{prId}")
    public String deleteProduct(@PathVariable("prId") Long prId, Model model) {
        try {
            productService.deleteProduct(prId);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "상품 삭제 중 오류가 발생했습니다.");
            return "redirect:/admin/product/management";
        }
        return "redirect:/admin/product/management";
    }

        // 상품 상세 정보 조회 (일반 사용자용)
        @GetMapping(value="/product/{prId}")
        public String productDtl(Model model, @PathVariable("prId") Long prId) {
            ProductFormDto productFormDto = productService.getProductDtl(prId); // 상품 정보 조회
            model.addAttribute("product", productFormDto); // 모델에 상품 정보 추가

    //        return "product/productDtl";
            return "product/productDetail";
        }

}
