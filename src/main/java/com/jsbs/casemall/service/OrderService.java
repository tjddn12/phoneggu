package com.jsbs.casemall.service;

import com.jsbs.casemall.constant.OrderStatus;
import com.jsbs.casemall.dto.OrderDto;
import com.jsbs.casemall.entity.Order;
import com.jsbs.casemall.entity.OrderDetail;
import com.jsbs.casemall.entity.Product;
import com.jsbs.casemall.entity.Users;
import com.jsbs.casemall.exception.OutOfStockException;
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
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CartService cartService;

    public OrderDto checkout(String userId) {
        return cartService.checkoutCart(userId);
    }

    @Transactional(readOnly = true)
    public boolean validatePayment(String orderId, int amount) {
        try {
            // 주문을 조회합니다.
            Order order = orderRepository.findByOrderId(orderId).orElse(null);

            // 주문이 존재하지 않을 경우 예외 발생
            if (order == null) {
                throw new IllegalArgumentException("주문 정보를 찾을 수 없습니다.");
            }

            // 주문 상세 항목의 가격을 합산합니다.
            int price = 0;
            for (OrderDetail orderDetail : order.getOrderItems()) {
                price += orderDetail.getTotalPrice();
            }
//            log.info("졀제 금액 {} ", price);

            // 결제 금액 확인
            if (price == amount) {
                return true;
            } else {
                throw new IllegalArgumentException("결제 금액이 올바르지 않습니다.");
            }
        } catch (IllegalArgumentException e) {
            log.error("validatePayment 에서 발생: {}", e.getMessage());
            return false;
        } catch (Exception e) {
            // 기타 예외 처리
            log.error("Unexpected error during payment validation: {}", e.getMessage());
            return false;
        }
    }

    // 주문 정보 생성과 반환하는 메소드
    public OrderDto getOrder(Long prId, String id, int count,boolean fromCart) {
            Users user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("해당 유저를 찾을수 없습니다"));
        if (fromCart) {
            // 장바구니에서 결제할 때
            return cartService.checkoutCart(id);
        } else {
            // 단일 상품 상세 페이지에서 결제할 때
            List<OrderDetail> orderDetails = new ArrayList<>();
            int totalAmount = 0;

            Product product = productRepository.findById(prId).orElseThrow(() -> new EntityNotFoundException("Product not found"));

            try {
                product.removeStock(count); // 재고 감소
            } catch (OutOfStockException e) {
                throw new IllegalArgumentException("재고가 부족합니다: " + product.getPrName(), e);
            }
            productRepository.save(product);

            OrderDetail orderDetail = OrderDetail.createOrderDetails(product, count);
            orderDetails.add(orderDetail);
            totalAmount += orderDetail.getTotalPrice();

            Order order = Order.createOrder(user, orderDetails);
            orderRepository.save(order);

            return OrderDto.builder()
                    .orderName(product.getPrName())
                    .orderID(order.getOrderId())
                    .amount(totalAmount)
                    .userName(user.getName())
                    .email(user.getEmail())
                    .phone(user.getPhone())
                    .build();
        }
    }

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
            // 실패시 해당 주문아이디로 주문을 찾고 상태를 캔슬로 변경 > 재고 다시 원상복구
            Order order = orderRepository.findByOrderId(orderId).orElseThrow(() -> new EntityNotFoundException("주문정보를 찾을수 없습니다"));
            order.setOrderStatus(OrderStatus.CANCEL);

            for(OrderDetail orderDetail : order.getOrderItems()){
                Product product = orderDetail.getProduct();
                product.addStock(orderDetail.getCount()); // 다시 추가
//                productRepository.save(product);
            }

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
