package com.jsbs.casemall.controller;


import com.jsbs.casemall.dto.UserDto;
import com.jsbs.casemall.entity.Users;
import com.jsbs.casemall.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 회원가입 페이지 출력 요청
    @GetMapping("/join")
    public String saveForm() {

        return "user/join";
    }

    @PostMapping("/join")
    public String userForm(@Valid  @ModelAttribute UserDto userDTO,
                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "user/join";
        }
        System.out.println("userDTO" + userDTO);
        userService.JoinUser(userDTO);

        return "redirect:/";
    }
    // 로그인 페이지
    @GetMapping("/login")
    public String login(){
        return  "user/userLogin"; // 로그인 페이지
    }

    // 아이디 찾기 페이지
    @GetMapping("/findUser")
    public String findForm() {

        return "user/findUser";
    }

    // 아이디 찾기
    @GetMapping("/findId")
    public String findIdForm(){

        return "user/findId";
    }

    @PostMapping("/findId")
    public String findUserByEmailAndPhoneNumberPost(
            @ModelAttribute UserDto userDto,
            Model model) {
        Users user = userService.getUserByEmailAndPhoneNumber(userDto.getEmail(), userDto.getPhone());
        if (user != null) {
            model.addAttribute("userId", user.getUserId());
        } else {
            model.addAttribute("error", "해당 아이디를 찾을 수 없습니다. 이메일 또는 전화번호를 다시 확인해 주세요.");
        }
        return "user/findId";
    }










}