package com.jsbs.casemall.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "order_detail")
public class OrderDetail {
    // 주문상세
    // 하나의 주문에는 여러개의 주문아이템이 존재 상품 하나의 여러개 주문아이템 존재


    @Id
    @GeneratedValue
    @Column(name="order_item_id")
    private Long id;



    /*
        @ManyToOne(fetch=FetchType.LAZY) //지연로딩
        @JoinColumn(name="pr_no")
        private Product product;
    */

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="order_no")
    private Order order;


    private int orderPrice; //주문가격


    private int count; //주문수량




    public int getTotalPrice() {
        return orderPrice * count;
    }
    // 주문할 상품과 주문수량을 통해 orderItem 객체를 만드는 메소드 작성

//    public  void  cancel(){
//        this.getItem().addStock(count);
//    }








}
