package com.jsbs.casemall.controller;

import com.jsbs.casemall.dto.ProductFormDto;
import com.jsbs.casemall.dto.ProductSearchDto;
import com.jsbs.casemall.entity.Product;
import com.jsbs.casemall.service.ProductService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/admin/product/new")
    public String productForm(Model model){
        model.addAttribute("productFormDto", new ProductFormDto());
        return "/product/productForm";
    }

    @PostMapping(value="/admin/product/new")
    public String productNew(@Valid ProductFormDto productFormDto, BindingResult bindingResult,
                          Model model, @RequestParam("productImgFile") List<MultipartFile> productImgFileList){
        if(bindingResult.hasErrors()){
            return "product/productForm"; //상품등록시 필수값이 없다면
        }
        if(productImgFileList.get(0).isEmpty() && productFormDto.getId() == null) {
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값 입니다.");
            return "product/productForm";
        }
        try{
            productService.saveProduct(productFormDto, productImgFileList);
        }catch(Exception e){
            model.addAttribute("errorMessage", "상품 등록 중 에러가 발생하였습니다.");
            return "product/productForm";
        }
        return "redirect:/"; //상품 등록이 완료되면 첫페이지 이동
    }

    @GetMapping(value = "/admin/product/{productId}")
    public String productDtl(@PathVariable("productId") Long productId, Model model){
        try{
            ProductFormDto productFormDto = productService.getProductDtl(productId);
            model.addAttribute("productFormDto",productFormDto);
        }catch(EntityNotFoundException e){
            model.addAttribute("errorMessage", "존재하지 않는 상품입니다.");
            model.addAttribute("productFormDto", new ProductFormDto());
            return "product/productForm";
        }

        return "product/productForm";
    }

    @PostMapping(value = "/admin/product/{productId}")
    public String productUpdate(@Valid ProductFormDto productFormDto, BindingResult bindingResult,
                             Model model, @RequestParam("productImgFile") List<MultipartFile> productImgFileList){
        if(bindingResult.hasErrors()){
            return "product/productForm";
        }
        if(productImgFileList.get(0).isEmpty() && productFormDto.getId() == null) {
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값 입니다.");
            return "product/productForm";
        }
        try{
            productService.updateProduct(productFormDto, productImgFileList);
        }catch(Exception e){
            model.addAttribute("errorMessage", "상품 수정 중 에러가 발생하였습니다.");
            return "product/productForm";
        }
        return "redirect:/";
    }

    @GetMapping(value = {"/admin/product/management", "/admin/product/management/{page}"})
    public String productManage(ProductSearchDto productSearchDto,
                                @PathVariable("page") Optional<Integer> page, Model model){
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 5);
        // pageable(페이지 처리정보) 객체를 생성 PageRequest.of->페이지 번호(page 있으면 경우사용, 없으면 0) page당3개
        Page<Product> management = productService.getAdminProductPage(productSearchDto, pageable);
        //상품목록 조회 productSearchDto(검색조건), 페이징 처리된 상품 목록을 Page<Product> 객체 형태로 반환합니다.
        model.addAttribute("management", management);
        model.addAttribute("productSearchDto", productSearchDto);
        model.addAttribute("maxPage", 5);
        //최대 페이지 번호를 5로 설정

        return "product/productManagement";
    }

    @GetMapping(value="/product/{productId}")
    public String productDtl(Model model, @PathVariable("productId") Long productId){
        ProductFormDto productFormDto = productService.getProductDtl(productId);
        model.addAttribute("product", productFormDto);

        return "product/productDtl";
    }
}
