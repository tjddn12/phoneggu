package com.jsbs.casemall.entity;

import com.jsbs.casemall.constant.Role;
import com.jsbs.casemall.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("더미데이터넣기")
    void save(){
        Member member = new Member();
        member.setName("테스트");
        member.setUserId("test");
        member.setEmail("test@xxx.com");
        member.setUserPw(passwordEncoder.encode("1234"));
        member.setRole(Role.USER);
        userRepository.save(member);

        Member save =  userRepository.findById(member.getUserId()).orElseThrow(EntityNotFoundException::new);
        assertEquals(member.getUserId(),save.getUserId());

    }





}