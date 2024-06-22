package com.jsbs.casemall.dto;

import com.jsbs.casemall.constant.ProductModelSelect;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductModelDto {
    private Long id;

    private ProductModelSelect productModelSelect;

    @PositiveOrZero(message = "재고는 0 또는 양수이어야 합니다.")
    private Integer prStock = 0;

}
