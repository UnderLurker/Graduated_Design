package com.chat.graduated_design.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chat.graduated_design.entity.chat.chatInfo;
import com.chat.graduated_design.entity.contact.ResponseContact;
import com.chat.graduated_design.entity.contact.contact;
import com.chat.graduated_design.entity.user.User;
import com.chat.graduated_design.mapper.contactMapper;
import com.chat.graduated_design.service.contactService;
import org.springframework.stereotype.Service;

// import javax.management.Query;
import java.util.HashMap;
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
        QueryWrapper<chatInfo> queryInfo=new QueryWrapper<>();
        queryInfo.eq("dest",id).or().eq("origin",id);
        List<chatInfo> chatInfoList=chatInfoService.list(queryInfo);
        //将聊天信息分类 key为联系人的id
        Map<Integer,List<chatInfo>> chatInfoMap=new HashMap<>();
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
        }
        //第二步排序（按照时间）
        for (Map.Entry<Integer,List<chatInfo>> entry : chatInfoMap.entrySet()){
            entry.getValue().sort(null);
        }

        //contactid可能会重复 userid绝对不会重复
        int contactIndex = 0;
        for (int index = 0; index < userList.size() && contactIndex < contactList.size(); ) {
            if (userList.get(index).getId().equals(contactList.get(contactIndex).getContactid())) {
                ResponseContact responseContact = new ResponseContact(
                        contactList.get(contactIndex),
                        userList.get(index).getNickname(),
                        userList.get(index).getPhone(),
                        chatInfoMap.get(contactList.get(contactIndex).getContactid()));
                res.add(responseContact);
                contactIndex++;
                continue;
            }
            index++;
        }
        return res;
    }
}
