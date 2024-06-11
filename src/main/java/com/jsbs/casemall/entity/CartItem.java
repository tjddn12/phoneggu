package com.jsbs.casemall.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Entity
@Setter
@Table(name = "cart_list")
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 다른곳에서 new 생성자를 막기 위해
// 장바구니 상품리스트
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pr_id")
    private Product product;


    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="cart_id")
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "model_id")
    private ProductModel productModel;

    private int count;

    // 정적 팩토리 메서드로 카트 아이템 생성
    public static CartItem createCartItem(Product product, ProductModel productModel, int count) {
        CartItem cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setProductModel(productModel);
        cartItem.setCount(count);
        return cartItem;
    }

    public void addCount(int count) {
        this.count += count;
    }
}
