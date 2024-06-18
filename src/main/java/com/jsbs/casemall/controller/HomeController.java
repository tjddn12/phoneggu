package com.jsbs.casemall.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String member() {
        return "index";
    }



//   각 사이트  테스트용 (없는것도 있음 예- 각 컨트롤러에 매핑한것도 있음)
    @GetMapping("/case")
    public String test(){return "product/productList";}
    // 마이페이지
    @GetMapping("/test/myPage")
    String test2(){return "user/myPage";}

    // 상품 상세
    @GetMapping("/productDtl")
    String test3(){return "product/productDetail";}

    // 장바구니
    @GetMapping("/test/cart")
    public String tset222(){
        return "cart/cart";
    }
    // 결제
    @GetMapping("/orderTest")
    String orderTest(){
        return "order/orderPayment";
    }

    // 주문시 비로그인 일때 나오는 페이지
    @GetMapping("/test/login")
    String test4(){
        return "user/orderLogin";
    }
    // 로그인
    @GetMapping("/test/login2")
    String test5(){
        return "user/userLogin";
    }
    // 회원정보 수정
    @GetMapping("/test/edit")
    String test7(){
        return "user/userEdit";
    }

    // 결제 성공페이지
    @GetMapping("/test/success")
    String test6(){
        return "order/orderComplete";
    }
    // 주문내역
    @GetMapping("/test/history")
    String test8(){
        return "order/orderHistory";
    }

}
