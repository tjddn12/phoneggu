package com.jsbs.casemall.dto;

import com.jsbs.casemall.constant.ProductCategory;
import com.jsbs.casemall.constant.ProductSellStatus;
import com.jsbs.casemall.constant.ProductType;
import com.jsbs.casemall.entity.Product;
import com.jsbs.casemall.entity.ProductModel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
public class ProductFormDto {
    private Long id;

    @NotBlank(message = "상품명은 필수 입력 값입니다.")
    private String prName;

    @NotNull(message = "가격은 필수 입력 값입니다.")
    @Positive(message = "가격은 양수이어야 합니다.")
    private Integer prPrice;

    @NotBlank(message = "상품 설명는 필수 입력 값입니다.")
    private String prDetail;

//    @NotNull(message = "재고는 필수 입력 값입니다.")
//    private Integer prStock;

    private ProductType productType;
    private ProductCategory productCategory;
    private ProductSellStatus productSellStatus;

    private List<ProductModelDto> productModelDtoList = new ArrayList<>();
//    private List<Long> productModelIds = new ArrayList<>();
    private List<ProductImgDto> productImgDtoList = new ArrayList<>(); // 이미지 리스트 추가
    private List<Long> productImgIds = new ArrayList<>(); // 이미지 ID 리스트 추가

    private static ModelMapper modelMapper = new ModelMapper();

    public Product createProduct(){
        return modelMapper.map(this, Product.class);

    }
    //ProductFormDto => Product

    public static ProductFormDto of(Product product) {
        return modelMapper.map(product, ProductFormDto.class);
    }
    // Product =>ProductFormDto
}