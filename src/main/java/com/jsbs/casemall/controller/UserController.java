package com.jsbs.casemall.controller;


import com.jsbs.casemall.dto.UserDto;
import com.jsbs.casemall.dto.UserPwRequestDto;
import com.jsbs.casemall.entity.Users;
import com.jsbs.casemall.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@Slf4j
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

        return "redirect:/login";
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


    @GetMapping("/findId")
    public String showFindIdPage() {
        return "user/findId";
    }

    @PostMapping("/findUser")
    public String findUserByEmailAndPhone(@RequestParam String email, @RequestParam String phone, Model model) {
        try {
            Users user = userService.getUserByEmailAndPhoneNumber(email, phone);
            model.addAttribute("user", user);
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", "User not found");
        } catch (Exception e) {
            model.addAttribute("error", "An unexpected error occurred: " + e.getMessage());
        }
        return "user/findId";
    }
    // 비밀번호 변경

    @GetMapping("/findPw")
    public String findPwForm() {

        return "user/findPw";
    }

    @PostMapping("/findPw")
    public String pwFind(UserPwRequestDto requestDto) {
        userService.userCheck(requestDto);
        return "user/userLogin";
    }

}







