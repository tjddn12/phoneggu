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

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="cart_id")
    private Cart cart;


    @ManyToOne
    @JoinColumn(name = "pr_id")
    Product product;


    int count; // 수량

    // CartItem 객체 생성 메서드
    public static CartItem createCartItem(Product product, int count) {
        CartItem cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setCount(count);
        return cartItem;
    }

}
