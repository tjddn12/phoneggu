package com.jsbs.casemall.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QUsers is a Querydsl query type for Users
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUsers extends EntityPathBase<Users> {

    private static final long serialVersionUID = 2119849344L;

    public static final QUsers users = new QUsers("users");

    public final StringPath detailAddr = createString("detailAddr");

    public final StringPath email = createString("email");

    public final StringPath extraAddr = createString("extraAddr");

    public final StringPath loadAddr = createString("loadAddr");

    public final StringPath lotAddr = createString("lotAddr");

    public final StringPath name = createString("name");

    public final StringPath pCode = createString("pCode");

    public final StringPath phone = createString("phone");

    public final StringPath provider = createString("provider");

    public final StringPath providerId = createString("providerId");

    public final EnumPath<com.jsbs.casemall.constant.Role> role = createEnum("role", com.jsbs.casemall.constant.Role.class);

    public final StringPath socialId = createString("socialId");

    public final StringPath userId = createString("userId");

    public final StringPath username = createString("username");

    public final StringPath userPw = createString("userPw");

    public QUsers(String variable) {
        super(Users.class, forVariable(variable));
    }

    public QUsers(Path<? extends Users> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUsers(PathMetadata metadata) {
        super(Users.class, metadata);
    }

}

