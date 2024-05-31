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


    // 주문
//    @Transactional
//    // 임시로 해놈 나중에 dto 정보에 맞춰서 변경 예정 = dto 정보를 통채로 넘김
//    public Long order(String userId, long prId, int count, String payInfo) {
//        // 주문이 들어오면 해당 고객 + 주문하는 상품 개수 + 상세정보를 만들고  + 주문 테이블에 저장후
//        // 주문 아이디를 반환
//
//        // 주문한 고객 찾기
//        Users user = userRepository.findById(userId).orElseThrow(EntityNotFoundException::new);
//
//        // 무슨 상품인지 검색
//        Product product = productRepository.findById(prId).orElseThrow(EntityNotFoundException::new);
//
//        // 주문상세 만들기
//        List<OrderDetail> orderList = new ArrayList<>();
//        OrderDetail orders = OrderDetail.createOrderDetails(product, count);
//        orderList.add(orders);
//        // 주문 객체 만들어주기
//        Order order = Order.createOrder(user, orderList, payInfo);
//
//        // 작업이 끝나면 주문 정보를 DB에 저장 시킴
//        orderRepository.save(order);
//
//        // 주문 번호 반환
//        return order.getId();
//    }

//    @Transactional(readOnly = true)
    @Transactional
    public OrderDto getOrder(Long prId,String id) { // 나중에 수량도 받아와야함
         Product product = productRepository.findById(prId).orElseThrow(EntityNotFoundException::new);
        // 주문한 고객 찾기
        Users user = userRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        // 주문 상세 객체 생성
        OrderDetail orderDetail = OrderDetail.createOrderDetails(product, 1); // 임의로 상품 수량은 1로 설정
        List<OrderDetail> orderItems = new ArrayList<>();
        orderItems.add(orderDetail);
        // 주문 정보 생성
        Order order = Order.createOrder(user, orderItems, "카드 결제"); // 결제 정보는 임의로 설정

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
    public void updateOrderWithPaymentInfo(String orderId,String paymentMethod) {
        Order order = orderRepository.findByOrderId(orderId).orElseThrow(EntityNotFoundException::new);
        order.updatePaymentInfo(paymentMethod);
        orderRepository.save(order);
    }


    public void failOrder(String orderId) {
        // 주문 실패시 임시 저장한 order객체를 삭제
        Order order = orderRepository.findByOrderId(orderId).orElseThrow(EntityNotFoundException::new);
        order.setOrderStatus(OrderStatus.CANCEL); // 주문 취소 상태로 변경
        orderRepository.save(order);

    }
}
