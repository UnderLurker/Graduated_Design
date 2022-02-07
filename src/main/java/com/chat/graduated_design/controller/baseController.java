package com.chat.graduated_design.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @program: Graduated_Design
 * @description: 基础导航类
 * @author: 常笑男
 * @create: 2022-02-07 09:33
 **/
@Controller
public class baseController {
    @RequestMapping("/register.html")
    public String register(){
        return "register";
    }

    @RequestMapping("/login.html")
    public String login (){
        return "login";
    }

    @RequestMapping("/main.html")
    public String main(Model model){
        return "main";
    }
}
