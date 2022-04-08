package com.chat.graduated_design.controller.account;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chat.graduated_design.entity.user.User;
import com.chat.graduated_design.entity.user.RegisterUser;
import com.chat.graduated_design.message.ErrorMessage;
import com.chat.graduated_design.service.impl.userServiceImpl;
import com.chat.graduated_design.util.ImageUtil;
import com.chat.graduated_design.util.MD5Util;
import com.google.code.kaptcha.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.chat.graduated_design.service.impl.MailActiveImpl;
import com.chat.graduated_design.service.impl.fileServiceImpl;
import com.chat.graduated_design.service.impl.folderTableServiceImpl;

import javax.servlet.http.HttpServletRequest;
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
    @Autowired
    private MailActiveImpl activeUtil;
    @Autowired
    private fileServiceImpl fileService;
    @Autowired
    private folderTableServiceImpl folderTableService;

    ErrorMessage errorMessage = new ErrorMessage();

    // 邮箱注册
    @ResponseBody
    @RequestMapping(value = "/emailRegister", method = RequestMethod.POST)
    public ErrorMessage emailRegister(@RequestBody RegisterUser user) {
        User saveUser = new User();
        errorMessage.clear();
        if (user.getPassword().equals(user.getPasswordConfirm())) {
            try {
                String saveString = null;
                saveString = MD5Util.md5Encode(user.getPassword());
                saveUser.setEmail(user.getEmail());
                saveUser.setName(user.getName());
                saveUser.setPassword(saveString);
                saveUser.setNickname(user.getName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (!user.getPassword().equals(user.getPasswordConfirm())) {
            errorMessage.setEmail("密码填写错误");
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", user.getEmail());
        List<Object> userList = userService.listObjs(queryWrapper);
        if (!userList.isEmpty()) {
            errorMessage.setEmail("该邮箱已经被注册");
        }
        if (errorMessage.getEmail() == null) {
            userService.save(saveUser);
            folderTableService.folderInit(saveUser.getId(), "所有");
            activeUtil.sendMimeMailWithId(user.getEmail(), saveUser.getId());
        }
        return errorMessage;
    }

    // 电话注册
    @ResponseBody
    @RequestMapping(value = "/phoneRegister", method = RequestMethod.POST)
    public ErrorMessage phoneRegister(@RequestBody RegisterUser user,HttpServletRequest request) {
        User saveUser = new User();
        errorMessage.clear();
        if (user.getPassword().equals(user.getPasswordConfirm())) {
            try {
                String saveString = null;
                saveString = MD5Util.md5Encode(user.getPassword());
                saveUser.setPhone(user.getPhone());
                saveUser.setName(user.getName());
                saveUser.setPassword(saveString);
                saveUser.setNickname(user.getName());
                saveUser.setActive(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (!user.getPassword().equals(user.getPasswordConfirm())) {
            errorMessage.setPhone("密码填写错误");
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone", user.getPhone());
        List<Object> userList = userService.listObjs(queryWrapper);
        if (!userList.isEmpty()) {
            errorMessage.setPhone("该手机号已经被注册");
        }
        if (errorMessage.getPhone() == null) {
            // 比对验证码
            String sessionCode = (String) request.getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY);
            if (!sessionCode.equals(user.getCode())) {
                errorMessage.setPhone("验证码错误");
                return errorMessage;
            }
            userService.save(saveUser);
            folderTableService.folderInit(saveUser.getId(), "所有");
            errorMessage.setPhone("success");
        }
        return errorMessage;
    }

    @ResponseBody
    @RequestMapping(value = "/faceRegister", method = RequestMethod.POST)
    public ErrorMessage faceRegister(@RequestBody Map<String,Object> info) {
        errorMessage.clear();
        errorMessage.setFace("success");
        String file=(String) info.get("faceBase");
        String name=(String) info.get("name");
        if(name.isEmpty()){
            errorMessage.setFace("请输入您的名字");
            return errorMessage;
        }

        User user=new User();
        user.setName(name);
        user.setNickname(name);
        user.setActive(true);

        String uuid=ImageUtil.generateImage(file, fileService.getPath(fileServiceImpl.FACE_PATH)+"/");
        if(uuid==null){
            errorMessage.setFace("注册失败");
            return errorMessage;
        }
        user.setFaceImageUuid(uuid);
        userService.save(user);
        folderTableService.folderInit(user.getId(), "所有");
        return errorMessage;
    }
}
