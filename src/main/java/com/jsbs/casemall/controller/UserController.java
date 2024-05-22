package com.jsbs.casemall.controller;


import com.jsbs.casemall.dto.UserDTO;
import com.jsbs.casemall.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    // 회원가입 페이지 출력 요청
    @GetMapping("/save")
    public String saveForm() {

        return "save";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute UserDTO userDTO){
        System.out.println("MemberController.save");
        System.out.println("memberDTO" + userDTO);
        userService.JoinUser(userDTO);
        return "member";
    }

    // 로그인 페이지
    @GetMapping("/login")
    public String login(){
        return  "login"; // 로그인 페이지
    }
}