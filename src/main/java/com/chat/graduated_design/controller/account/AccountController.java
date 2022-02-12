package com.chat.graduated_design.controller.account;

import com.chat.graduated_design.entity.user.User;
import com.chat.graduated_design.service.impl.userServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @program: Graduated_Design
 * @description: 账户控制器
 * @author: 常笑男
 * @create: 2022-02-09 11:32
 **/
@Controller
public class AccountController {
    @Autowired
    private userServiceImpl userService;

    @GetMapping("/user/active/{id}/")
    public String active(@PathVariable(value = "id") Integer id){
        User queryUser=userService.getById(id);
        if(queryUser==null){
            return "redirect:/error.html";
        }
        queryUser.setActive(true);
        userService.updateById(queryUser);
        return "redirect:/main.html";
    }
}
