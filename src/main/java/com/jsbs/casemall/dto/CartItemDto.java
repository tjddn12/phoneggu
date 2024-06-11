package com.jsbs.casemall.dto;

import com.jsbs.casemall.entity.CartItem;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemDto {
    private Long cartItemId;
    private Long modelId;
    private String productName; // 제품이름
    private int count; // 개수
    private int price; // 가격

    public CartItemDto(CartItem cartItem) {
        this.cartItemId = cartItem.getId();
        this.productName = cartItem.getProduct().getPrName();
        // 기종 이름 가져오기
        this.modelId =  cartItem.getProductModel().getId();
        this.count = cartItem.getCount();
        this.price = cartItem.getProduct().getPrPrice();
    }
}