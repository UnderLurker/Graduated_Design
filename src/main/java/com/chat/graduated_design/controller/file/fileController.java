package com.chat.graduated_design.controller.file;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chat.graduated_design.controller.WebSocket;
import com.chat.graduated_design.entity.chat.chatInfo;
import com.chat.graduated_design.entity.file.FileStorage;
import com.chat.graduated_design.entity.file.videoThumbnail;
import com.chat.graduated_design.message.Response;
import com.chat.graduated_design.service.impl.chatInfoServiceImpl;
import com.chat.graduated_design.service.impl.fileDataServiceImpl;
import com.chat.graduated_design.service.impl.fileServiceImpl;
import com.chat.graduated_design.service.impl.videoThumbnailServiceImpl;
import com.chat.graduated_design.util.DateUtil;
import com.chat.graduated_design.util.FileSizeUtil;
import com.chat.graduated_design.util.VideoUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.Session;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
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

    @Autowired
    private videoThumbnailServiceImpl videoThumbnailService;

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

        Date saveDate=DateUtil.getCurrentTime();
        FileStorage fileStorage=new FileStorage(null,id,null,uuid,originName,saveDate,fileServiceImpl.HEAD_PORTRAIT_PATH,null);
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
            String originName=files[index].getOriginalFilename();
            String fileType=files[index].getContentType();
            String fileUuid=stringListMap.get("uuid").get(index);
            String suffix=stringListMap.get("suffix").get(index);
            String size=FileSizeUtil.getSize(files[index].getSize());
            
            //保存数据信息到数据库并得到主键值
            FileStorage fileStorage=new FileStorage(null,userId,contactId,fileUuid,originName,saveDate,null,suffix);
            fileDataService.save(fileStorage);
            Integer fileStorageNo=fileStorage.getNo();

            //添加聊天信息
            chatInfo info=new chatInfo(false,originName,saveDate,contactId,userId,true,size);
            chatInfoService.save(info);
            Integer chatNo=info.getChatNo();

            //获得存储视频封面的图片
            String root=null;
            try {
                root = ResourceUtils.getURL("classpath:static/image").getPath().replace("%20", " ").substring(1);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            VideoUtil videoUtil=new VideoUtil(root+"/"+suffix,fileService.getPath(fileServiceImpl.THUMBNAIL_PATH));
            String thumbnail=videoUtil.CreateJPGByFileType(fileType, fileUuid);

            if(thumbnail!=null){
                //保存thumbnail信息到数据库
                videoThumbnailService.save(new videoThumbnail(chatNo,thumbnail,fileStorageNo,fileType));
            }
            else{
                videoThumbnailService.save(new videoThumbnail(chatNo,null,fileStorageNo,fileType));
            }

            //给发送信息的客户端发送成功信息
            Map<String,Object> item=new HashMap<>();
            item.put("content",originName);
            item.put("dest",contactId);
            item.put("origin",userId);
            item.put("file",true);
            item.put("time",saveDate);
            item.put("thumbnail", thumbnail);
            item.put("size",size);
            item.put("suffix",suffix);
            item.put("filetype", fileType.split("/")[0]);
            item.put("fileStorageNo", fileStorageNo);
            responseInfo.add(item);

            WebSocket webSocket=new WebSocket();
            Session toSession= webSocket.getSessionById(contactId);
            if (toSession != null&&toSession.isOpen()){
                //向接收者发送信息
                Map<String,Object> sendInfo=new HashMap<>();
                sendInfo.put("content",originName);
                sendInfo.put("dest",contactId);
                sendInfo.put("origin",userId);
                sendInfo.put("readFlag",true);
                sendInfo.put("time",saveDate);
                sendInfo.put("file",true);
                sendInfo.put("size",size);
                sendInfo.put("suffix",suffix);
                sendInfo.put("filetype", fileType.split("/")[0]);
                sendInfo.put("thumbnail", thumbnail);
                sendInfo.put("fileStorageNo", fileStorageNo);

                String jsonObject= new JSONObject(sendInfo).toString();
                toSession.getAsyncRemote().sendObject(jsonObject);
            }

        }
        return Response.ok("存储成功",responseInfo);
    }

    @GetMapping("/file/{fileStorageNo}")
    public void fileDownLoad(@PathVariable("fileStorageNo") Integer fileStorageNo,
                                                            HttpServletResponse response) throws IOException {
        FileStorage fileStorage=fileDataService.getById(fileStorageNo);
        String fileName=fileStorage.getOriginname();
        String root=null;
        try {
            root=ResourceUtils.getURL("classpath:static/image").getPath().replace("%20", " ").substring(1);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        root+="/"+fileStorage.getPath()+"/"+fileStorage.getUuid();
        
        InputStream inputStream=new FileInputStream(root);
        response.reset();
        response.setContentType("application/octet-stream");
        response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));

        ServletOutputStream outputStream=response.getOutputStream();
        byte[] b=new byte[1024];
        int len;
        //从输入流中读取一定数量的字节，并将其存储在缓冲区字节数组中，读到末尾返回-1
        while ((len = inputStream.read(b)) > 0) {
            outputStream.write(b, 0, len);
        }
        inputStream.close();
    }
}
