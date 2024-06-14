package com.jsbs.casemall.dto;


import com.jsbs.casemall.entity.Cart;
import com.jsbs.casemall.entity.CartItem;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class CartDto {
    private String userId;
    // form 에서 받을 데이터
    private List<CartItemDto> items = new ArrayList<>();
    // 총  결제 금액
    private long totalPrice;

    // 디비에서  가져올때
    public CartDto(Cart cart) {

        this.userId = cart.getUser().getUserId();
        this.items = new ArrayList<>();

        for (CartItem cartItem : cart.getCartItems()) {
            this.items.add(new CartItemDto(cartItem));
        }
        this.totalPrice = cart.getTotalPrice();
    }
}
