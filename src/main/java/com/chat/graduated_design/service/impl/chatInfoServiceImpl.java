package com.chat.graduated_design.service.impl;

import java.util.LinkedList;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chat.graduated_design.entity.chat.chatInfo;
import com.chat.graduated_design.mapper.chatInfoMapper;
import com.chat.graduated_design.service.chatInfoService;
import org.springframework.stereotype.Service;

/**
 * @program: Graduated_Design
 * @description:
 * @author: 常笑男
 * @create: 2022-02-14 10:56
 **/
@Service
public class chatInfoServiceImpl extends ServiceImpl<chatInfoMapper, chatInfo> implements chatInfoService {

    /**
     * 
     * @param id 用户Id
     * @return 接收或者发送为id的信息列表
     */
    public List<chatInfo> selectChatByUserId(Integer id){
        QueryWrapper<chatInfo> queryInfo=new QueryWrapper<>();
        queryInfo.eq("dest",id).or().eq("origin",id);
        return this.list(queryInfo);
    }
    /**
     * 删除聊天记录(不能删除物理存储的文件)
     * @param userId
     * @param contactId
     */
    public void removeChatInfo(Integer userId,Integer contactId){
        QueryWrapper<chatInfo> query=new QueryWrapper<>();
        query.eq("dest", userId).eq("origin", contactId)
            .or()
            .eq("dest", contactId).eq("origin", userId);
        this.remove(query);
    }

    /**
     * 获得文件大小，通过主键
     * @param no
     * @return
     */
    public String getSizeByField(Integer no){
        return this.getById(no).getSize();
    }

    /**
     * 获取聊天所发送的文件的主键
     * @param userId
     * @param contactId
     * @return
     */
    public List<Integer> getFileId(Integer userId,Integer contactId){
        List<chatInfo> list=this.getAllFile(userId, contactId);
        List<Integer> result=new LinkedList<>();
        for(chatInfo info : list){
            result.add(info.getChatNo());
        }
        return result;
    }

    /**
     * 获取所有文件
     * @param userId
     * @param contactId
     * @return
     */
    public List<chatInfo> getAllFile(Integer userId,Integer contactId){
        QueryWrapper<chatInfo> query=new QueryWrapper<>();
        query.eq("dest", userId).eq("origin", contactId).eq("file", true).isNull("share")
            .or()
            .eq("dest", contactId).eq("origin", userId).eq("file", true).isNull("share");
        return this.list(query);
    }

    /**
     * 设置聊天信息为已读
     * @param userId
     * @param contactId
     */
    public void setRead(Integer userId,Integer contactId){
        UpdateWrapper<chatInfo> query=new UpdateWrapper<>();
        query.eq("dest",userId).eq("origin",contactId);
        chatInfo chat=new chatInfo();
        chat.setReadFlag(true);
        this.update(chat, query);
    }

}
