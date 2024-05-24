package com.jsbs.casemall.repository;

import com.jsbs.casemall.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<Users, String> {
    Users findByEmail(String email);
   Optional<Users> findByEmailAndPhone(String email, String phone);
}

