package com.chat.graduated_design.controller.account;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chat.graduated_design.entity.user.User;
import com.chat.graduated_design.message.ErrorMessage;
import com.chat.graduated_design.service.impl.userServiceImpl;
import com.chat.graduated_design.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @program: Graduated_Design
 * @description: 登录控制器
 * @author: 常笑男
 * @create: 2022-02-07 09:32
 **/
@Controller
public class loginController {
    @Autowired
    private userServiceImpl userService;
    ErrorMessage errorMessage=new ErrorMessage();
    //邮箱登录
    @ResponseBody
    @RequestMapping("/emaillogin")
    public ErrorMessage emailLogin(@RequestBody User user,
                                   HttpServletRequest request,
                                   HttpServletResponse response){
        String saveString= null;
        errorMessage.clear();
        errorMessage.setEmail("用户名或密码错误");
//        user.setPassword(user.getPassword().trim());
        try {
            saveString = MD5Util.md5Encode(user.getPassword());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        List<Object> userList=userService.listUserByInfo(user.getName(),user.getEmail(),saveString);
        List<Map<String,Object>> list=userService.listMapByEmail(user.getEmail());
        if(userList.isEmpty()){
            if(list.isEmpty()){
                errorMessage.setEmail("请先注册");
            }
        }
        else{
            Map<String,Object> map=list.get(0);
            User sessionUser=new User(map);
            sessionUser.setPassword("");
            request.getSession().setAttribute("user",sessionUser);
            Cookie cookie=new Cookie("id",sessionUser.getId().toString());
            response.addCookie(cookie);
            errorMessage.clear();
        }
        return errorMessage;
    }
    //手机登录
    @ResponseBody
    @PostMapping("/phonelogin")
    public ErrorMessage phoneLogin(@RequestBody User user,
                                   HttpServletRequest request,
                                   HttpServletResponse response){
        errorMessage.clear();
        errorMessage.setPhone("用户名或密码错误");
        String saveString= null;
        try {
            saveString = MD5Util.md5Encode(user.getPassword());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        List<Object> userList=userService.listUserByInfo(user.getName(),user.getEmail(),saveString);
        List<Map<String,Object>> list=userService.listMapByPhone(user.getPhone());
        if(userList.isEmpty()){
            if(list.isEmpty()){
                errorMessage.setEmail("请先注册");
            }
        }
        else{
            errorMessage.clear();
            Map<String,Object> map=list.get(0);
            User sessionUser=new User(map);
            sessionUser.setPassword("");
            request.getSession().setAttribute("user",sessionUser);
            Cookie cookie=new Cookie("id",user.getId().toString());
            response.addCookie(cookie);
        }
        return errorMessage;
    }

    //刷脸登录 还未实现

}
