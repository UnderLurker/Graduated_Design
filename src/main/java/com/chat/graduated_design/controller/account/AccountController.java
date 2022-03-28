package com.chat.graduated_design.controller.account;

import com.chat.graduated_design.controller.WebSocket;
import com.chat.graduated_design.entity.contact.ResponseContact;
import com.chat.graduated_design.entity.contact.contact;
import com.chat.graduated_design.entity.contact.friendRequest;
import com.chat.graduated_design.entity.file.FileStorage;
import com.chat.graduated_design.entity.user.User;
import com.chat.graduated_design.message.Response;
import com.chat.graduated_design.service.impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.websocket.Session;

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
    @Autowired
    private friendRequestServiceImpl friendRequestService;

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
        userService.updateLoginTime(id);
        //查询用户头像信息
        FileStorage fileStorage=fileDataService.getUserPortrait(id);
        //查询用户分类列表
        List<Map<String,Object>> folderList=folderTableService.selectUserClassify(id);
        List<String> folders=new LinkedList<>();
        for (Map<String, Object> val: folderList){
            folders.add(val.get("folder").toString());
        }
        //查询用户的联系人
        List<ResponseContact> resList=contactService.queryContact(id,userService,chatInfoService);
        //查询表情图片
        List<String> emojiList=fileDataService.selectEmojiById(id);

        Map<String,Object> res=new HashMap<>();
        res.put("user",user);
        res.put("headportrait",fileStorage.getUuid());
        res.put("folder",folders);
        res.put("contact",resList);
        res.put("emojiList",emojiList);

        return Response.ok("/prepare/"+id.toString(),res);
    }

    @ResponseBody
    @PostMapping("/addContact/{userId}")
    public Response addContact(@RequestBody Map<String,Object> info,
                                @PathVariable("userId") Integer userId) throws IOException{
        // 添加联系人到数据库
        // contact person=new contact(null,userId,(Integer) info.get("id"),"所有",
        //                             (String) info.get("headportrait"),false,0);
        // contactService.save(person);
        Integer contactId=(Integer) info.get("id");
        // friendRequestService.save(new friendRequest(null,userId,contactId));

        //发送给要添加的联系人
        WebSocket webSocket=new WebSocket();
        Session toSession= webSocket.getSessionById(contactId);
        if (toSession != null&&toSession.isOpen()){
            User user=userService.getById(userId);
            String headPortrait=fileDataService.getById(userId).getUuid();//仅仅是uuid

            Map<String,Object> toInfo=new HashMap<>();
            toInfo.put("friend", true);
            toInfo.put("id", userId);
            toInfo.put("headportrait", headPortrait);
            toInfo.put("nickname", user.getNickname());

            String jsonObject= new JSONObject(toInfo).toString();
            toSession.getBasicRemote().sendText(jsonObject);
        }
        return Response.ok("/addContact",info);
    }


}
