package com.jsbs.casemall.dto;

import com.jsbs.casemall.constant.ProductSell;
import com.jsbs.casemall.entity.Product;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ProductFormDto {
    private Long prNo;

    @NotBlank(message = "상품명은 필수 입력 값입니다.")
    private String prName;

    @NotNull(message = "가격은 필수 입력 값입니다.")
    private Integer prPrice;

    @NotBlank(message = "상품 설명는 필수 입력 값입니다.")
    private String pr;

    @NotNull(message = "재고는 필수 입력 값입니다.")
    private Integer stockNumber;

    private ProductSell productsSell;
    private List<ProductImgDto> ProductImgDtoList = new ArrayList<>();
    private List<Long> itemImgIds = new ArrayList<>();

    private static ModelMapper modelMapper = new ModelMapper();
    public Product createItem(){
        return modelMapper.map(this, Product.class);
    }
    //ItemFormDto => Item

    public static ProductFormDto of(Product product_obj){
        return modelMapper.map(product_obj, ProductFormDto.class);
    }
}
