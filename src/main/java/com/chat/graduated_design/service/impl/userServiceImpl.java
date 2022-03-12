package com.chat.graduated_design.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chat.graduated_design.entity.file.FileStorage;
import com.chat.graduated_design.entity.user.User;
import com.chat.graduated_design.mapper.userMapper;
import com.chat.graduated_design.service.userService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class userServiceImpl extends ServiceImpl<userMapper, User> implements userService {
    /**
     * 查询相似用户
     * @param column 列名
     * @param content 相似内容
     * @return
     */
    public List<Map<String, Object>> querylike(String column,String content,fileDataServiceImpl fileDataService) {
        QueryWrapper<User> userQueryWrapper=new QueryWrapper<>();
        userQueryWrapper.like(column,content);
        List<Map<String,Object>> queryResult=this.listMaps(userQueryWrapper);
        //将密码置为空
        for(Map<String,Object> item : queryResult){
            item.remove("password");
            item.remove("name");
            QueryWrapper<FileStorage> fileStorageQueryWrapper=new QueryWrapper<>();
            fileStorageQueryWrapper.eq("Id",item.get("id"));
            Map<String, Object> fileStorge=fileDataService.getMap(fileStorageQueryWrapper);
            item.put("headportrait","/headportrait/"+fileStorge.get("uuid"));
        }
        return queryResult;
    }
}
