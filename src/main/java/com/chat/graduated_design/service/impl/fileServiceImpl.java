package com.chat.graduated_design.service.impl;

import com.chat.graduated_design.exception.FileException;
import com.chat.graduated_design.service.fileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.UUID;

/**
 * @program: Graduated_Design
 * @description: 文件上传下载服务实现类
 * @author: 常笑男
 * @create: 2022-02-10 13:54
 **/
@Component
public class fileServiceImpl implements fileService {
    public static final Integer HEADPORTRAITPATH = 0;   //头像存储位置
    public static final Integer FILEPATH = 1;           //
    public static final Integer PHOTOPATH = 2;          //图片存储位置
    public static final Integer MUSICPATH = 3;          //音乐存储位置
    public static final Integer VIDEOPATH = 4;          //视频存储位置

    private String[] path = new String[5];              // 文件在本地存储的地址

    public fileServiceImpl(@Value("${file.upload.path.headportrait}") String headPortraitPath,
                           @Value("${file.upload.path.file}") String filePath,
                           @Value("${file.upload.path.photo}") String photoPath,
                           @Value("${file.upload.path.music}") String musicPath,
                           @Value("${file.upload.path.video}") String videoPath) {
        this.path[0] = headPortraitPath;
        this.path[1] = filePath;
        this.path[2] = photoPath;
        this.path[3] = musicPath;
        this.path[4] = videoPath;
    }

    /**
     * 存储文件到系统
     *
     * @param file 文件
     * @param target 存储位置
     * @return UUID
     */
    public String storeFile(MultipartFile file, Integer target) {
        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        File saveFile=new File(this.path[target]);
        String uuid= UUID.randomUUID().toString().toLowerCase();
        if(!saveFile.exists()){
            saveFile.mkdir();
        }
        try {
            file.transferTo(new File(this.path[target]+"/"+uuid));//生成UUID
        } catch (IOException e) {
            e.printStackTrace();
        }
        return uuid;
    }

    public Resource loadFileAsResource(String fileName,Integer source) {
        try {
            Path fileStorageLocation=Paths.get(this.path[source]).toAbsolutePath().normalize();
            Path filePath = fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new FileException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new FileException("File not found " + fileName, ex);
        }
    }
}
