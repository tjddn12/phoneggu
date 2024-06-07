package com.jsbs.casemall.repository;

import com.jsbs.casemall.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review,Long>, ReviewRepositoryCustom{

}