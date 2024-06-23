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

    public final QBaseEntity _super = new QBaseEntity(this);

    //inherited
    public final StringPath createdBy = _super.createdBy;

    public final StringPath filename = createString("filename");

    public final StringPath filepath = createString("filepath");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final StringPath modifiedBy = _super.modifiedBy;

    public final QProduct prId;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> regTime = _super.regTime;

    public final ListPath<ReviewImg, QReviewImg> reviewImgs = this.<ReviewImg, QReviewImg>createList("reviewImgs", ReviewImg.class, QReviewImg.class, PathInits.DIRECT2);

    public final StringPath revwContent = createString("revwContent");

    public final NumberPath<Integer> revwHits = createNumber("revwHits", Integer.class);

    public final NumberPath<Integer> revwRatings = createNumber("revwRatings", Integer.class);

    public final StringPath revwTitle = createString("revwTitle");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updateTime = _super.updateTime;

    public final QUsers userId;

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
        this.prId = inits.isInitialized("prId") ? new QProduct(forProperty("prId")) : null;
        this.userId = inits.isInitialized("userId") ? new QUsers(forProperty("userId")) : null;
    }

}

