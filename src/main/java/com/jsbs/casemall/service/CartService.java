package com.jsbs.casemall.service;


import com.jsbs.casemall.dto.CartDto;
import com.jsbs.casemall.dto.CartItemDto;
import com.jsbs.casemall.entity.*;
import com.jsbs.casemall.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final ProductModelRepository productModelRepository;
    private final CartItemRepository cartItemRepository;


    @Transactional(readOnly = true)
    public CartDto getCartByUserId(String userId) {
        Users user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("해당 유저를 찾을수 없습니다"));
        Cart cart = cartRepository.findByUser(user);
        return cart != null ? new CartDto(cart) : null;
    }

    // 장바구니에 추가
    public void addItemToCart(CartDto cartDto,String userId,Long productId) {

        // 이용 회원과 해당 상품이 디비에 있는지 확인
        Users user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("해당 유저를 찾을수 없습니다"));
        Product product = productRepository.findById(productId).orElseThrow(() -> new EntityNotFoundException("제품을 찾을수 없습니다"));

        // 카트 객체 생성
        Cart cart = cartRepository.findByUser(user);
        if (cart == null) {
            cart = Cart.createCart(user); // 기존에 없다면 새로 생성
        }

        if (cartDto.getItems() == null || cartDto.getItems().isEmpty()) {
            throw new IllegalArgumentException("장바구니에 추가할 아이템이 없습니다.");
        }
        // 기종별로 처리
        for (CartItemDto itemDto : cartDto.getItems()) {
            ProductModel productModel = product.getProductModelList().stream()
                    .filter(findModel -> findModel.getId().equals(itemDto.getModelId()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("해당 모델을 찾을 수 없습니다."));

            // 기존 카트 아이템 찾기
            CartItem findItem = cart.getCartItems().stream()
                    .filter(cartItem -> cartItem.getProduct().equals(product) && cartItem.getProductModel().equals(productModel))
                    .findFirst()
                    .orElse(null);

            if (findItem != null) {
                // 기존 아이템이 있으면 수량 업데이트
                log.info("Updating existing cart item: cartItemId={}, newCount={}", findItem.getId(), findItem.getCount() + itemDto.getCount());
                findItem.addCount(itemDto.getCount());
            } else {
                // 새로운 아이템 추가
                CartItem cartItem = CartItem.createCartItem(product, productModel, itemDto.getCount());
                cart.addCartItems(cartItem);
                log.info("Adding new cart item: cartItemId={}, count={}", cartItem.getId(), cartItem.getCount());
            }
        }

        cartRepository.save(cart); // 최종적으로 카트에 저장
    }


//

    // 비우기 메소드
    public void clearCart(String orderId) {
        Order order = orderRepository.findByOrderId(orderId).orElseThrow(()-> new IllegalArgumentException("주문 정보가 없습니다!"));
        String userId = order.getUsers().getUserId();
        Users user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("해당 유저를 찾을수 없습니다"));
        Cart cart = cartRepository.findByUser(user);
        if (cart != null) {
            cart.getCartItems().clear();
            cartRepository.save(cart);
        }else {
            throw new IllegalArgumentException("장바구니가 없습니다");
        }
    }

    // 장바구니에서 삭제
    public void removeItemFromCart(String userId, Long cartItemId) {
        Users user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("해당 유저를 찾을 수 없습니다"));
        Cart cart = cartRepository.findByUser(user);
        // 장바구니가 비어있지 않으면
        if (cart != null) {
            CartItem removeCartItem = null; // 삭제하려는 아이템을 담을 변수
            for (CartItem cartItem : cart.getCartItems()) { // cart 안에 아이템을 순회하면서 찾기
                if (cartItem.getId().equals(cartItemId)) {
                    removeCartItem = cartItem; // 찾으면 빠져나옴
                    break;
                }
            }
            // 예외 처리 삭제하려는 카트아이템이 없다면
            if (removeCartItem == null) {
                throw new EntityNotFoundException("해당 상품을 찾을 수 없습니다");
            }

            cart.removeItems(removeCartItem);
            cartRepository.save(cart);
        }
    }

    // 업데이트

    public void updateCartItemQuantity(Long cartItemId, int count) {
        Optional<CartItem> findCartItem = cartItemRepository.findById(cartItemId);
        if (findCartItem.isPresent()) {
            CartItem cartItem = findCartItem.get();
            cartItem.updateCount(count);
            cartItemRepository.save(cartItem);
        }
    }


    public int getItemStock(Long cartItemId) {
        int stock = 0 ;
        CartItem item = cartItemRepository.findById(cartItemId).orElseThrow(()->new EntityNotFoundException("찾을 수 없습니다"));
        stock = item.getProductModel().getPrStock();
        return  stock;
    }
}
