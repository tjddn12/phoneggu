package com.jsbs.casemall.repository;

import com.jsbs.casemall.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom {
    //: 기본 CRUD 메서드 상속용 인터페이스
}