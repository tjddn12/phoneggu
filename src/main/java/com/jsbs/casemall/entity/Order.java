package com.jsbs.casemall.entity;

import com.jsbs.casemall.constant.OrderStatus;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "order")
public class Order {
    // 주문 저장 테이블

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_no") // 주문번호
    private Long id;


    // 주문아이템으로 빠지는데 여기 있어야하나?
    @Column(name = "pr_id") // 참조할거임
    private String prId; // 상품 아이디 = 상품이름


    @Column(name = "pr_count")
    private String prCount; // 상품 수량


    @ManyToOne
    @JoinColumn(name = "userId")
    private Member member; // 주문한 고객 아이디


    // 바로 주문시
    @Column(name = "pr_price")
    private int prPrice; // 상품의 가격


    @Column(name = "order_date")
    private LocalDateTime localDateTime; // 주문일


    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus; // 주문상태 저장




    @OneToMany(mappedBy = "order" , cascade=CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderDetail> itemList = new ArrayList<>();










}
