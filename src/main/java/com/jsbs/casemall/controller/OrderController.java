package com.jsbs.casemall.controller;

import com.jsbs.casemall.dto.OrderDto;
import com.jsbs.casemall.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor

public class OrderController {

    private static final Logger log = LoggerFactory.getLogger(OrderController.class);
    private final OrderService orderService;


    // 각각 요청에따른 주문 페이지 보여주기
    @PostMapping("/order")
    public String processOrder(@RequestParam("type") String orderType, @RequestParam("cartItemIds") List<Long> cartItemIds, Model model,Principal principal) {
        // 장바구니에 저장되어있는 정보를 OrderDto 에 담아서 보여줌
        // 장바구니 type=cart &  체크된 것들만 카트 아이디받아서 처리
        // 바로 주문 주문
        String id = principal.getName();
        if(id==null){
            return "redirect:/login";
        }
        log.info("들어온 값 :{} " , cartItemIds);
        log.info("들어온 타입 :{} " , orderType);
        OrderDto order;
        if(orderType.equals("cart")) {
           // 장바구니에서 선택한 cartItem 를 가지고 Order 생성
            order = orderService.createOrder(id, cartItemIds);
            model.addAttribute("order", order);
        }


        return "order/orderPayment";
    }



}
