package com.chat.graduated_design.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface fileService {
    /**
     * 存储文件
     * @param file
     * @return
     */
    public String storeFile(MultipartFile file, Integer target, Map<String,Double> crop);

    /**
     * 存储多个文件 通过后缀新建不存在的文件夹
     * @param files 用户上传的文件组
     * @return path 存储的是对应顺序的存储路径
     *         uuid 对应的uuid
     */
    public Map<String,List<String>> storeFiles(MultipartFile[] files);

    /**
     * 加载文件资源
     * @param fileName
     * @return
     */
    public Resource loadFileAsResource(String fileName,Integer source);

    /**
     * 删除文件
     * @param uuid
     * @param target 目标路径
     */
    public void deleteFile(String uuid,Integer target);

    /**
     * 删除文件
     * @param path 文件的绝对路径
     */
    public void deleteFile(String path);

}

