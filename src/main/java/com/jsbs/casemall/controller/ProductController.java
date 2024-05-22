package com.jsbs.casemall.controller;

import com.jsbs.casemall.dto.ProductFormDto;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProductController {

    @GetMapping("/admin/product/new")
    public String productForm(Module module){
        module.addAttribute("productFormDto", new ProductFormDto());
        return "/product/productForm";
    }
}
