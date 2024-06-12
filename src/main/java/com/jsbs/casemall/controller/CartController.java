package com.jsbs.casemall.controller;

import com.jsbs.casemall.dto.CartDto;
import com.jsbs.casemall.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;



    // 장바구니 /cart 시 장바구니
    @GetMapping
    public String viewCart(Model model, Principal principal) {
        String userId = principal.getName();
        CartDto cart = cartService.getCartByUserId(userId);
        // 만약 없다면 빈 객체 생성하여 전달
        if (cart == null) {
            cart = new CartDto();
        }
        model.addAttribute("cart", cart);
        return "cart/cart";
    }


    @PostMapping("/add")
    public String addToCart(@RequestParam Long prId, CartDto dto , Principal principal,Model model) {
        String userId = principal.getName();
        try {
            cartService.addItemToCart(dto,userId,prId);
            return "redirect:/cart"; // 장바구니 페이지로 리다이렉션
        } catch (Exception e) {
            log.error("Error adding item to cart", e);
            model.addAttribute("error", "Failed to add item to cart");
            return "/error";
        }
    }






    // 카트 아이템 삭제 메소드
    @PostMapping("/remove")
    public String removeItemFromCart(@RequestParam Long cartItemId, Principal principal, Model model) {
        String userId = principal.getName();
        try {
            cartService.removeItemFromCart(userId, cartItemId);
            return "redirect:/cart"; // 장바구니 페이지로 리다이렉션
        } catch (Exception e) {
            log.error("Error removing item from cart", e);
            model.addAttribute("error", "Failed to remove item from cart");
            return "error";
        }
    }

    @PostMapping("/clear")
    public String clearCart(Principal principal, Model model) {
        String userId = principal.getName();
        try {
            cartService.clearCart(userId);
            return "redirect:/cart"; // 장바구니 페이지로 리다이렉션
        } catch (Exception e) {
            log.error("Error clearing cart", e);
            model.addAttribute("error", "Failed to clear cart");
            return "error";
        }
    }
}
