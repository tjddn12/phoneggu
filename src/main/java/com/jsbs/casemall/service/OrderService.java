package com.jsbs.casemall.service;

import com.jsbs.casemall.constant.OrderStatus;
import com.jsbs.casemall.dto.OrderDto;
import com.jsbs.casemall.dto.OrderItemDto;
import com.jsbs.casemall.entity.*;
import com.jsbs.casemall.repository.CartItemRepository;
import com.jsbs.casemall.repository.OrderRepository;
import com.jsbs.casemall.repository.ProductRepository;
import com.jsbs.casemall.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CartItemRepository cartItemRepository;

    // 주문 생성
    public OrderDto createOrder(String userId, List<Long> itemIds) {
        // 로그인한 유저 찾기
        Users user = userRepository.findByUserId(userId).orElseThrow(() -> new EntityNotFoundException("해당 유저를 찾을 수 없습니다"));

        // 기존에 생성된 주문이 있는지 확인
        List<Order> existingOrders = orderRepository.findByUsersAndOrderStatus(user, OrderStatus.STAY);
        if (!existingOrders.isEmpty()) {
            // 기존 주문 반환
            Order existingOrder = existingOrders.get(0);
            List<OrderItemDto> orderItemDtos = existingOrder.getOrderItems().stream()
                    .map(OrderItemDto::new)
                    .collect(Collectors.toList());

            return OrderDto.builder()
                    .orderNo(existingOrder.getId())
                    .totalPrice(existingOrder.getOrderItems().stream().mapToInt(OrderDetail::getTotalPrice).sum())
                    .items(orderItemDtos)
                    .userName(user.getName())
                    .email(user.getEmail())
                    .phone(user.getPhone())
                    .build();
        }

        // 새로운 주문 생성
        List<OrderDetail> orderDetails = new ArrayList<>();
        int totalAmount = 0;

        // 체크한 제품 찾아서 OrderDetail 저장
        for (Long cartItemId : itemIds) {
            CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(() -> new IllegalArgumentException("찾는 아이템이 없습니다"));
            Product product = cartItem.getProduct();
            ProductModel productModel = cartItem.getProductModel();
            OrderDetail orderDetail = OrderDetail.createOrderDetails(product, productModel, cartItem.getCount());
            orderDetails.add(orderDetail);
            totalAmount += orderDetail.getTotalPrice();
        }

        Order order = Order.createOrder(user, orderDetails);
        orderRepository.save(order);
        List<OrderItemDto> orderItemDtos = orderDetails.stream()
                .map(OrderItemDto::new)
                .collect(Collectors.toList());

        return OrderDto.builder()
                .orderNo(order.getId())
                .totalPrice(totalAmount)
                .items(orderItemDtos)
                .userName(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .build();
    }
    // 주문에서 업데이트





}
