package com.chat.graduated_design.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class indexController {
    @RequestMapping("/register.html")
    public String register(){
        return "register";
    }
    @RequestMapping("/login.html")
    public String login (){
        return "login";
    }
}
