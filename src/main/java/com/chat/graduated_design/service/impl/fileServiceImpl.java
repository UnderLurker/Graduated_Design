package com.chat.graduated_design.service.impl;

import com.chat.graduated_design.exception.FileException;
import com.chat.graduated_design.service.fileService;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;

/**
 * @program: Graduated_Design
 * @description: 文件上传下载服务实现类
 * @author: 常笑男
 * @create: 2022-02-10 13:54
 **/
@Component
public class fileServiceImpl implements fileService {
    public static final Integer HEAD_PORTRAIT_PATH = 0;   //头像存储位置
    public static final Integer EMOJI_PATH = 1;          //视频存储位置
    public static final Integer THUMBNAIL_PATH = 2;       //视频文件的展示图片

    private String[] path = new String[3];              // 文件在本地存储的地址

    public fileServiceImpl(@Value("${file.upload.paths}") String[] ymlPath) {
        for(int index=0;index<ymlPath.length;index++){
            try {
                this.path[index]=ResourceUtils.getURL(ymlPath[index]).getPath().replace("%20"," ").substring(1);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public String getPath(Integer num) {
        return this.path[num];
    }

    /**
     * 存储文件到系统，如果需要裁剪图片裁剪图片
     *
     * @param file 文件
     * @param target 存储位置
     * @param crop 图片裁剪
     * @return UUID
     */
    @Override
    public String storeFile(MultipartFile file, Integer target,Map<String,Double> crop) {
        String suffix=file.getOriginalFilename().split("\\.")[1];
        String uuid= UUID.randomUUID().toString().toLowerCase();
        if(crop==null){
            //保存文件到本地(不裁剪)
            try {
                file.transferTo(new File(this.path[target]+"/"+uuid+"."+suffix));//生成UUID
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            Integer width=crop.get("originWidth").intValue();
            Integer height=crop.get("originHeight").intValue();
            Double scale=crop.get("scale");
            int x=Integer.parseInt(new java.text.DecimalFormat("0").format(crop.get("x")/scale));
            int y=Integer.parseInt(new java.text.DecimalFormat("0").format(crop.get("y")/scale));
            int w=Integer.parseInt(new java.text.DecimalFormat("0").format(crop.get("w")/scale));
            int h=Integer.parseInt(new java.text.DecimalFormat("0").format(crop.get("h")/scale));
            int x1=x+w,y1=y+h;
            if(x1>width){
                int dif=x1-width;
                x-=dif;
            }
            if(y1>height){
                int dif=y1-height;
                y-=dif;
            }
            //保存图片到本地(裁剪)
            try {
                Thumbnails.of(file.getInputStream())
                        .scale(1)
                        .sourceRegion(x,y,w,h)
                        .toFile(this.path[target]+"/"+uuid+"."+suffix);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return uuid+"."+suffix;
    }

    @Override
    public Map<String,List<String>> storeFiles(MultipartFile[] files){
        String rootPath="";
        try {
            rootPath=ResourceUtils.getURL("classpath:static/image/").getPath().replace("%20"," ").substring(1);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Map<String,List<String>> result=new HashMap<>();
        result.put("path",new LinkedList<>());
        result.put("uuid",new LinkedList<>());
        result.put("suffix",new LinkedList<>());

        for(MultipartFile file : files){
            //看是否有对应后缀的文件夹
            String[] fileNameSplit=file.getOriginalFilename().split("\\.");
            String suffix=fileNameSplit[fileNameSplit.length-1];
            String folderName=rootPath+suffix;
            File folder=new File(folderName);
            if(!folder.exists()){
                folder.mkdir();
            }
            //存储文件
            String uuid= UUID.randomUUID().toString().toLowerCase();
            try {
                file.transferTo(new File(folderName+"/"+uuid+"."+suffix));
            } catch (IOException e) {
                e.printStackTrace();
            }

            result.get("path").add(folderName);
            result.get("uuid").add(uuid+'.'+suffix);
            result.get("suffix").add(suffix);
        }
        return result;
    }

    @Override
    public void deleteFile(String uuid, Integer target) {
        File file=new File(this.path[target]+"/"+uuid);
        file.delete();
    }

    @Override
    public Resource loadFileAsResource(String fileName, Integer source) {
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
