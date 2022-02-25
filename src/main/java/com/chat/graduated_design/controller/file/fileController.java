package com.chat.graduated_design.controller.file;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chat.graduated_design.entity.file.FileStorage;
import com.chat.graduated_design.entity.file.upLoadFileResponse;
import com.chat.graduated_design.message.Response;
import com.chat.graduated_design.service.impl.fileDataServiceImpl;
import com.chat.graduated_design.service.impl.fileServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    @Value("${file.upload.paths}")
    String[] paths;

    @PostMapping("/{id}/headportrait")
    public Response headPortrait(@RequestParam("headportrait") MultipartFile file,
                                 @PathVariable("id") Integer id){
        String uuid=fileService.storeFile(file,fileServiceImpl.HEADPORTRAITPATH);
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        String originName=fileName.split("\\.")[0];
        String fileType=file.getContentType();
        Date date=new Date();
        Date saveDate=null;
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            saveDate=format.parse(format.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
            return Response.error("出现了未知的错误");
        }
        FileStorage fileStorage=new FileStorage(id,null,uuid,originName,saveDate,fileType,fileServiceImpl.HEADPORTRAITPATH);
        //看数据库中是否有头像信息
        //如果有就更新，否则添加
        QueryWrapper<FileStorage> fileStorageQueryWrapper=new QueryWrapper<>();
        fileStorageQueryWrapper.eq("Id",id)
                .eq("folder",fileServiceImpl.HEADPORTRAITPATH);
        FileStorage queryFileStorage=fileDataService.getOne(fileStorageQueryWrapper);
        if(queryFileStorage!=null){
            fileService.deleteFile(queryFileStorage.getUuid(),fileServiceImpl.HEADPORTRAITPATH);
            fileDataService.update(fileStorage,fileStorageQueryWrapper);
        }
        else{
            fileDataService.save(fileStorage);
        }
        String responseUrl="/headportrait/"+uuid;
        return Response.ok("上传成功",responseUrl);
    }

    @PostMapping("/uploadFile")
    public upLoadFileResponse uploadFile(@RequestParam("headportrait") MultipartFile file){
        String fileName = fileService.storeFile(file,fileServiceImpl.PHOTOPATH);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadFile/")
                .path(fileName)
                .toUriString();

        return new upLoadFileResponse(fileName, fileDownloadUri,
                file.getContentType(), file.getSize());
    }

    @PostMapping("/uploadMultipleFiles")
    public List<upLoadFileResponse> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
        List<upLoadFileResponse> list = new ArrayList<>();
        if (files != null) {
            for (MultipartFile multipartFile:files) {
                upLoadFileResponse uploadFileResponse = uploadFile(multipartFile);
                list.add(uploadFileResponse);
            }
        }
        return list;
    }

    @GetMapping("/downloadFile/{fileName:.*}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        Resource resource = fileService.loadFileAsResource(fileName,fileServiceImpl.FILEPATH);
        String contentType = null;
        try {
            request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(contentType == null) {
            contentType = "application/octet-stream";
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
