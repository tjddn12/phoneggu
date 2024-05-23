package com.jsbs.casemall.dto;

import com.jsbs.casemall.constant.ProductSell;
import com.jsbs.casemall.entity.Item;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ItemFormDto {
    private Long id;

    @NotBlank(message = "상품명은 필수 입력 값입니다.")
    private String itemNm;

    @NotNull(message = "가격은 필수 입력 값입니다.")
    private Integer price;

    @NotBlank(message = "상품 설명는 필수 입력 값입니다.")
    private String itemDetail;

    @NotNull(message = "재고는 필수 입력 값입니다.")
    private Integer stockNumber;

    private ProductSell prSell;
    private List<ItemImgDto> prImgDtoList = new ArrayList<>();
    private List<Long> prImgIds = new ArrayList<>();

    private static ModelMapper modelMapper = new ModelMapper();
    public Item createItem(){
        return modelMapper.map(this, Item.class);
    }
    //ItemFormDto => Item

    public static ItemFormDto of(Item item_obj){
        return modelMapper.map(item_obj, ItemFormDto.class);
    }
    // Item =>ItemFormDto
}