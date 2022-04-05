package com.chat.graduated_design.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chat.graduated_design.entity.chat.ResponseChat;
import com.chat.graduated_design.entity.chat.chatInfo;
import com.chat.graduated_design.entity.contact.ResponseContact;
import com.chat.graduated_design.entity.contact.contact;
import com.chat.graduated_design.entity.user.User;
import com.chat.graduated_design.mapper.contactMapper;
import com.chat.graduated_design.service.contactService;
import com.chat.graduated_design.util.DateUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @program: Graduated_Design
 * @description:
 * @author: 常笑男
 * @create: 2022-02-14 11:35
 **/
@Service
public class contactServiceImpl extends ServiceImpl<contactMapper, contact> implements contactService {

    @Autowired
    private videoThumbnailServiceImpl videoThumbnailService;

    /**
     * 查询联系人的信息以及聊天记录
     * @param id
     * @param userService
     * @param chatInfoService
     * @return
     */
    public List<ResponseContact> queryContact(Integer id,
                                                userServiceImpl userService,
                                                chatInfoServiceImpl chatInfoService) {
        List<ResponseContact> res = new LinkedList<>();

        Map<String, Object> query = new HashMap<>();
        query.put("userid", id);
        
        //将联系人表和用户表排序以便构造返回的联系人信息
        List<contact> contactList = this.listByMap(query);
        contactList.sort(null);

        //构造联系人的id集合，以便查询联系人的信息
        List<Integer> ids = new LinkedList<>();
        for (contact con : contactList) {
            ids.add(con.getContactid());
        }
        if(ids.size()==0) return res;
        List<User> userList = userService.listByIds(ids);
        userList.sort(null);

        //查询聊天记录
        List<chatInfo> chatInfoList=chatInfoService.selectChatByUserId(id);
        //将聊天信息分类 key为联系人的id
        Map<Integer,List<chatInfo>> chatInfoMap=new HashMap<>();
        //用户聊天信息的主键信息，用来查找thumbnail的信息
        List<Integer> chatNoList=new LinkedList<>();
        //第一步分类
        for(chatInfo info : chatInfoList){
            Integer tempId=(info.getOrigin()==id)?info.getDest():info.getOrigin();
            if(!chatInfoMap.containsKey(tempId)){
                List<chatInfo> chatInfos=new LinkedList<>();
                chatInfos.add(info);
                chatInfoMap.put(tempId,chatInfos);
            }
            else{
                chatInfoMap.get(tempId).add(info);
            }
            //如果为文件将chat_no添加到chatNoList
            if(info.isFile()){
                chatNoList.add(info.getChatNo());
            }
        }
        Map<Integer,Map<String,Object>> thumbnailMap=new HashMap<>();
        if(!chatNoList.isEmpty()){
            thumbnailMap=videoThumbnailService.mapByIds(chatNoList);
        }
        //第二步排序（按照时间）
        for (Map.Entry<Integer,List<chatInfo>> entry : chatInfoMap.entrySet()){
            entry.getValue().sort(null);
        }

        //contactid可能会重复 userid绝对不会重复
        int contactIndex = 0;
        for (int index = 0; index < userList.size() && contactIndex < contactList.size(); ) {
            if (userList.get(index).getId().equals(contactList.get(contactIndex).getContactid())) {
                //先将聊天信息与文件图片的信息连接
                List<chatInfo> chat=chatInfoMap.get(contactList.get(contactIndex).getContactid());
                List<ResponseChat> responseChat=new LinkedList<>();
                if(chat!=null){
                    for(chatInfo item : chat){
                        if(!item.isFile()||thumbnailMap.get(item.getChatNo())==null){
                            responseChat.add(new ResponseChat(item,null,null,null));
                        }
                        else{
                            responseChat.add(new ResponseChat(item,
                                    thumbnailMap.get(item.getChatNo()).get("uuid"),
                                    thumbnailMap.get(item.getChatNo()).get("fileStorageNo"),
                                    thumbnailMap.get(item.getChatNo()).get("filetype")));
                        }
                    }
                }
                Integer misTiming;
                try {
                    misTiming=DateUtil.daysBetween(userList.get(index).getPreLoginTime(), userList.get(index).getLoginTime());
                } catch (ParseException e) {
                    misTiming=null;
                }
                ResponseContact responseContact = new ResponseContact(
                        contactList.get(contactIndex),
                        userList.get(index).getNickname(),
                        userList.get(index).getPhone(),
                        responseChat,
                        misTiming);
                res.add(responseContact);
                contactIndex++;
                continue;
            }
            index++;
        }
        return res;
    }

    /**
     * 通过用户ID查找联系人ID
     * @param userId 用户ID
     * @return
     */
    public HashSet<Integer> selectContactIdByUserId(Integer userId){
        QueryWrapper<contact> query=new QueryWrapper<>();
        query.eq("userid", userId);
        List<contact> cotactList=this.list(query);
        HashSet<Integer> result=new HashSet<>();
        for(contact person : cotactList){
            result.add(person.getContactid());
        }
        return result;
    }

    /**
     * 获取userId对contactId设置的昵称
     * @param userId 用户ID
     * @param contactId 联系人ID
     * @return userId对contactId设置的nickname
     */
    public String getUserSettingNickName(Integer userId, Integer contactId){
        QueryWrapper<contact> query=new QueryWrapper<>();
        query.eq("userid", userId).eq("contactid", contactId);
        return this.getOne(query).getName();
    }

    public boolean updateName(Integer userId, Integer contactId,String name){
        UpdateWrapper<contact> qWrapper=new UpdateWrapper<>();
        qWrapper.eq("userid", userId).eq("contactid", contactId);
        contact person=new contact();
        person.setName(name);
        return this.update(person, qWrapper);
    }

    /**
     * 删除联系人列表
     * @param userId
     * @param contactId
     */
    public void removeContact(Integer userId,Integer contactId){
        QueryWrapper<contact> query=new QueryWrapper<>();
        query.eq("userid", userId).eq("contactid", contactId)
            .or()
            .eq("userid", contactId).eq("contactid", userId);
        this.remove(query);
    }
}
