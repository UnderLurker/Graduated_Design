package com.chat.graduated_design.controller.account;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chat.graduated_design.entity.user.User;
import com.chat.graduated_design.message.Response;
import com.chat.graduated_design.service.impl.userServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: Graduated_Design
 * @description: 用户修改信息控制类
 * @author: 常笑男
 * @create: 2022-02-12 16:13
 **/
@RestController
public class changeInfoController {
    @Autowired
    private userServiceImpl userService;

    @PostMapping(value = "/changeInfo")
    public Response changeInfo(@RequestBody User user,HttpSession session){
        User clientUser=(User)session.getAttribute("user");
        Map<String,String> map=new HashMap<>();
        if(clientUser.equals(user)){
            map.put("title","修改结果");
            map.put("msg","您未修改信息！！！");
            return Response.error("/changeInfo",map);
        }
        //需要查询数据库看是否重复
        if(!clientUser.getEmail().equals(user.getEmail())){
            QueryWrapper<User> emailQuery=new QueryWrapper<>();
            emailQuery.eq("email",user.getEmail());
            List<User> emailUser=userService.list(emailQuery);
            if(!emailUser.isEmpty()){
                map.put("title","修改失败");
                map.put("msg","您的新邮箱已被注册！！！");
                map.put("email",clientUser.getEmail());
                map.put("phone",clientUser.getPhone());
                return Response.error("/changeInfo",map);
            }
        }
        if(!clientUser.getPhone().equals(user.getPhone())){
            QueryWrapper<User> phoneQuery=new QueryWrapper<>();
            phoneQuery.eq("phone",user.getPhone());
            List<User> phoneUser=userService.list(phoneQuery);
            if(!phoneUser.isEmpty()){
                map.put("title","修改失败");
                map.put("msg","您的新手机号码已被注册！！！");
                map.put("phone",clientUser.getPhone());
                return Response.error("/changeInfo",map);
            }
        }

        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("Id",clientUser.getId());
        User queryUser=userService.getOne(queryWrapper);

        clientUser.setName(user.getName());
        clientUser.setNickname(user.getNickname());
        clientUser.setGender(user.getGender());
        clientUser.setPassword(queryUser.getPassword());
        clientUser.setEmail(user.getEmail());
        clientUser.setPhone(user.getPhone());


        userService.update(clientUser,queryWrapper);
        session.setAttribute("user",clientUser);
        map.put("title","修改成功");
        map.put("msg","您成功修改了自己的个人信息！！！");
        return Response.ok("/changeInfo",map);
    }
}
