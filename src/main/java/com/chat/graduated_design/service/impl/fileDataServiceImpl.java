package com.chat.graduated_design.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chat.graduated_design.entity.file.FileStorage;
import com.chat.graduated_design.mapper.fileMapper;
import com.chat.graduated_design.service.fileDataService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @program: Graduated_Design
 * @description: 文件存储数据库
 * @author: 常笑男
 * @create: 2022-02-11 17:33
 **/
@Service
public class fileDataServiceImpl  extends ServiceImpl<fileMapper, FileStorage> implements fileDataService {
    /**
     * 通过id来查询用户自己的表情
     * @param id 用户id
     * @return uuid列表
     */
    public List<String> selectEmojiById(Integer id){
        List<String> result=new LinkedList<>();
        Map<String,Object> queryCriteria=new HashMap<>();
        queryCriteria.put("Id",id);
        queryCriteria.put("folder",fileServiceImpl.EMOJI_PATH);
        List<FileStorage> emojiInfo=this.listByMap(queryCriteria);
        for(FileStorage item : emojiInfo){
            result.add(item.getUuid());
        }
        return result;
    }

    /**
     * 
     * @param userId
     * @return 返回用户头像信息
     */
    public FileStorage getUserPortrait(Integer userId){
        QueryWrapper<FileStorage> portraitQueryWrapper=new QueryWrapper<>();
        portraitQueryWrapper.eq("Id",userId).eq("folder",fileServiceImpl.HEAD_PORTRAIT_PATH);
        return this.getOne(portraitQueryWrapper);
    }

    /**
     * 查找接收或发送的所有文件，通过用户ID
     * @param userId
     * @return
     */
    public List<FileStorage> selectById(Integer userId){
        QueryWrapper<FileStorage> query=new QueryWrapper<>();
        query.eq("Id", userId).isNotNull("receive_id").or().eq("receive_id", userId);
        return this.list(query);
    }

    // /**
    //  * 保存信息到数据库并返回生成的主键值
    //  * @param fileInfo FileStorage实体
    //  * @return 存储到数据库中的主键值
    //  */
    // public Integer saveFile(FileStorage fileInfo){
    //     this.save(fileInfo);

    //     Map<String,Object> query=new HashMap<>();
    //     query.put("id", fileInfo.getId());
    //     query.put("receive_id", fileInfo.getReceiveId());
    //     query.put("uuid", fileInfo.getUuid());
    //     query.put("originname", fileInfo.getOriginname());
    //     query.put("datetime", fileInfo.getDatetime());
    //     query.put("type", fileInfo.getType());
    //     query.put("folder", fileInfo.getFolder());
    //     query.put("path", fileInfo.getPath());

    //     List<FileStorage> queryResult=this.listByMap(query);
    //     return queryResult.get(0).getNo();
    // }
}
