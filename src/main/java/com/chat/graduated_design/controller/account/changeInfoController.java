package com.chat.graduated_design.controller.account;

import com.chat.graduated_design.entity.user.User;
import com.chat.graduated_design.message.Response;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @program: Graduated_Design
 * @description: 用户修改信息控制类
 * @author: 常笑男
 * @create: 2022-02-12 16:13
 **/
@RestController
public class changeInfoController {
    @RequestMapping(value = "/changeInfo")
    public Response changeInfo(@RequestBody User user, HttpServletRequest request){
        ModelAndView mav=new ModelAndView();
        HttpSession session=request.getSession();
        User saveUser=(User) session.getAttribute("user");
//        saveUser.setName(name);
//        saveUser.setNickname(nickname);
        return Response.ok("/changeInfo");
    }
}
