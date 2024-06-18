package com.jsbs.casemall.controller;

import com.jsbs.casemall.dto.OrderDto;
import com.jsbs.casemall.service.OrderService;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private static final Logger log = LoggerFactory.getLogger(OrderController.class);
    private final OrderService orderService;

    @GetMapping
    public String order(Principal principal, Model model) {
        String userId = principal.getName();
        OrderDto order = orderService.getOrder(userId);
        model.addAttribute("order", order);
        return "order/orderPayment";
    }

    @PostMapping
    public String processOrder(@RequestParam("type") String orderType, @RequestParam("cartItemIds") List<Long> cartItemIds, Model model, Principal principal) {
        String userId = principal.getName();
        if (userId == null) {
            return "redirect:/login";
        }

        log.info("들어온 값 : {}", cartItemIds);
        log.info("들어온 타입 : {}", orderType);

        OrderDto order;
        if (orderType.equals("cart")) {
            order = orderService.createOrder(userId, cartItemIds);
            model.addAttribute("order", order);

        }
        return "redirect:order";
    }

    @PostMapping("/remove")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> removeOrderDetail(@RequestBody Map<String, Object> payload, Principal principal) {
        Long orderId = Long.valueOf(payload.get("orderId").toString());
        Long orderDetailId = Long.valueOf(payload.get("orderDetailId").toString());
        String userId = principal.getName();

        Map<String, Object> response = new HashMap<>();
        try {
            orderService.removeOrderDetail(orderId, orderDetailId, userId);
            response.put("success", true);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
        }
        return ResponseEntity.ok(response);
    }


    @PostMapping("/updateQuantity")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updateOrderItemQuantity(@RequestBody Map<String, Object> payload) {
        Long orderDetailId = Long.valueOf(payload.get("orderDetailId").toString());
        int newCount = Integer.parseInt(payload.get("count").toString());

        orderService.updateOrderItemQuantity(orderDetailId, newCount);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        return ResponseEntity.ok(response);
    }



    // 주문확인
    @GetMapping("/history")
    public String history(Model model, Principal principal) {
        String userId = principal.getName();
        List<OrderDto> order = orderService.history(userId);
        model.addAttribute("orders", order);
        return "order/orderHistory";
    }


}
