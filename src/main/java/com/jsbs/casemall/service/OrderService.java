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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {




    private final OrderRepository orderRepository;

    private final UserRepository userRepository;

    private final ProductRepository productRepository;


    @Transactional
    public OrderDto getOrder(Long prId,String id,int count) { // 나중에 수량도 받아와야함
         Product product = productRepository.findById(prId).orElseThrow(EntityNotFoundException::new);
        // 주문한 고객 찾기
        Users user = userRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        // 주문 상세 객체 생성
        OrderDetail orderDetail = OrderDetail.createOrderDetails(product, count); // 임의로 상품 수량은 1로 설정
        List<OrderDetail> orderItems = new ArrayList<>();
        orderItems.add(orderDetail);
        // 주문 정보 생성
        Order order = Order.createOrder(user, orderItems);

        orderRepository.save(order);

        return OrderDto.builder()
                .orderName(product.getPrName()) // 상품정보
                .orderID(order.getOrderId()) // 주문 아이디 생성한것을 반환
                .amount(orderDetail.getTotalPrice()) // 주문 총액은 상품 가격 * 수량
                .userName(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .build();
    }




    @Transactional
    public void updateOrderWithPaymentInfo(String orderId,String paymentMethod,String payInfo) {
        Order order = orderRepository.findByOrderId(orderId).orElseThrow(EntityNotFoundException::new);
        order.updatePaymentInfo(paymentMethod,payInfo);
        orderRepository.save(order);
    }


    public void failOrder(String orderId) {
        // 주문 실패시 임시 저장한 order객체를 삭제
        Order order = orderRepository.findByOrderId(orderId).orElseThrow(EntityNotFoundException::new);
        order.setOrderStatus(OrderStatus.CANCEL); // 주문 취소 상태로 변경
        orderRepository.save(order);

    }
}
