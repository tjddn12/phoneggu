package com.jsbs.casemall.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QProduct is a Querydsl query type for Product
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProduct extends EntityPathBase<Product> {

    private static final long serialVersionUID = 1198364263L;

    public static final QProduct product = new QProduct("product");

    public final QBaseEntity _super = new QBaseEntity(this);

    public final NumberPath<Integer> categoriesNo = createNumber("categoriesNo", Integer.class);

    //inherited
    public final StringPath createdBy = _super.createdBy;

    public final NumberPath<Double> discount = createNumber("discount", Double.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final StringPath modifiedBy = _super.modifiedBy;

    public final StringPath prDetail = createString("prDetail");

    public final StringPath prName = createString("prName");

    public final NumberPath<Integer> prPrice = createNumber("prPrice", Integer.class);

    public final EnumPath<com.jsbs.casemall.constant.ProductSell> prSell = createEnum("prSell", com.jsbs.casemall.constant.ProductSell.class);

    public final NumberPath<Integer> prStock = createNumber("prStock", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> regTime = _super.regTime;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updateTime = _super.updateTime;

    public QProduct(String variable) {
        super(Product.class, forVariable(variable));
    }

    public QProduct(Path<? extends Product> path) {
        super(path.getType(), path.getMetadata());
    }

    public QProduct(PathMetadata metadata) {
        super(Product.class, metadata);
    }

}

