package com.jsbs.casemall.dto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.jsbs.casemall.dto.QMainProductDto is a Querydsl Projection type for MainProductDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QMainProductDto extends ConstructorExpression<MainProductDto> {

    private static final long serialVersionUID = -495648709L;

    public QMainProductDto(com.querydsl.core.types.Expression<Long> id, com.querydsl.core.types.Expression<String> prName, com.querydsl.core.types.Expression<String> prDetail, com.querydsl.core.types.Expression<String> imgUrl, com.querydsl.core.types.Expression<Integer> prPrice) {
        super(MainProductDto.class, new Class<?>[]{long.class, String.class, String.class, String.class, int.class}, id, prName, prDetail, imgUrl, prPrice);
    }

}

