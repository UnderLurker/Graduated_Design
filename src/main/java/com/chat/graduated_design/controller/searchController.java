package com.chat.graduated_design.controller;

import com.chat.graduated_design.entity.file.videoThumbnail;
import com.chat.graduated_design.message.Response;
import com.chat.graduated_design.service.impl.contactServiceImpl;
import com.chat.graduated_design.service.impl.fileDataServiceImpl;
import com.chat.graduated_design.service.impl.userServiceImpl;
import com.chat.graduated_design.service.impl.videoThumbnailServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.HashSet;
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
    private fileDataServiceImpl fileDataService;
    @Autowired
    private contactServiceImpl contactService;
    @Autowired
    private videoThumbnailServiceImpl videoThumbnailService;

    @GetMapping("/search/{id}")
    public Response search(@PathVariable("id") Integer id,
                            @RequestParam("content") String content,
                            @RequestParam("classify") Integer classify){
        Map<String,Object> res=new HashMap<>();
        if(classify.equals(0)){
            //查找相似用户
            List<Map<String,Object>> queryUser=userService.querylike("nickname",content,fileDataService);
            res.put("user",queryUser);
        }
        else if(classify.equals(1)){
            //文件
        }
        else if(classify.equals(2)){
            //图片
            
        }
        else if(classify.equals(3)){
            //视频
        }
        else if(classify.equals(4)){
            //音频
        }
        else{
            return Response.error("查询错误，请刷新重试");
        }
        return Response.ok("/search",res);
    }

}
