package com.jsbs.casemall.controller;

import com.jsbs.casemall.dto.MemberDto;
import com.jsbs.casemall.entity.Member;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MemberController {

    @GetMapping("/join")
    public String join(){
        return "member/joinForm";
    }
}
