package com.chat.graduated_design.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chat.graduated_design.entity.user.User;
import com.chat.graduated_design.message.Response;
import com.chat.graduated_design.service.impl.contactServiceImpl;
import com.chat.graduated_design.service.impl.fileDataServiceImpl;
import com.chat.graduated_design.service.impl.folderTableServiceImpl;
import com.chat.graduated_design.service.impl.userServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: Graduated_Design
 * @description: 返回搜素信息控制器
 * @author: 常笑男
 * @create: 2022-02-16 15:42
 **/
@RestController
public class searchController {
    @Autowired
    private userServiceImpl userService;
    @Autowired
    private folderTableServiceImpl folderTableService;
    @Autowired
    private contactServiceImpl contactService;
    @Autowired
    private fileDataServiceImpl fileDataService;
    @GetMapping("/search/{id}")
    public Response search(@PathVariable("id") Integer id, @RequestParam("content") String content){
        Map<String,Object> res=new HashMap<>();
        //查找相似用户
        List<Map<String,Object>> queryUser=userService.querylike("nickname",content,fileDataService);

        res.put("user",queryUser);
        return Response.ok("/search",res);
    }

}
