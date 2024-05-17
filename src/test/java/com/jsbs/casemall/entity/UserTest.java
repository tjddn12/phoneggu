package com.jsbs.casemall.entity;

import com.jsbs.casemall.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
//@Transactional
class UserTest {

    @Autowired
    UserRepository userRepository;



    @Test
    void test(){
        Member user = new Member();
        user.setUserId("test");
        user.setName("테스트");
        user.setUserPw("123");
        user.setEmail("h2");
        user.setPhone(123);
        user.setPCode("123");
        user.setLoadAddr("rjfl");
        user.setLotAddr("ss");
        user.setDetailAddr("222");

        Member save =  userRepository.save(user);



    }
}