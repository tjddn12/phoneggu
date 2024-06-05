package com.jsbs.casemall.dto;

import com.jsbs.casemall.entity.CartItem;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemDto {
    private Long cartItemId;
    private Long productId;
    private String productName;
    private int count;
    private int price;

    public CartItemDto(CartItem cartItem) {
        this.cartItemId = cartItem.getId();
        this.productId = cartItem.getProduct().getId();
        this.productName = cartItem.getProduct().getPrName();
        this.count = cartItem.getCount();
        this.price = cartItem.getProduct().getPrPrice();
    }
}