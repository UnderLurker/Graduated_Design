package com.chat.graduated_design.controller;

import com.chat.graduated_design.entity.Response;
import com.chat.graduated_design.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @program: Graduated_Design
 * @description: 修改信息控制器
 * @author: 常笑男
 * @create: 2022-02-07 09:40
 **/
@Controller
public class changeInfo {
    @ResponseBody
    @RequestMapping(value = "/changeInfo")
    public Response changeInfo(HttpServletRequest request){
        ModelAndView mav=new ModelAndView();
        HttpSession session=request.getSession();
        User saveUser=(User) session.getAttribute("user");
//        saveUser.setName(name);
//        saveUser.setNickname(nickname);
        return Response.ok("/changeInfo");
    }
}
