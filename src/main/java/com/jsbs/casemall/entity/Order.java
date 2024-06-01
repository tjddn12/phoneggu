package com.jsbs.casemall.entity;

import com.jsbs.casemall.constant.OrderStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Entity
@Setter
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 다른곳에서 new 생성자를 막기 위해
public class Order {
    // 주문 저장 테이블

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_no") // 주문번호
    private Long id;


    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private Users users; // 주문한 고객 아이디


    @Column(name = "order_date",nullable = false)
    private LocalDateTime orderDate; // 주문일


    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus; // 주문상태 저장


    @Column(name = "payInfo")
    private  String payInfo; // 결제 수단

    @Column(name = "payment_method")
    private String paymentMethod; // 결제 수단


    @Column(name = "order_id", nullable = false, unique = true)
    private String orderId; // 주문 고유 ID


    // 주문 목록
    @OneToMany(mappedBy = "order" ,cascade=CascadeType.ALL,orphanRemoval = true,fetch = FetchType.LAZY)
    private List<OrderDetail> orderItems = new ArrayList<>();


    // 양방향
    public  void addOrderItem(OrderDetail orderDetail){
        orderItems.add(orderDetail); // 주문 목록을 주문에 추가
        orderDetail.setOrder(this); // 주문 항목의 주문을 현재 주문으로 설정
    }


    // 주문 테이블 생성
    public static Order createOrder(Users user, List<OrderDetail> orderItems) {
        Order order = new Order();
        order.setUsers(user);

        // 주문아이템이 한개가 아닐수도 있기때문에 for문으로 집어넣기 => 장바구니에서 주문시
        for (OrderDetail item : orderItems) {
            order.addOrderItem(item);
        }

        order.setOrderStatus(OrderStatus.STAY); // 주문 상태
        order.setOrderDate(LocalDateTime.now()); // 결제 날짜
        order.setOrderId(UUID.randomUUID().toString()); // 고유한 주문 ID 생성

        return order;
    }

    public void updatePaymentInfo(String paymentMethod,String payInfo) {
        this.orderStatus = OrderStatus.ORDER; // 성공시 결제 상태를 변경
        this.paymentMethod = paymentMethod; // 결제 수단을 업데이트
        this.payInfo = payInfo;//결제 방식을 저장
    }






}
