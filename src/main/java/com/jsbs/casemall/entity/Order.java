package com.jsbs.casemall.entity;

import com.jsbs.casemall.constant.OrderStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Setter
@Table(name = "order")
public class Order {
    // 주문 저장 테이블

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_no") // 주문번호
    private Long id;


    @Column(name = "pr_id")
    private String prId; // 주문한 상품 아이디


    @Column(name = "pr_count")
    private String prCount; // 상품 수량


    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users users; // 주문한 고객 아이디


    @Column(name = "pr_price")
    private int prPrice; // 상품의 가격


    @Column(name = "order_date")
    private LocalDateTime localDateTime; // 주문일


    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus; // 주문상태 저장


    @Column(name = "payInfo")
    private  String payInfo; // 결제 수단

    // 주문 목록
    @OneToMany(mappedBy = "order" ,cascade=CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderDetail> orderItems = new ArrayList<>();


    // 양방향
    public  void addOrderItem(OrderDetail orderDetail){
        orderItems.add(orderDetail); // 주문 목록을 주문에 추가
        orderDetail.setOrder(this); // 주문 항목의 주문을 현재 주문으로 설정
    }



    public  static  Order createOrder(Users member, List<OrderDetail> orderItems){
        Order order = new Order();
        // 주문아이템이 한개가 아닐수도 있기때문에 for문으로 집어넣기
        for(OrderDetail item : orderItems){
            order.addOrderItem(item);
        }

        order.setOrderStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());

        return  order;
    }





}
