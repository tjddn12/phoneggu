package com.jsbs.casemall.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QProductModel is a Querydsl query type for ProductModel
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProductModel extends EntityPathBase<ProductModel> {

    private static final long serialVersionUID = -2859774L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QProductModel productModel = new QProductModel("productModel");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QProduct product;

    public final EnumPath<com.jsbs.casemall.constant.ProductModelSelect> productModelSelect = createEnum("productModelSelect", com.jsbs.casemall.constant.ProductModelSelect.class);

    public final NumberPath<Integer> prStock = createNumber("prStock", Integer.class);

    public QProductModel(String variable) {
        this(ProductModel.class, forVariable(variable), INITS);
    }

    public QProductModel(Path<? extends ProductModel> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QProductModel(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QProductModel(PathMetadata metadata, PathInits inits) {
        this(ProductModel.class, metadata, inits);
    }

    public QProductModel(Class<? extends ProductModel> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.product = inits.isInitialized("product") ? new QProduct(forProperty("product")) : null;
    }

}

