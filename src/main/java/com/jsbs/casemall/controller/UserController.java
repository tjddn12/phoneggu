package com.jsbs.casemall.controller;


import com.jsbs.casemall.dto.UserDTO;
import com.jsbs.casemall.entity.Users;
import com.jsbs.casemall.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 회원가입 페이지 출력 요청
    @GetMapping("/save")
    public String saveForm() {

        return "member/save";
    }

    @PostMapping("/save")
    public String memberForm(@Valid  @ModelAttribute UserDTO userDTO,
                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "/save";
        }
        System.out.println("userDTO" + userDTO);
        userService.JoinUser(userDTO);

        return "redirect:/";
    }
    // 로그인 페이지
    @GetMapping("/login")
    public String login(){
        return  "member/memberLoginForm"; // 로그인 페이지
    }

    @GetMapping(value = "/login/error")
    public String loginError(Model model){
        model.addAttribute("loginErrorMsg","아이디 또는 비밀번호 확인");
        return "member/memberLoginForm";
    }


//
//    @GetMapping(value = "/login")
//    public String loginMember(){
//        return "member/memberLoginForm";
//    }
////    @GetMapping(value = "/login/error")
////    public String loginError(Model model){
////        model.addAttribute("loginErrorMsg","아이디 또는 비밀번호 확인");
////        return "member/memberLoginForm";
////    }

}