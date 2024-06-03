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

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;


    public CartDto getCartByUserId(String userId) {
        Users user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("해당 유저를 찾을수 없습니다"));
        Cart cart = cartRepository.findByUser(user);
        return new CartDto(cart);
    }


    public void addItemToCart(String userId, Long productId, int quantity) {
        // 이용 회원과 해당 상품이 디비에 있는지 확인
        Users user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("해당 유저를 찾을수 없습니다"));
        Product product = productRepository.findById(productId).orElseThrow(() -> new EntityNotFoundException("제품을 찾을수 없습니다"));

        // 카트 객체 생성
        Cart cart = cartRepository.findByUser(user);

        // 기존에 있는지 확인
        if (cart == null) {
            cart = Cart.createCart(user); // Cart 객체 생성 메서드 사용
        }
        // 장바구니상품 생성
        CartItem cartItem = CartItem.createCartItem(product, quantity);
        cart.addCartItems(cartItem);
        cartRepository.save(cart); // 장바구니에 임시 저장

    }

    public OrderDto checkoutCart(String userId) {
        Users user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("해당 유저를 찾을수 없습니다"));
        Cart cart = cartRepository.findByUser(user);
        if (cart == null || cart.getCartItems().isEmpty()) {
            throw new IllegalArgumentException("Cart is empty");
        }

        List<OrderDetail> orderDetails = new ArrayList<>();
        int totalAmount = 0; // 총 금액

        for (CartItem cartItem : cart.getCartItems()) {
            Product product = cartItem.getProduct();
            product.removeStock(cartItem.getQuantity());  // 재고 감소
            productRepository.save(product);
            OrderDetail orderDetail = OrderDetail.createOrderDetails(product, cartItem.getQuantity());
            orderDetails.add(orderDetail);
            totalAmount += orderDetail.getTotalPrice(); // 총 금액을 계산
        }

        Order order = Order.createOrder(user, orderDetails);
        orderRepository.save(order);

        // 장바구니 비우기
        cart.getCartItems().clear();
        cartRepository.save(cart);

        return OrderDto.builder()
                .orderID(order.getOrderId())
                .amount(totalAmount) // 총 금액 설정
                .userName(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .build();
    }


    public void clearCart(String userId) {
        Users user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("해당 유저를 찾을수 없습니다"));
        Cart cart = cartRepository.findByUser(user);
        if (cart != null) {
            cart.getCartItems().clear();
            cartRepository.save(cart);
        }
    }

    public void removeItemFromCart(String userId, Long cartItemId) {
        Users user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("해당 유저를 찾을 수 없습니다"));
        Cart cart = cartRepository.findByUser(user);
        if (cart != null) {
            CartItem cartItemToRemove = null;
            for (CartItem cartItem : cart.getCartItems()) {
                if (cartItem.getId().equals(cartItemId)) {
                    cartItemToRemove = cartItem;
                    break;
                }
            }

            if (cartItemToRemove == null) {
                throw new EntityNotFoundException("Cart item not found");
            }

            cart.removeItems(cartItemToRemove);
            cartRepository.save(cart);
        }
    }






}
