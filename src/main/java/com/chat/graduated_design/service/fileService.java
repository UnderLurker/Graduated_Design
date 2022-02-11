package com.chat.graduated_design.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface fileService {
    /**
     * 存储文件
     * @param file
     * @return
     */
    public String storeFile(MultipartFile file,Integer target);

    /**
     * 加载文件资源
     * @param fileName
     * @return
     */
    public Resource loadFileAsResource(String fileName,Integer source);
}

