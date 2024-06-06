package com.jsbs.casemall.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String member() {
        return "index";
    }

//   각 사이트  테스트용
    @GetMapping("/case")
    public String test(){return "product/case";}



    @GetMapping("/myPage")
    String test2(){return "user/myPage";}


    @GetMapping("/productDtl")
    String test3(){return "product/productDetail";}


    @GetMapping("/cart/test")
    public String tset222(){
        return "cart/cart";
    }
    @GetMapping("/orderTest")
    String orderTest(){
        return "/pay/payment";
    }

}
