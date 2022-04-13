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
import com.chat.graduated_design.util.ObjectUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Collections;
import java.util.Comparator;
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
            if(info.isFile()&&info.getShare()==null){
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
                        if((!item.isFile()||thumbnailMap.get(item.getChatNo())==null)&&item.getShare()==null){
                            responseChat.add(new ResponseChat(item,null,null,null,null));
                        }
                        else{
                            String nickname=null;
                            if(item.getShare()!=null){
                                nickname=userService.getById(item.getShare()).getNickname();
                            }
                            Map<String,Object> temp=thumbnailMap.get(item.getChatNo());
                            Object uuid=temp==null?"1.jpeg":temp.get("uuid");
                            Object fileStorageNo=temp==null?null:temp.get("fileStorageNo");
                            Object filetype=temp==null?null:temp.get("filetype");
                            responseChat.add(new ResponseChat(item,uuid,fileStorageNo,filetype,nickname));
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

    /**
     * 更新用户设置的昵称
     * @param userId
     * @param contactId
     * @param name
     * @return 更新成功或失败
     */
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

    /**
     * 更新用户与联系人之间的拉黑关系(拉黑)
     * @param userId
     * @param contactId
     * @return
     */
    public Map<String,Integer> userBlackContact(Integer userId, Integer contactId){
        Map<String,Integer> res=new HashMap<>();
        //先更改用户对联系人的状态
        QueryWrapper<contact> query1=new QueryWrapper<>();
        query1.eq("userid", userId).eq("contactid", contactId);
        List<contact> accordWithList1=this.list(query1);

        if(accordWithList1.size()==0) return res;

        int blackState1=accordWithList1.get(0).getBlackList();
        if(blackState1==contact.ACTIVE||blackState1==contact.MUTUAL) return res;
        int finalState1=blackState1==contact.NORMAL?contact.ACTIVE:contact.MUTUAL;

        res.put("userState", finalState1);

        this.updateBlackList(userId, contactId, finalState1);

        //更改联系人对用户的状态
        QueryWrapper<contact> query2=new QueryWrapper<>();
        query2.eq("userid", contactId).eq("contactid", userId);
        List<contact> accordWithList2=this.list(query2);

        if(accordWithList2.size()==0) return res;

        int blackState2=accordWithList2.get(0).getBlackList();
        int finalState2=blackState2==contact.NORMAL?contact.PASSIVE:contact.MUTUAL;

        res.put("contactState", finalState2);

        this.updateBlackList(contactId, userId, finalState2);
        return res;
    }

    /**
     * 更新用户与联系人之间的拉黑关系(移出)
     * @param userId
     * @param contactId
     * @return
     */
    public Map<String,Integer> userWhiteContact(Integer userId, Integer contactId){
        Map<String,Integer> res=new HashMap<>();
        //先更改用户对联系人的状态
        QueryWrapper<contact> query1=new QueryWrapper<>();
        query1.eq("userid", userId).eq("contactid", contactId);
        List<contact> accordWithList1=this.list(query1);

        if(accordWithList1.size()==0) return res;

        int blackState1=accordWithList1.get(0).getBlackList();
        if(blackState1==contact.NORMAL||blackState1==contact.PASSIVE) return res;
        int finalState1=blackState1==contact.ACTIVE?contact.NORMAL:contact.PASSIVE;

        res.put("userState", finalState1);

        this.updateBlackList(userId, contactId, finalState1);

        //更改联系人对用户的状态
        QueryWrapper<contact> query2=new QueryWrapper<>();
        query2.eq("userid", contactId).eq("contactid", userId);
        List<contact> accordWithList2=this.list(query2);

        if(accordWithList2.size()==0) return res;

        int blackState2=accordWithList2.get(0).getBlackList();
        int finalState2=blackState2==contact.PASSIVE?contact.NORMAL:contact.ACTIVE;

        res.put("contactState", finalState2);

        this.updateBlackList(contactId, userId, finalState2);
        return res;
    }

    /**
     * 更新联系人黑名单
     * @param userId
     * @param contactId
     * @param finalState
     */
    public void updateBlackList(Integer userId, Integer contactId,Integer finalState){
        UpdateWrapper<contact> uWrapper=new UpdateWrapper<>();
        uWrapper.eq("userid", userId).eq("contactid", contactId);
        contact finalContact=new contact();
        finalContact.setBlackList(finalState);
        this.update(finalContact, uWrapper);
    }

    /**
     * 获得联系人的头像
     * @param userId
     * @param contactId
     * @return
     */
    public String getHeadPortrait(Integer userId,Integer contactId){
        QueryWrapper<contact> query=new QueryWrapper<>();
        query.eq("userid", userId).eq("contactid", contactId);
        contact person=this.getOne(query);
        return person.getHeadportrait();
    }

    /**
     * contact表中是否包含该联系人
     * @param userId
     * @param contactId
     * @return
     */
    public boolean isContain(Integer userId,Integer contactId){
        QueryWrapper<contact> query=new QueryWrapper<>();
        query.eq("userid", userId).eq("contactid", contactId);
        contact person=null;
        person=this.getOne(query);
        return person==null?false:true;
    }

    /**
     * 删除folderName的分组
     * @param userId
     * @param folderName
     * @return
     */
    public boolean folderDelete(Integer userId,String folderName){
        QueryWrapper<contact> query=new QueryWrapper<>();
        query.eq("userid", userId).eq("folder", folderName);
        return this.remove(query);
    }

    /**
     * 批量移除
     * @param userId
     * @param contactIds
     * @param folderName
     * @return
     */
    public boolean removeBatch(Integer userId,List<Object> contactIds,String folderName){
        if(contactIds.size()==0) return true;
        List<Integer> keyList=new LinkedList<>();

        List<Integer> idList=new LinkedList<>();
        for(Object obj : contactIds){
            idList.add((Integer) obj);
        }
        Collections.sort(idList,new IntegerComparator());

        QueryWrapper<contact> query=new QueryWrapper<>();
        query.eq("userid", userId).eq("folder", folderName);
        List<contact> list=this.list(query);
        list.sort(null);

        for (int index1 = 0, index2 = 0; index1 < idList.size() && index2 < list.size();) {
            Integer id1=idList.get(index1);
            Integer id2=list.get(index2).getContactid();
            if(id1>id2){
                index2++;
            }
            else if(id1<id2){
                index1++;
            }
            else{
                keyList.add(list.get(index2).getContactNo());
                index1++;
                index2++;
            }
        }

        return this.removeByIds(keyList);
    }

    //比较器
    class IntegerComparator implements Comparator<Integer>{
        @Override
        public int compare(Integer o1, Integer o2) {
            return o1-o2;
        }
        
    }

    /**
     * 批量保存
     * @param userId
     * @param selectedPerson
     * @param folderName
     * @return
     */
    public boolean saveBatch(Integer userId,List<Object> selectedPerson,String folderName){
        List<contact> saveList=new LinkedList<>();
        if(selectedPerson.size()==0) return true;
        for(Object obj : selectedPerson){
            Map<String,Object> person=null;
            try {
                person=(Map<String,Object>) obj;
                String photoPath=(String) person.get("headportrait");
                String headPortrait=photoPath.split("/")[photoPath.split("/").length-1];
                contact savePerson=new contact(null,
                                userId, (Integer) person.get("contactid"), 
                                folderName, headPortrait, (boolean)person.get("doNotDisturb"), 
                                (String) person.get("nickname"), (Integer) person.get("relative"));
                saveList.add(savePerson);
            } catch (Exception e) {
                return false;
            }
        }
        return this.saveBatch(saveList, 30);
    }

}

