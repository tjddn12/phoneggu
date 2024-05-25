package com.jsbs.casemall.dto;

import com.jsbs.casemall.constant.ProductSellStatus;
import com.jsbs.casemall.entity.Product;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ProductFormDto {
    private Long id;

    @NotBlank(message = "상품명은 필수 입력 값입니다.")
    private String prName;

    @NotNull(message = "카테고리는 필수 선택입니다.")
    private Long categoriesId;

    @NotNull(message = "가격은 필수 입력 값입니다.")
    private Long prPrice;

    @NotBlank(message = "상품 설명는 필수 입력 값입니다.")
    private String prDetail;

    @NotNull(message = "재고는 필수 입력 값입니다.")
    private Integer prStock;

    private ProductSellStatus productSellStatus;
    private List<ProductImgDto> prImgDtoList = new ArrayList<>();
    private List<Long> prImgIds = new ArrayList<>();

    private static ModelMapper modelMapper = new ModelMapper();
    public Product createProduct(){
        return modelMapper.map(this, Product.class);
    }
    //ItemFormDto => Item

    public static ProductFormDto of(Product product) {
        return modelMapper.map(product, ProductFormDto.class);
    }
    // Item =>ItemFormDto
}