package com.chat.graduated_design.controller.account;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.chat.graduated_design.controller.WebSocket;
import com.chat.graduated_design.entity.contact.ResponseContact;
import com.chat.graduated_design.entity.contact.contact;
import com.chat.graduated_design.entity.contact.friendRequest;
import com.chat.graduated_design.entity.file.FileStorage;
import com.chat.graduated_design.entity.file.videoThumbnail;
import com.chat.graduated_design.entity.user.User;
import com.chat.graduated_design.message.Response;
import com.chat.graduated_design.service.impl.*;
import com.chat.graduated_design.util.DateUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.HashSet;
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
    @Autowired
    private friendRequestServiceImpl friendRequestService;
    @Autowired
    private fileServiceImpl fileService;
    @Autowired
    private videoThumbnailServiceImpl videoThumbnailService;

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
    
    //登录账号的准备工作，将数据封装完毕发送给客户端
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
        folders.add("新的朋友");
        //查询用户的联系人 + 新的朋友列
        List<ResponseContact> resList=contactService.queryContact(id,userService,chatInfoService);
        HashSet<Integer> requester=friendRequestService.selectRequester(id);
        for(Integer requestId : requester){
            FileStorage info=fileDataService.getUserPortrait(requestId);
            User request=userService.getById(requestId);
            contact temp=new contact(null,null,requestId,null,info.getUuid(),false,null,request.getNickname());
            resList.add(new ResponseContact(temp,request.getNickname(),request.getPhone(),null,null));
        }

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

    //添加联系人信息转发
    @ResponseBody
    @PostMapping("/addContact/{userId}")
    public Response addContact(@RequestBody Map<String,Object> info,
                                @PathVariable("userId") Integer userId) throws IOException{
        Integer contactId=(Integer) info.get("id");
        friendRequestService.save(new friendRequest(null,userId,contactId));

        //发送给要添加的联系人
        User user=userService.getById(userId);
        String headPortrait=fileDataService.getById(userId).getUuid();//仅仅是uuid

        Map<String,Object> toInfo=new HashMap<>();
        toInfo.put("friend", true);
        toInfo.put("id", userId);
        toInfo.put("headportrait", headPortrait);
        toInfo.put("nickname", user.getNickname());
        toInfo.put("phone", user.getPhone());
        WebSocket.sendObject(contactId, toInfo);

        return Response.ok("/addContact");
    }

    //用户确认添加联系人
    @ResponseBody
    @PostMapping("/{userId}/userAddContact")
    public Response userAddContact(@PathVariable("userId") Integer userId,
                                    @RequestBody Map<String,Object> contactInfo) throws IOException,ParseException{
        Integer contactId=(Integer) contactInfo.get("contactId");
        String contactHeadPortrait=(String) contactInfo.get("contactHeadportrait");
        String userHeadPortrait=(String) contactInfo.get("userHeadportrait");
        User requester=userService.getById(contactId);
        User receiver=userService.getById(userId);

        // 添加联系人到数据库
        contact receive=new contact(null,userId,contactId,"所有",contactHeadPortrait,false,0,requester.getNickname());
        contactService.save(receive);
        contact request=new contact(null,contactId,userId,"所有",userHeadPortrait,false,0,receiver.getNickname());
        contactService.save(request);
        // 删除请求好友表中的项
        friendRequestService.deleteApply(userId, contactId);

        Map<String,Object> requestAddContact=new HashMap<>();
        requestAddContact.put("contactid",userId);
        requestAddContact.put("headportrait",userHeadPortrait);
        requestAddContact.put("nickname",receiver.getNickname());
        requestAddContact.put("phone",receiver.getPhone());
        requestAddContact.put("misTiming",DateUtil.daysBetween(receiver.getLoginTime(), receiver.getPreLoginTime()));
        requestAddContact.put("sure", true);

        Map<String,Object> receiveAddContact=new HashMap<>();
        receiveAddContact.put("contactid",contactId);
        receiveAddContact.put("headportrait",contactHeadPortrait);
        receiveAddContact.put("nickname",requester.getNickname());
        receiveAddContact.put("phone",requester.getPhone());
        receiveAddContact.put("misTiming",DateUtil.daysBetween(requester.getLoginTime(), requester.getPreLoginTime()));
        receiveAddContact.put("sure", true);
        //发送给原来的请求者
        WebSocket.sendObject(contactId, requestAddContact);

        return Response.ok("/"+userId+"/userAddContact",receiveAddContact);
    }

    @ResponseBody
    @PostMapping("/contact/delete/{userId}")
    public Response deleteContact(@PathVariable("userId") Integer userId,
                                    @RequestBody Map<String,Object> contactInfo) throws IOException{
        Integer contactId=(Integer) contactInfo.get("id");
        String contactNickName=contactService.getUserSettingNickName(userId, contactId);
        String userNickName=contactService.getUserSettingNickName(contactId, userId);
        //删除文件
        removeFile(userId, contactId);
        //删除聊天信息
        chatInfoService.removeChatInfo(userId, contactId);
        //删除文件列表
        fileDataService.removeFileStorage(userId, contactId);
        //删除联系人列表
        contactService.removeContact(userId, contactId);

        Map<String,Object> userResponse=new HashMap<>();
        userResponse.put("id", userId);
        userResponse.put("nickname", userNickName);
        userResponse.put("deleteContact", true);

        WebSocket.sendObject(contactId, userResponse);

        userResponse.remove("deleteContact");
        userResponse.put("id", contactId);
        userResponse.put("nickname",contactNickName);
        return Response.ok("/contact/delete/"+userId,userResponse);
    }

    public void removeFile(Integer userId,Integer contactId){
        //删除file_storage中物理位置上的文件
        Map<Integer,String> fileInfo=fileDataService.getFileInfo(userId, contactId);
        for(Map.Entry<Integer, String> entry : fileInfo.entrySet()){
            fileService.deleteFile(entry.getValue());
        }
        //删除video_thumbnail物理位置上的文件
        List<Integer> field=chatInfoService.getFileId(userId, contactId);
        if(field.size()==0) return;
        List<videoThumbnail> entity=videoThumbnailService.listByIds(field);
        for(videoThumbnail item : entity){
            fileService.deleteFile(item.getUuid(), fileServiceImpl.THUMBNAIL_PATH);
        }
    }

    @ResponseBody
    @PostMapping("/resetName/{userId}")
    public Response resetContactName(@PathVariable("userId") Integer userId,
                                        @RequestBody Map<String,Object> info){
        Integer contactId=(Integer) info.get("contactId");
        String name=(String) info.get("name");

        contactService.updateName(userId, contactId, name);

        return Response.ok("/resetName/"+userId);
    }
}
