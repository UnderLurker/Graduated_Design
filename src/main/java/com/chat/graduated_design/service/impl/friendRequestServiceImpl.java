package com.chat.graduated_design.service.impl;

import java.util.HashSet;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chat.graduated_design.entity.contact.friendRequest;
import com.chat.graduated_design.mapper.friendRequestMapper;
import com.chat.graduated_design.service.friendRequestService;

import org.springframework.stereotype.Service;

/*
 * @program: Graduated_Design
 * @description: 
 * @author: 常笑男
 * @create: 2022-03-29 18:44
*/
@Service
public class friendRequestServiceImpl extends ServiceImpl<friendRequestMapper, friendRequest> implements friendRequestService{

    /**
     * 查询申请好友的id
     * @param userId
     * @return
     */
    public HashSet<Integer> selectRequester(Integer userId){
        HashSet<Integer> result=new HashSet<>();
        QueryWrapper<friendRequest> query=new QueryWrapper<>();
        query.eq("receive_id", userId);
        List<friendRequest> requester=this.list(query);
        for(friendRequest person : requester){
            result.add(person.getRequestId());
        }
        return result;
    }

    /**
     * 删除好友申请
     * @param userId
     * @param contactId
     */
    public void deleteApply(Integer userId,Integer contactId){
        QueryWrapper<friendRequest> query=new QueryWrapper<>();
        query.eq("request_id", userId).eq("receive_id", contactId)
            .or()
            .eq("request_id", contactId).eq("receive_id", userId);
        this.remove(query);
    }
}
