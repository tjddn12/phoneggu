package com.jsbs.casemall.controller;

import com.jsbs.casemall.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;



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
