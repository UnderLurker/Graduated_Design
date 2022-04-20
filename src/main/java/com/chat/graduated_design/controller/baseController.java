package com.chat.graduated_design.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chat.graduated_design.entity.file.FileStorage;
import com.chat.graduated_design.entity.user.User;
import com.chat.graduated_design.service.impl.fileDataServiceImpl;
import com.chat.graduated_design.service.impl.userServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @program: Graduated_Design
 * @description: 基础导航类
 * @author: 常笑男
 * @create: 2022-02-07 09:33
 **/
@Controller
public class baseController {
    @Autowired
    private userServiceImpl userService;
    @Autowired
    private fileDataServiceImpl fileDataService;

    @RequestMapping("/register.html")
    public String register(){
        return "register";
    }

    @RequestMapping("/login.html")
    public String login (){
        return "login";
    }

    @RequestMapping("/forgetCode.html")
    public String forgetCode (){
        return "forgetCode";
    }

    @RequestMapping("/main.html")
    public String main(Model model, HttpServletRequest request){
        Cookie[] cookies=request.getCookies();
        String responseUrl="./image/1.jpeg";
        //看是否激活
        
        String pwd=null;
        Integer id=null;
        for(Cookie cookie : cookies){
            if(cookie.getName().equals("id")){
                id=Integer.parseInt(cookie.getValue());
            }
            else if(cookie.getName().equals("pwd")){
                pwd=cookie.getValue().split("/")[cookie.getValue().split("/").length-1];
            }
        }
        if(pwd==null||id==null) return "redirect:/login.html";
        QueryWrapper<User> query=new QueryWrapper<>();
        query.eq("Id",id).eq("password", pwd)
            .or()
            .eq("Id", id).eq("face_image_uuid", pwd);
        User person=userService.getOne(query);
        if(person==null) return "redirect:/login.html";
        if(!person.isActive()){
            return "redirect:/login.html";
        }
        return "main";
    }

    @RequestMapping("/")
    public String root(){
        return "index";
    }
}
