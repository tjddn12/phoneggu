package com.jsbs.casemall.entity;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "order_detail")
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 다른곳에서 new 생성자를 막기 위해
public class OrderDetail {
    // 주문상세
    // 하나의 주문에는 여러개의 주문아이템이 존재 상품 하나의 여러개 주문아이템 존재
    @Id
    @GeneratedValue
    @Column(name="order_item_id")
    private Long id; // 고유 번호

    // 상품 고유 번호 가져오기
    @ManyToOne(fetch=FetchType.LAZY) //지연로딩
    @JoinColumn(name="pr_no")
    private Product product; // 제품 아이디 조인


    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="order_no")
    private Order order; // 주문번호


    private Long orderPrice; //주문가격


    private int count; //주문수량

    public Long getTotalPrice() {
        return orderPrice * count;
    }


    // 주문상세 객체 만들고 객체를 리턴
    public static OrderDetail createOrderDetails(Product product, int count ){

        OrderDetail orders = new OrderDetail();
        orders.setProduct(product);
        orders.setCount(count);
        orders.setOrderPrice(product.getPrPrice());
        product.removeStock(count); // 해당 제품의 재고를 감소함

        return  orders;
    }








}
