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
import com.chat.graduated_design.util.FileUtil;
import com.chat.graduated_design.util.VideoUtil;

import net.coobird.thumbnailator.Thumbnails;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.Session;

import java.awt.image.BufferedImage;
import java.io.File;
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

    /**
     * 接收上传的多个文件
     * @param files 文件
     * @param userId 用户id
     * @param contactId 联系人id
     * @return
     */
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
            String size=FileUtil.getSize(files[index].getSize());
            
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

    /**
     * 文件下载响应函数
     * @param fileStorageNo 文件的存储id
     * @param response 通过输出流返回信息
     * @throws IOException
     */
    @GetMapping("/file/{fileStorageNo}")
    public void fileDownLoad(@PathVariable("fileStorageNo") Integer fileStorageNo,
                                                            HttpServletResponse response) throws IOException {
        FileStorage fileStorage=fileDataService.getById(fileStorageNo);
        String fileName=fileStorage.getOriginname();
        String root=FileUtil.getPathByFileStorageNo(fileStorage);
        
        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("UTF-8");
        response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));

        InputStream inputStream=new FileInputStream(root);
        ServletOutputStream outputStream=response.getOutputStream();
        inToOutStream(inputStream, outputStream);
        inputStream.close();
    }

    /**
     * 返回图片的缩略图
     * @param fileStorageNo 文件存储id
     * @param response 通过输出流返回数据
     * @throws IOException
     */
    @GetMapping("/photo/dim/{fileStorageNo}")
    public void dimPhoto(@PathVariable("fileStorageNo") Integer fileStorageNo,
                            HttpServletResponse response) throws IOException {
        FileStorage fileStorage=fileDataService.getById(fileStorageNo);
        String root=FileUtil.getPathByFileStorageNo(fileStorage);

        response.setContentType("image/*");
        ServletOutputStream outputStream=response.getOutputStream();

        BufferedImage bi = ImageIO.read(new File(root));
        Thumbnails.of(root)
                    .sourceRegion(0, 0,bi.getWidth(),bi.getHeight())
                    .size(bi.getWidth(),bi.getHeight())
                    .keepAspectRatio(true) // 是否保持原来的长宽比
                    .toOutputStream(outputStream); // 将生成的缩略图直接一输出流的形式输出；
    }

    @GetMapping("/photo/{fileStorageNo}")
    public void originPhoto(@PathVariable("fileStorageNo") Integer fileStorageNo,
                            HttpServletResponse response) throws IOException {
        FileStorage fileStorage=fileDataService.getById(fileStorageNo);
        String root=FileUtil.getPathByFileStorageNo(fileStorage);

        response.setContentType("image/*");
        
        InputStream inputStream=new FileInputStream(root);
        ServletOutputStream outputStream=response.getOutputStream();
        inToOutStream(inputStream, outputStream);
        inputStream.close();
    }

    public void inToOutStream(InputStream inputStream,ServletOutputStream outputStream) throws IOException{
        byte[] b=new byte[1024];
        int len;
        while((len=inputStream.read(b))>0){
            outputStream.write(b,0,len);
        }
    }

}
