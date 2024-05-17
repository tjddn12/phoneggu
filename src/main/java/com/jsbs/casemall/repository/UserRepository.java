package com.jsbs.casemall.repository;

import com.jsbs.casemall.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}
