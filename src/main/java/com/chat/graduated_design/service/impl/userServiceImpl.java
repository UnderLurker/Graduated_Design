package com.chat.graduated_design.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chat.graduated_design.entity.file.FileStorage;
import com.chat.graduated_design.entity.user.User;
import com.chat.graduated_design.mapper.userMapper;
import com.chat.graduated_design.service.userService;
import com.chat.graduated_design.util.DateUtil;

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
            Map<String, Object> fileStorage=fileDataService.getMap(fileStorageQueryWrapper);
            String uuid = fileStorage==null?"1.jpeg":(String) fileStorage.get("uuid");
            item.put("headportrait","/headportrait/"+uuid);
        }
        return queryResult;
    }

    /**
     * 更新用户最近登录时间
     * @param no
     */
    public void updateLoginTime(Integer no){
        User user=this.getById(no);
        user.setPreLoginTime(user.getLoginTime());
        user.setLoginTime(DateUtil.getCurrentTime());
        this.updateById(user);
    }

    /**
     * 查询用户（通过email）
     * @param email
     * @return 
     */
    public List<User> selectByEmail(String email){
        QueryWrapper<User> emailQuery=new QueryWrapper<>();
        emailQuery.eq("email",email);
        return this.list(emailQuery);
    }

    /**
     * 查询用户（通过name）
     * @param name
     * @return
     */    
    public List<User> selectByName(String name){
        QueryWrapper<User> nameQuery=new QueryWrapper<>();
        nameQuery.eq("name",name);
        return this.list(nameQuery);
    }

    /**
     * 查询用户（通过phone）
     * @param phone
     * @return
     */
    public List<User> selectByPhone(String phone){
        QueryWrapper<User> phoneQuery=new QueryWrapper<>();
        phoneQuery.eq("phone",phone);
        return this.list(phoneQuery);
    }

    /**
     * 查询用户（通过姓名，邮件地址，密码）
     * @param name
     * @param email
     * @param password
     * @return
     */
    public List<Object> listUserByInfo(String name,String email,String password){
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("name",name)
                .eq("email",email)
                .eq("password",password);
        return this.listObjs(queryWrapper);
    }

    /**
     * 
     * @param email
     * @return
     */
    public List<Map<String,Object>> listMapByEmail(String email){
        QueryWrapper<User> query=new QueryWrapper<>();
        query.eq("email",email);
        return this.listMaps(query);
    }

    /**
     * 
     * @param phone
     * @return
     */
    public List<Map<String,Object>> listMapByPhone(String phone){
        QueryWrapper<User> query=new QueryWrapper<>();
        query.eq("phone",phone);
        return this.listMaps(query);
    }

    public User getOneByEmail(String email){
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("email",email);
        return this.getOne(queryWrapper);
    }

    public boolean updatePassword(String key,String password){
        UpdateWrapper<User> query=new UpdateWrapper<>();
        query.eq("email", key).or().eq("phone", key);
        User user=new User();
        user.setPassword(password);
        return this.update(user, query);
    }
}
