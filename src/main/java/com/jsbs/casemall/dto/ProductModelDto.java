package com.jsbs.casemall.dto;

import com.jsbs.casemall.constant.ProductModelSelect;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductModelDto {
    private Long id;

    private ProductModelSelect productModelSelect;

    @Positive(message = "재고는 양수이어야 합니다.")
    private Integer prStock;
}
