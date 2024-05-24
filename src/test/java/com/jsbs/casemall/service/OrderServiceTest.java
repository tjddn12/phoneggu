package com.jsbs.casemall.service;

import com.jsbs.casemall.entity.Product;
import com.jsbs.casemall.entity.Users;
import com.jsbs.casemall.repository.OrderRepository;
import com.jsbs.casemall.repository.ProductRepository;
import com.jsbs.casemall.repository.UserRepository;
import groovy.util.logging.Slf4j;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
@Slf4j
class OrderServiceTest {

    @Autowired
    private static OrderRepository orderRepository;


    @Autowired
    private static  UserRepository userRepository;

    @Autowired
    private static ProductRepository productRepository;



    @BeforeAll
    static void ready(){
    }


    @Test
    @DisplayName("주문 테스트")
    void order() {


    }




}