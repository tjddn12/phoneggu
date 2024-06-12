package com.jsbs.casemall.service;


import com.jsbs.casemall.dto.CartDto;
import com.jsbs.casemall.dto.OrderDto;
import com.jsbs.casemall.entity.*;
import com.jsbs.casemall.exception.OutOfStockException;
import com.jsbs.casemall.repository.CartRepository;
import com.jsbs.casemall.repository.OrderRepository;
import com.jsbs.casemall.repository.ProductRepository;
import com.jsbs.casemall.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

//    private final CartRepository cartRepository;
//    private final ProductRepository productRepository;
//    private final UserRepository userRepository;
//    private final OrderRepository orderRepository;
//
//
//    @Transactional(readOnly = true)
//    public CartDto getCartByUserId(String userId) {
//        Users user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("해당 유저를 찾을수 없습니다"));
//        Cart cart = cartRepository.findByUser(user);
//        return cart != null ? new CartDto(cart) : null;
//    }
//
//    // 장바구니에 추가
//    public void addItemToCart(String userId, Long productId, int count) {
//        // 이용 회원과 해당 상품이 디비에 있는지 확인
//        Users user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("해당 유저를 찾을수 없습니다"));
//        Product product = productRepository.findById(productId).orElseThrow(() -> new EntityNotFoundException("제품을 찾을수 없습니다"));
//
//        // 카트 객체 생성
//        Cart cart = cartRepository.findByUser(user);
//
//        if (cart == null) {
//            cart = Cart.createCart(user);
//        } else { // 기존에 있다면 해당 제품에 수량을 추가하고 장바구니에 저장
//            for (CartItem cartItem : cart.getCartItems()) {
//                if (cartItem.getProduct().equals(product)) {
//                    cartItem.addCount(count); // 기존 수량에 추가
//                    cartRepository.save(cart);
//                    return;
//                }
//            }
//        }
//
//        CartItem cartItem = CartItem.createCartItem(product, count);
//        cart.addCartItems(cartItem);
//        cartRepository.save(cart);
//    }


        // 장바구니에서 주문 목록 처리
//    public OrderDto checkoutCart(String userId) {
//        Users user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("해당 유저를 찾을수 없습니다"));
//        Cart cart = cartRepository.findByUser(user);
//        if (cart == null || cart.getCartItems().isEmpty()) {
//            throw new IllegalArgumentException("Cart is empty");
//        }
//
//        List<OrderDetail> orderDetails = new ArrayList<>();
//        List<String> productNames = new ArrayList<>(); // 주문 상품 이름 목록
//        int totalAmount = 0; // 총 금액

//        for (CartItem cartItem : cart.getCartItems()) {
//            Product product = cartItem.getProduct();
//            if (product.getPrStock() < cartItem.getCount()) {
//                throw new OutOfStockException("재고가 부족합니다: " + product.getPrName());
//            }
//            product.removeStock(cartItem.getCount());
//            productRepository.save(product);

//            OrderDetail orderDetail = OrderDetail.createOrderDetails(product, cartItem.getCount());
//            orderDetails.add(orderDetail);
//            productNames.add(product.getPrName()); // 상품 이름 추가
//            totalAmount += orderDetail.getTotalPrice();
//        }

//        Order order = Order.createOrder(user, orderDetails);
//        orderRepository.save(order);
//
//        // 장바구니 비우기
//        cart.clearItems();
//        cartRepository.save(cart);
//
//        return OrderDto.builder()
//                .orderID(order.getOrderId())
//                .amount(totalAmount)
//                .userName(user.getName())
//                .email(user.getEmail())
//                .phone(user.getPhone())
//                .orderName(productNames) // 주문 상품 이름 목록 설정
//                .build();
//    }
//
//    // 비우기 메소드
//    public void clearCart(String userId) {
//        Users user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("해당 유저를 찾을수 없습니다"));
//        Cart cart = cartRepository.findByUser(user);
//        if (cart != null) {
//            cart.getCartItems().clear();
//            cartRepository.save(cart);
//        }else {
//            throw new IllegalArgumentException("장바구니가 없습니다");
//        }
//    }
//
//    // 장바구니에서 삭제
//    public void removeItemFromCart(String userId, Long cartItemId) {
//        Users user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("해당 유저를 찾을 수 없습니다"));
//        Cart cart = cartRepository.findByUser(user);
//        // 장바구니가 비어있지 않으면
//        if (cart != null) {
//            CartItem removeCartItem = null; // 삭제하려는 아이템을 담을 변수
//            for (CartItem cartItem : cart.getCartItems()) { // cart 안에 아이템을 순회하면서 찾기
//                if (cartItem.getId().equals(cartItemId)) {
//                    removeCartItem = cartItem; // 찾으면 빠져나옴
//                    break;
//                }
//            }
//            // 예외 처리 삭제하려는 카트아이템이 없다면
//            if (removeCartItem == null) {
//                throw new EntityNotFoundException("해당 상품을 찾을 수 없습니다");
//            }
//
//            cart.removeItems(removeCartItem);
//            cartRepository.save(cart);
//        }
//    }
}