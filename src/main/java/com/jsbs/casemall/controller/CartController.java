//package com.jsbs.casemall.controller;
//
//import com.jsbs.casemall.dto.CartDto;
//import com.jsbs.casemall.service.CartService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.log4j.Log4j2;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//import java.security.Principal;
//
//@Log4j2
//@Controller
//@RequiredArgsConstructor
//@RequestMapping("/cart") // myPage cart 가아닐까
//public class CartController {
//
//    private final CartService cartService;
//
//
//
//    // 장바구니 /cart 시 장바구니
//    @GetMapping
//    public String getCart(Model model, Principal principal) {
//        String userId = principal.getName();
//        CartDto cartDto = cartService.getCartByUserId(userId);
//        model.addAttribute("cart", cartDto);
//        return "cart/cartList";
//    }
//
//
//    @PostMapping("/add")
//    public String addItemToCart(@RequestParam Long prId, @RequestParam int count, Principal principal, Model model) {
//        String userId = principal.getName();
//        try {
//            cartService.addItemToCart(userId, prId, count);
//            return "redirect:/cart"; // 장바구니 페이지로 리다이렉션
//        } catch (Exception e) {
//            log.error("Error adding item to cart", e);
//            model.addAttribute("error", "Failed to add item to cart");
//            return "error";
//        }
//    }
//
//    // 카트 아이템 삭제 메소드
//    @PostMapping("/remove")
//    public String removeItemFromCart(@RequestParam Long cartId, Principal principal, Model model) {
//        String userId = principal.getName();
//        try {
//            cartService.removeItemFromCart(userId, cartId);
//            return "redirect:/cart"; // 장바구니 페이지로 리다이렉션
//        } catch (Exception e) {
//            log.error("Error removing item from cart", e);
//            model.addAttribute("error", "Failed to remove item from cart");
//            return "error";
//        }
//    }
//
//    @PostMapping("/clear")
//    public String clearCart(Principal principal, Model model) {
//        String userId = principal.getName();
//        try {
//            cartService.clearCart(userId);
//            return "redirect:/cart"; // 장바구니 페이지로 리다이렉션
//        } catch (Exception e) {
//            log.error("Error clearing cart", e);
//            model.addAttribute("error", "Failed to clear cart");
//            return "error";
//        }
//    }
//}
