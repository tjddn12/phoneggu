package com.jsbs.casemall.repository;

import com.jsbs.casemall.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {

    Optional<Users> findByEmail(String email);

//    Users findByEmail(String email);  나중에 수정 및 삭제

    Optional<Users> findByEmailAndPhone(String email, String phone);

    Optional<Users> findByUserId(String userId);

//    Users findByUserName(String Name);

    @Query("SELECT u FROM Users u WHERE u.username = :username")
    Users findByUsername(@Param("username") String username);
}



