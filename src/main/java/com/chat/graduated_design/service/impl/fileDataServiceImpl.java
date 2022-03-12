package com.chat.graduated_design.service.impl;

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
}
