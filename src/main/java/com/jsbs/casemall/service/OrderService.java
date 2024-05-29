package com.jsbs.casemall.service;


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
@Transactional(readOnly = true)
public class OrderService {



    private final OrderDetailRepository orderDetailRepository;

    private final OrderRepository orderRepository;

    private final UserRepository userRepository;

    private final ProductRepository productRepository;


    // 주문
    @Transactional
    // 임시로 해놈 나중에 dto 정보에 맞춰서 변경 예정 = dto 정보를 통채로 넘김
    public Long order(String userId, long prId, int count, String payInfo) {
        // 주문이 들어오면 해당 고객 + 주문하는 상품 개수 + 상세정보를 만들고  + 주문 테이블에 저장후
        // 주문 아이디를 반환

        // 주문한 고객 찾기
        Users user = userRepository.findById(userId).orElseThrow(EntityNotFoundException::new);

        // 무슨 상품인지 검색
        Product product = productRepository.findById(prId).orElseThrow(EntityNotFoundException::new);

        // 주문상세 만들기
        List<OrderDetail> orderList = new ArrayList<>();
        OrderDetail orders = OrderDetail.createOrderDetails(product, count);
        orderList.add(orders);
        // 주문 객체 만들어주기
        Order order = Order.createOrder(user, orderList, payInfo);

        // 작업이 끝나면 주문 정보를 DB에 저장 시킴
        orderRepository.save(order);

        // 주문 번호 반환
        return order.getId();
    }

    @Transactional(readOnly = true)
    public OrderDto getOrder(Long prId,String id) {
         Product product = productRepository.findById(prId).orElseThrow(EntityNotFoundException::new);
         Users user = userRepository.findById(id).orElseThrow(EntityNotFoundException::new);

        return OrderDto.builder()
//                .amount(product.getPrPrice())
                .amount(5) //바꿀거
                .prPrice(5000) //  바꿀거
                .orderID(user.getUserId())
                .orderName("테스트상품")
//                .orderName(product.getPrName())
                .userName(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .build();

    }




}
