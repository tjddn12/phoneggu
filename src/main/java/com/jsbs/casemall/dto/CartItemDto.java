package com.jsbs.casemall.dto;

import com.jsbs.casemall.entity.CartItem;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemDto {
    private Long cartItemId;
    private Long productId; // 제품 고유아이디
    private String productName; // 제품이름
    private String productModel; // 기종
    private int count; // 개수
    private int price; // 가격

    public CartItemDto(CartItem cartItem) {
        this.cartItemId = cartItem.getId();
        this.productId = cartItem.getProduct().getId();
        this.productName = cartItem.getProduct().getPrName();
        // 기종 이름 가져오기
//        this.productModel = cartItem.getProductModel().getProductModelSelect().getDisplayName();
        this.count = cartItem.getCount();
        this.price = cartItem.getProduct().getPrPrice();
    }
}