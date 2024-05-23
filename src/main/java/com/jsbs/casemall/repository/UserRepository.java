package com.jsbs.casemall.repository;

import com.jsbs.casemall.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, String> {
    Users findByEmail(String email);
}
