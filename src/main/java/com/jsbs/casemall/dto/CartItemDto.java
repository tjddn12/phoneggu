package com.jsbs.casemall.dto;

import com.jsbs.casemall.entity.CartItem;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class CartItemDto {
    private Long productId;
    private Long cartItemId; // 카트 번호
    private Long modelId; // 모델 아이디
    private String modelName;
    private String productName; // 제품이름
    private int count; // 개수
    private int price; // 가격
    private String imgUrl;

    public CartItemDto(CartItem cartItem) {
        this.cartItemId = cartItem.getId();
        this.productName = cartItem.getProduct().getPrName();
        // 기종 이름 가져오기

        this.modelName = cartItem.getProductModel().getProductModelSelect().getDisplayName();
        this.productId = cartItem.getProduct().getId();
        this.modelId =  cartItem.getProductModel().getId();
        this.count = cartItem.getCount();
        this.price = cartItem.getProduct().getPrPrice();
        // 대표사진만 필요
        this.imgUrl = cartItem.getProduct().getProductImgList().get(0).getImgUrl();
    }
}