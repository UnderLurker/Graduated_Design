package com.chat.graduated_design.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
     * @param info 需要存储的实体信息
     * @return 存储的信息的主键值
     */
    // public Integer saveInfo(chatInfo info){
    //     this.save(info);

    //     Map<String,Object> query=new HashMap<>();
    //     query.put("read_flag", info.isReadFlag());
    //     query.put("content", info.getContent());
    //     query.put("time", info.getTime());
    //     query.put("dest", info.getDest());
    //     query.put("origin", info.getOrigin());
    //     query.put("file", info.isReadFlag());

    //     List<chatInfo> queryResult=this.listByMap(query);

    //     return queryResult.get(0).getChatNo();
    // }

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

}
