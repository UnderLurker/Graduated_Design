package com.chat.graduated_design.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chat.graduated_design.entity.User;
import com.chat.graduated_design.message.ErrorMessage;
import com.chat.graduated_design.service.impl.userServiceImpl;
import com.chat.graduated_design.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
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
    public ErrorMessage emailLogin(@RequestBody User user, HttpServletRequest request){
        String saveString= null;
        errorMessage.clear();
        errorMessage.setEmail("用户名或密码错误");
//        user.setPassword(user.getPassword().trim());
        try {
            saveString = MD5Util.md5Encode(user.getPassword());
        } catch (Exception e) {
            e.printStackTrace();
        }
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("name",user.getName())
                .eq("email",user.getEmail())
                .eq("password",saveString);
        List<Object> userList=userService.listObjs(queryWrapper);
        QueryWrapper<User> query=new QueryWrapper<>();
        query.eq("email",user.getEmail());
        List<Map<String,Object>> list=userService.listMaps(query);
        if(userList.isEmpty()){
            if(!list.isEmpty()){
                errorMessage.setEmail("请先注册");
            }
        }
        else{
            errorMessage.clear();
            user.setNickname(list.get(0).get("nickname").toString());
            request.getSession().setAttribute("user",user);
        }
        return errorMessage;
    }
    //手机登录
    @ResponseBody
    @PostMapping("/phonelogin")
    public ErrorMessage phoneLogin(@RequestBody User user, HttpServletRequest request){
        errorMessage.clear();
        errorMessage.setPhone("用户名或密码错误");
        String saveString= null;
        try {
            saveString = MD5Util.md5Encode(user.getPassword());
        } catch (Exception e) {
            e.printStackTrace();
        }
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("name",user.getName())
                .eq("phone",user.getPhone())
                .eq("password",saveString);
        List<Object> userList=userService.listObjs(queryWrapper);
        QueryWrapper<User> query=new QueryWrapper<>();
        query.eq("phone",user.getPhone());
        List<Map<String,Object>> list=userService.listMaps(query);
        if(userList.isEmpty()){
            if(!list.isEmpty()){
                errorMessage.setEmail("请先注册");
            }
        }
        else{
            errorMessage.clear();
            user.setNickname(list.get(0).get("nickname").toString());
            request.getSession().setAttribute("user",user);
        }
        return errorMessage;
    }

    //刷脸登录 还未实现

}
