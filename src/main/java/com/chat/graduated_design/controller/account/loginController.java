package com.chat.graduated_design.controller.account;

import com.chat.graduated_design.entity.user.User;
import com.chat.graduated_design.message.ErrorMessage;
import com.chat.graduated_design.service.impl.fileServiceImpl;
import com.chat.graduated_design.service.impl.userServiceImpl;
import com.chat.graduated_design.util.ImageUtil;
import com.chat.graduated_design.util.MD5Util;

import org.apache.tomcat.util.codec.binary.Base64;
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
    private String domain="localhost";

    @Autowired
    private userServiceImpl userService;
    @Autowired
    private fileServiceImpl fileService;
    ErrorMessage errorMessage=new ErrorMessage();
    //邮箱登录
    @ResponseBody
    @RequestMapping("/login/emaillogin")
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
            Cookie pwd=new Cookie("pwd", sessionUser.getPassword());
            Cookie id=new Cookie("id",sessionUser.getId().toString());
            pwd.setPath("/");
            pwd.setDomain(domain);
            id.setPath("/");
            id.setDomain(domain);
            response.addCookie(id);
            response.addCookie(pwd);
            request.getSession().setAttribute("user", sessionUser);
            errorMessage.clear();
        }
        return errorMessage;
    }
    //手机登录
    @ResponseBody
    @PostMapping("/login/phonelogin")
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
            Cookie pwd=new Cookie("pwd", sessionUser.getPassword());
            Cookie cookie=new Cookie("id",user.getId().toString());
            pwd.setPath("/");
            pwd.setDomain(domain);
            cookie.setPath("/");
            cookie.setDomain(domain);
            response.addCookie(cookie);
            response.addCookie(pwd);
            request.getSession().setAttribute("user", sessionUser);
        }
        return errorMessage;
    }

    //刷脸登录
    @ResponseBody
    @PostMapping("/login/facelogin")
    public ErrorMessage faceLogin(@RequestBody Map<String,Object> info,HttpServletRequest request,HttpServletResponse response){
        errorMessage.clear();
        String file=(String) info.get("faceBase");
        String name=(String) info.get("name");

        if(name.isEmpty()){
            errorMessage.setFace("请输入您的名字");
            return errorMessage;
        }

        byte[] stream;
        try {
            stream=fileToByte(file);
        } catch (Exception e) {
            errorMessage.setFace("出现错误，请刷新重试");
            return errorMessage;
        }
        List<User> list=userService.selectByName(name);
        for(User user : list){
            String path=fileService.getPath(fileServiceImpl.FACE_PATH)+"/"+user.getFaceImageUuid();
            double res=ImageUtil.isSame(path, stream);
            path=path.replace(" ","");
            if(res>0.72){
                request.getSession().setAttribute("user",user);
                Cookie pwd=new Cookie("pwd", path);
                Cookie cookie=new Cookie("id",user.getId().toString());
                pwd.setPath("/");
                pwd.setDomain(domain);
                cookie.setPath("/");
                cookie.setDomain(domain);
                response.addCookie(cookie);
                response.addCookie(pwd);
                return errorMessage;
            }
        }
        errorMessage.setFace("没有匹配结果");
        return errorMessage;
    }

    /**
     * 将String类型的文件转换为数据流
     * @param file
     * @return 数据流
     * @throws Exception 转换过程出现错误
     */
    private byte[] fileToByte(String file){
        byte[] res;
        file = file.substring(file.indexOf(",", 1) + 1, file.length());
        res=Base64.decodeBase64(file);
        return res;
    }
}
