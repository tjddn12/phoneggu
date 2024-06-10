package com.jsbs.casemall.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReview is a Querydsl query type for Review
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReview extends EntityPathBase<Review> {

    private static final long serialVersionUID = 1192500960L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReview review = new QReview("review");

    public final QOrder order;

    public final QOrderDetail orderDetail;

    public final NumberPath<Integer> orderNo = createNumber("orderNo", Integer.class);

    public final StringPath product = createString("product");

    public final NumberPath<Integer> reviewNo = createNumber("reviewNo", Integer.class);

    public final StringPath revwContent = createString("revwContent");

    public final NumberPath<Integer> revwHits = createNumber("revwHits", Integer.class);

    public final NumberPath<Integer> revwRatings = createNumber("revwRatings", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> revwRegDate = createDateTime("revwRegDate", java.time.LocalDateTime.class);

    public final StringPath revwTitle = createString("revwTitle");

    public final StringPath userId = createString("userId");

    public QReview(String variable) {
        this(Review.class, forVariable(variable), INITS);
    }

    public QReview(Path<? extends Review> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReview(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReview(PathMetadata metadata, PathInits inits) {
        this(Review.class, metadata, inits);
    }

    public QReview(Class<? extends Review> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.order = inits.isInitialized("order") ? new QOrder(forProperty("order"), inits.get("order")) : null;
        this.orderDetail = inits.isInitialized("orderDetail") ? new QOrderDetail(forProperty("orderDetail"), inits.get("orderDetail")) : null;
    }

}

