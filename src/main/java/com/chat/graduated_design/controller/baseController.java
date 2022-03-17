package com.chat.graduated_design.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chat.graduated_design.entity.file.FileStorage;
import com.chat.graduated_design.entity.user.User;
import com.chat.graduated_design.service.impl.fileDataServiceImpl;
import com.chat.graduated_design.service.impl.fileServiceImpl;
import com.chat.graduated_design.service.impl.userServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

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

    @RequestMapping("/main.html")
    public String main(Model model, HttpServletRequest request){
        HttpSession session=request.getSession();
        String responseUrl="./image/1.jpeg";
        //看是否激活
        User user=(User) session.getAttribute("user");
        User queryUser=userService.getOneByEmail(user.getEmail());
        if(!queryUser.isActive()){
            return "redirect:/login.html";
        }
        //查询头像路径
        FileStorage fileStorage=fileDataService.getUserPortrait(queryUser.getId());
        if(fileStorage!=null){
            responseUrl="/headportrait/"+fileStorage.getUuid();
        }
        model.addAttribute("headportrait",responseUrl);
        return "main";
    }

    @RequestMapping("/")
    public String root(){
        return "index";
    }
}
