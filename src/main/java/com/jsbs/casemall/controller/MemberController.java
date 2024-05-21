package com.jsbs.casemall.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MemberController {
    // 회원가입 페이지 출력 요청
    @GetMapping("/member/save")
    public String saveForm() {

        return "save";
    }

    @PostMapping("/member/save")
    public String save(@RequestParam("user_id") String user_id){
        System.out.println("MemberController.save");
        System.out.println("user_id = " + user_id);
        return "member";
    }
}
