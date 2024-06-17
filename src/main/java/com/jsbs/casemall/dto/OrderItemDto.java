package com.jsbs.casemall.dto;

import com.jsbs.casemall.entity.CartItem;
import com.jsbs.casemall.entity.OrderDetail;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class OrderItemDto {


    private Long productId;
    private Long orderItemId; // 주문아이템들의 아이디
    private Long modelId; // 모델 아이디
    private String modelName;
    private String productName; // 제품이름
    private int count; // 개수
    private int price; // 가격
    private String imgUrl;

    public OrderItemDto(OrderDetail orderItem) {
        this.orderItemId = orderItem.getId();
        this.productName = orderItem.getProduct().getPrName();
        // 기종 이름 가져오기
        this.modelName = orderItem.getProductModel().getProductModelSelect().getDisplayName();
        this.productId = orderItem.getProduct().getId();
        this.modelId =  orderItem.getProductModel().getId();
        this.count = orderItem.getCount();
        this.price = orderItem.getProduct().getPrPrice();
        // 대표사진만 필요
        this.imgUrl = orderItem.getProduct().getProductImgList().get(0).getImgUrl();
    }
}
