package com.jsbs.casemall.repository;

import com.jsbs.casemall.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Member,String> {
}
