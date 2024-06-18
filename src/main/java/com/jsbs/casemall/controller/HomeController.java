package com.jsbs.casemall.controller;

import com.jsbs.casemall.entity.Product;
import com.jsbs.casemall.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final ProductService productService;

    @GetMapping("/")
    public String member(Model model) {
        List<Product> products = productService.getProducts(0, 10); // 처음에 10개의 상품만 로드
        model.addAttribute("indexProducts", products);
        return "index"; // index.html을 렌더링
    }

    @GetMapping("/api/indexProducts")
    @ResponseBody
    public List<Product> getMoreIndexProducts(@RequestParam(defaultValue = "0") int offset,
                                              @RequestParam(defaultValue = "5") int limit) {
        return productService.getProducts(offset, limit);
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
