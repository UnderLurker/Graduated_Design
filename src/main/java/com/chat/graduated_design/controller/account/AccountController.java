package com.chat.graduated_design.controller.account;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chat.graduated_design.entity.contact.ResponseContact;
import com.chat.graduated_design.entity.file.FileStorage;
import com.chat.graduated_design.entity.folder.folderTable;
import com.chat.graduated_design.entity.user.User;
import com.chat.graduated_design.message.Response;
import com.chat.graduated_design.service.impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
    @Autowired
    private folderTableServiceImpl folderTableService;
    @Autowired
    private contactServiceImpl contactService;
    @Autowired
    private chatInfoServiceImpl chatInfoService;
    @Autowired
    private fileDataServiceImpl fileDataService;

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
    @ResponseBody
    @GetMapping("/prepare/{id}")
    public Response prepare(@PathVariable("id") Integer id){
        //查询用户信息
        User user=userService.getById(id);
        user.setPassword("");
        //查询用户头像信息
        QueryWrapper<FileStorage> portraitQueryWrapper=new QueryWrapper<>();
        portraitQueryWrapper.eq("Id",id).eq("folder",fileServiceImpl.HEADPORTRAITPATH);
        FileStorage fileStorage=fileDataService.getOne(portraitQueryWrapper);
        //查询用户分类列表
        QueryWrapper<folderTable> folderTableQueryWrapper=new QueryWrapper<>();
        folderTableQueryWrapper.eq("user_id",id);
        List<Map<String,Object>> folderList=folderTableService.listMaps(folderTableQueryWrapper);
        List<String> folders=new LinkedList<>();
        for (Map<String, Object> val: folderList){
            folders.add(val.get("folder").toString());
        }
        //查询用户的联系人
        List<ResponseContact> resList=contactService.queryContact(id,userService,chatInfoService);

        Map<String,Object> res=new HashMap<>();
        res.put("user",user);
        res.put("headportrait",fileStorage.getUuid());
        res.put("folder",folders);
        res.put("contact",resList);

        return Response.ok("/prepare/"+id.toString(),res);
    }

}
