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

    /**
     * 查询用户存储文件的位置
     * @param userId
     * @param contactId
     * @return key:no value:absolute_path
     */
    public Map<Integer,String> getFileInfo(Integer userId,Integer contactId){
        Map<Integer,String> result=new HashMap<>();
        QueryWrapper<FileStorage> query=new QueryWrapper<>();
        query.eq("Id", userId).eq("receive_id", contactId)
            .or()
            .eq("Id", contactId).eq("receive_id", userId);
        List<FileStorage> files=this.list(query);
        for(FileStorage file : files){
            String root=fileServiceImpl.getClassPath()+file.getPath()+"/"+file.getUuid();
            result.put(file.getNo(), root);
        }
        return result;
    }

    /**
     * 删除信息
     * @param userId
     * @param contactId
     */
    public void removeFileStorage(Integer userId,Integer contactId){
        QueryWrapper<FileStorage> query=new QueryWrapper<>();
        query.eq("Id", userId).eq("receive_id", contactId)
            .or()
            .eq("Id", contactId).eq("receive_id", userId);
        this.remove(query);
    }
}
