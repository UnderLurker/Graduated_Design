package com.chat.graduated_design.controller.file;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chat.graduated_design.controller.WebSocket;
import com.chat.graduated_design.entity.chat.chatInfo;
import com.chat.graduated_design.entity.file.FileStorage;
import com.chat.graduated_design.message.Response;
import com.chat.graduated_design.service.impl.chatInfoServiceImpl;
import com.chat.graduated_design.service.impl.fileDataServiceImpl;
import com.chat.graduated_design.service.impl.fileServiceImpl;
import com.chat.graduated_design.util.DateUtil;
import com.chat.graduated_design.util.FileSizeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.websocket.Session;
import java.util.*;

/**
 * @program: Graduated_Design
 * @description: 文件上传下载控制
 * @author: 常笑男
 * @create: 2022-02-10 13:47
 **/
@RestController
public class fileController {
    @Autowired
    private fileServiceImpl fileService;
    @Autowired
    private fileDataServiceImpl fileDataService;
    @Autowired
    private chatInfoServiceImpl chatInfoService;
    @Value("${file.upload.paths}")
    String[] paths;

    /**
     * 接收上传头像
     * @param file
     * @param id
     * @return
     */
    @PostMapping("/{id}/headportrait")
    public Response headPortrait(@RequestParam("headportrait") MultipartFile file,
                                 @RequestParam("scale") Double scale,
                                 @RequestParam("x") Double x,
                                 @RequestParam("y") Double y,
                                 @RequestParam("w") Double w,
                                 @RequestParam("h") Double h,
                                 @RequestParam("originWidth") Double originWidth,
                                 @RequestParam("originHeight") Double originHeight,
                                 @PathVariable("id") Integer id){
        //裁剪信息
        Map<String,Double> crop=new HashMap<>();
        crop.put("scale",scale);
        crop.put("x",x);
        crop.put("y",y);
        crop.put("w",w);
        crop.put("h",h);
        crop.put("originWidth",originWidth);
        crop.put("originHeight",originHeight);
        //保存图片
        String uuid=fileService.storeFile(file,fileServiceImpl.HEAD_PORTRAIT_PATH,crop);
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        String originName=fileName.split("\\.")[0];
        String fileType=file.getContentType();

        Date saveDate=DateUtil.getCurrentTime();
        FileStorage fileStorage=new FileStorage(id,null,uuid,originName,saveDate,fileType,fileServiceImpl.HEAD_PORTRAIT_PATH,null);
        //看数据库中是否有头像信息
        //如果有就更新，否则添加
        QueryWrapper<FileStorage> fileStorageQueryWrapper=new QueryWrapper<>();
        fileStorageQueryWrapper.eq("Id",id)
                .eq("folder",fileServiceImpl.HEAD_PORTRAIT_PATH);
        FileStorage queryFileStorage=fileDataService.getOne(fileStorageQueryWrapper);
        if(queryFileStorage!=null){
            fileService.deleteFile(queryFileStorage.getUuid(),fileServiceImpl.HEAD_PORTRAIT_PATH);
            fileDataService.update(fileStorage,fileStorageQueryWrapper);
        }
        else{
            fileDataService.save(fileStorage);
        }
        String responseUrl="/headportrait/"+uuid;
        return Response.ok("上传成功",responseUrl);
    }

    @PostMapping("/uploadMultipleFiles/{userId}/{contactId}")
    public Response uploadMultipleFiles(@RequestParam("files") MultipartFile[] files,
                                        @PathVariable("userId") Integer userId,
                                        @PathVariable("contactId") Integer contactId) {
        Map<String, List<String>> stringListMap = fileService.storeFiles(files);
        List<Map<String,Object>> responseInfo=new LinkedList<>();
        Date saveDate=DateUtil.getCurrentTime();
        for(int index=0;index<files.length;index++){
            FileStorage fileStorage=new FileStorage(userId,contactId,
                    stringListMap.get("uuid").get(index),
                    files[index].getOriginalFilename(),saveDate,
                    files[index].getContentType(),null,
                    stringListMap.get("suffix").get(index));
            fileDataService.save(fileStorage);
            //添加聊天信息
            chatInfo info=new chatInfo(false,
                    files[index].getOriginalFilename(),saveDate,contactId,userId,true);
            chatInfoService.save(info);

            //给发送信息的客户端发送成功信息
            Map<String,Object> item=new HashMap<>();
            item.put("name",files[index].getOriginalFilename());
            item.put("dest",contactId);
            item.put("origin",userId);
            responseInfo.add(item);

            WebSocket webSocket=new WebSocket();
            Session toSession= webSocket.getSessionById(contactId);
            if (toSession != null&&toSession.isOpen()){
                //向接收者发送信息
                Map<String,Object> sendInfo=new HashMap<>();
                sendInfo.put("content",files[index].getOriginalFilename());
                sendInfo.put("dest",contactId);
                sendInfo.put("origin",userId);
                sendInfo.put("readFlag",true);
                sendInfo.put("time",saveDate);
                sendInfo.put("file",true);
                sendInfo.put("size",FileSizeUtil.getSize(files[index].getSize()));
                sendInfo.put("suffix",stringListMap.get("suffix").get(index));

                String jsonObject= new JSONObject(sendInfo).toString();
                toSession.getAsyncRemote().sendObject(jsonObject);
            }

        }
        return Response.ok("存储成功",responseInfo);
    }
}
