package com.jsbs.casemall.controller;

import com.jsbs.casemall.service.OrderService;
import jakarta.validation.Valid;
import jakarta.validation.Validation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;


    @GetMapping("/test")
    public String test(Model model) {
        model.addAttribute("prId", 3);
        return "order/orderInfo"; // 뷰 이름을 반환
    }

//
//    @GetMapping("/order/{prId}/pay")
//    public String pay(Principal principal, @PathVariable(name = "prId") Long prId, @Valid OrderDto orderDto, Validation validation,
//                      Model model)  {
//
//        orderService.order();
//        model.addAttribute("id",id );
//
//
//        return "order/"+id;
//
//
//    }


}
