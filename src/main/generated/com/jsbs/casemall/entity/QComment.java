package com.jsbs.casemall.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QComment is a Querydsl query type for Comment
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QComment extends EntityPathBase<Comment> {

    private static final long serialVersionUID = -1836730409L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QComment comment = new QComment("comment");

    public final DateTimePath<java.time.LocalDateTime> commentChgDate = createDateTime("commentChgDate", java.time.LocalDateTime.class);

    public final StringPath commentContent = createString("commentContent");

    public final DateTimePath<java.time.LocalDateTime> commentDelDate = createDateTime("commentDelDate", java.time.LocalDateTime.class);

    public final NumberPath<Integer> commentNestedTo = createNumber("commentNestedTo", Integer.class);

    public final NumberPath<Integer> commentNestLevel = createNumber("commentNestLevel", Integer.class);

    public final NumberPath<Long> commentNo = createNumber("commentNo", Long.class);

    public final DateTimePath<java.time.LocalDateTime> commentRegDate = createDateTime("commentRegDate", java.time.LocalDateTime.class);

    public final StringPath memberId = createString("memberId");

    public final StringPath refBoard = createString("refBoard");

    public final NumberPath<Integer> refPostNo = createNumber("refPostNo", Integer.class);

    public final QUsers userId;

    public QComment(String variable) {
        this(Comment.class, forVariable(variable), INITS);
    }

    public QComment(Path<? extends Comment> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QComment(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QComment(PathMetadata metadata, PathInits inits) {
        this(Comment.class, metadata, inits);
    }

    public QComment(Class<? extends Comment> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.userId = inits.isInitialized("userId") ? new QUsers(forProperty("userId")) : null;
    }

}

