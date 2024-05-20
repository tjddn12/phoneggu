package com.jsbs.casemall.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;

@Controller
public class MainController {


    @GetMapping("/")
    public String main(){

        return "index";
    }

    @GetMapping("/login")
    public String login(){

        return "member/loginForm";
    }

    @GetMapping("/shop")
    public String shop(){
        return  "shop";
    }



}
