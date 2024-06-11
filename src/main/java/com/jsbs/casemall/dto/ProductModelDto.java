package com.jsbs.casemall.dto;

import com.jsbs.casemall.constant.ProductModelSelect;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProductModelDto {
    private Long id;

    private ProductModelSelect productModelSelect;

    @NotNull(message = "재고는 필수 입력 값입니다.")
    @Positive(message = "재고는 양수이어야 합니다.")
    private Integer prStock = 0; // 기본값 0으로 설정
}
