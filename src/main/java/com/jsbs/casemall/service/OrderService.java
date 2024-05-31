package com.jsbs.casemall.service;


import com.jsbs.casemall.constant.OrderStatus;
import com.jsbs.casemall.dto.OrderDto;
import com.jsbs.casemall.entity.Order;
import com.jsbs.casemall.entity.OrderDetail;
import com.jsbs.casemall.entity.Product;
import com.jsbs.casemall.entity.Users;
import com.jsbs.casemall.repository.OrderDetailRepository;
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

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {




    private final OrderRepository orderRepository;

    private final UserRepository userRepository;

    private final ProductRepository productRepository;


    public boolean validatePayment(String orderId, int amount) {
        try {
            // 주문을 조회합니다.
            Order order = orderRepository.findByOrderId(orderId).orElse(null);

            // 주문이 존재하지 않을 경우 예외 발생
            if (order == null) {
                throw new IllegalArgumentException("주문 정보를 찾을수 없습니다");
            }

            // 주문 상세 항목의 가격을 합산합니다.
            int price = 0;
            for (OrderDetail orderDetail : order.getOrderItems()) {
                price += orderDetail.getOrderPrice();
            }

            // 결제 금액이 확인
            if (price == amount) {
                return true;
            } else {
                throw new IllegalArgumentException("결제 금액이 올바르지 않습니다");
            }
        } catch (IllegalArgumentException e) {
            // 비즈니스 로직에서 발생한 예외를 처리합니다.
            log.error("validatePayment 에서 발생 : {} ",e.getMessage());
            return false;
        } catch (Exception e) {
            // 기타 예외를 처리합니다.
          log.error("Unexpected error during payment validation: {} " ,e.getMessage());
            return false;
        }
    }









    // 주문 정보 생성과 반환하는 메소드
    @Transactional
    public OrderDto getOrder(Long prId, String id, int count) {
        try {
            Product product = productRepository.findById(prId).orElseThrow(() -> new EntityNotFoundException("해당 제품을 찾을 수 없습니다 "));
            Users user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("회원 정보를 찾을 수 없습니다"));

            // 주문 상세 객체 생성
            OrderDetail orderDetail = OrderDetail.createOrderDetails(product, count);
            List<OrderDetail> orderItems = new ArrayList<>();
            orderItems.add(orderDetail);

            // 주문 정보 생성
            Order order = Order.createOrder(user, orderItems);
            orderRepository.save(order);

            return OrderDto.builder()
                    .orderName(product.getPrName())
                    .orderID(order.getOrderId())
                    .amount(orderDetail.getTotalPrice())
                    .userName(user.getName())
                    .email(user.getEmail())
                    .phone(user.getPhone())
                    .build();
        } catch (EntityNotFoundException e) {
            log.error("getOrder 에서 발생: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error during order creation: {}", e.getMessage());
            throw new RuntimeException("Order creation failed", e);
        }
    }

    @Transactional
    public void updateOrderWithPaymentInfo(String orderId, String paymentMethod, String payInfo) {
        try {
            Order order = orderRepository.findByOrderId(orderId).orElseThrow(() -> new EntityNotFoundException("주문정보를 찾을수 없습니다"));
            order.updatePaymentInfo(paymentMethod, payInfo);
            orderRepository.save(order);
        } catch (EntityNotFoundException e) {
            log.error("updateOrderWithPaymentInfo 에서 발생: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error during updating order with payment info: {}", e.getMessage());
            throw new RuntimeException("Updating order with payment info failed", e);
        }
    }

    public void failOrder(String orderId) {
        try {
            Order order = orderRepository.findByOrderId(orderId).orElseThrow(() -> new EntityNotFoundException("주문정보를 찾을수 없습니다"));
            order.setOrderStatus(OrderStatus.CANCEL);
            orderRepository.save(order);
        } catch (EntityNotFoundException e) {
            log.error("failOrder 에서 발생: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error during order cancellation: {}", e.getMessage());
            throw new RuntimeException("Order cancellation failed", e);
        }
    }
}