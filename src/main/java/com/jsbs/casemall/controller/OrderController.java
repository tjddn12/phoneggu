package com.jsbs.casemall.controller;

import com.jsbs.casemall.dto.CartDto;
import com.jsbs.casemall.dto.OrderDto;
import com.jsbs.casemall.dto.OrderItemDto;
import com.jsbs.casemall.entity.Order;
import com.jsbs.casemall.service.OrderService;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
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
    public String history(@RequestParam(value = "page", defaultValue = "0") int page,
                          Model model, Principal principal) {
        if (page < 0) {
            page = 0;
        }

        String userId = principal.getName();
        List<OrderDto> order = orderService.history(userId);
        Page<OrderDto> orderPage = orderService.orderPage(order, page);
        log.info("orderPage = {}", orderPage);
//        model.addAttribute("orders", order);
        model.addAttribute("orders", orderPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", orderPage.getTotalPages());
        model.addAttribute("maxPage", 5);
        return "order/orderHistory";
    }


    // 바로구매 처리
    @PostMapping("/now")
    public String orderNow(@RequestParam Long prId, CartDto dto , Principal principal,Model model) {
        log.info("바로구매로 넘어온 값 : 상품아이디 : {} , dto {}",prId,dto.toString());
        String id = principal.getName();

        OrderDto order = orderService.createOrderByNow(dto,prId,id);
        model.addAttribute("order", order);
        return "redirect:/order";
    }

    @GetMapping("/search")
    public String searchOrders(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            Model model
    ) {
//        log.info("{},{}",startDate,endDate);
        List<OrderDto> orders = orderService.findOrdersByDateRange(startDate, endDate);
        model.addAttribute("orders", orders);
        return "order/orderHistory";
    }

}
