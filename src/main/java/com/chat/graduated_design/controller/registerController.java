package com.chat.graduated_design.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chat.graduated_design.entity.User;
import com.chat.graduated_design.entity.RegisterUser;
import com.chat.graduated_design.message.ErrorMessage;
import com.chat.graduated_design.service.impl.userServiceImpl;
import com.chat.graduated_design.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @program: Graduated_Design
 * @description: 注册控制器
 * @author: 常笑男
 * @create: 2022-02-07 09:31
 **/
@Controller
public class registerController {
    @Autowired
    private userServiceImpl userService;
    ErrorMessage errorMessage=new ErrorMessage();


    //邮箱注册
    @ResponseBody
    @RequestMapping(value = "/emailRegister",method = RequestMethod.POST)
    public ErrorMessage emailRegister(@RequestBody RegisterUser user,HttpServletResponse response){
        User saveUser=new User();
        errorMessage.clear();
        if(user.getPassword().equals(user.getPasswordConfirm())){
            try {
                String saveString= null;
                saveString = MD5Util.md5Encode(user.getPassword());
                saveUser.setEmail(user.getEmail());
                saveUser.setId(User.getCount()+1);
                saveUser.setName(user.getName());
                saveUser.setPassword(saveString);
                saveUser.setNickname(user.getName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if(!user.getPassword().equals(user.getPasswordConfirm())) {
            errorMessage.setEmail("密码填写错误");
        }
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("email",user.getEmail());
        List<Object> userList=userService.listObjs(queryWrapper);
        if(!userList.isEmpty()) {
            errorMessage.setEmail("该邮箱已经被注册");
        }
        if(errorMessage.getEmail()==null){
            userService.save(saveUser);
            User.setCount(User.getCount()+1);
            try {
                response.sendRedirect("/login.html");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return errorMessage;
    }
    //电话注册
    @ResponseBody
    @RequestMapping(value = "/phoneRegister",method = RequestMethod.POST)
    public ErrorMessage phoneRegister(@RequestBody RegisterUser user,HttpServletResponse response){
        User saveUser=new User();
        errorMessage.clear();
        if(user.getPassword().equals(user.getPasswordConfirm())){
            try {
                String saveString= null;
                saveString = MD5Util.md5Encode(user.getPassword());
                saveUser.setPhone(user.getPhone());
                saveUser.setId(User.getCount()+1);
                saveUser.setName(user.getName());
                saveUser.setPassword(saveString);
                saveUser.setNickname(user.getName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if(!user.getPassword().equals(user.getPasswordConfirm())) {
            errorMessage.setPhone("密码填写错误");
        }
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("phone",user.getPhone());
        List<Object> userList=userService.listObjs(queryWrapper);
        if(!userList.isEmpty()) {
            errorMessage.setPhone("该手机号已经被注册");
        }
        if(errorMessage.getPhone()==null){
            userService.save(saveUser);
            User.setCount(User.getCount()+1);
            try {
                response.sendRedirect("/login.html");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return errorMessage;
    }
}
