package com.jsbs.casemall.controller;


import com.jsbs.casemall.dto.UserDto;
import com.jsbs.casemall.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
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

        return "user/save";
    }

    @PostMapping("/save")
    public String userForm(@Valid  @ModelAttribute UserDto userDTO,
                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "user/save";
        }
        System.out.println("userDTO" + userDTO);
        userService.JoinUser(userDTO);

        return "redirect:/";
    }
    // 로그인 페이지
    @GetMapping("/login")
    public String login(){
        return  "user/userLoginForm"; // 로그인 페이지
    }










}