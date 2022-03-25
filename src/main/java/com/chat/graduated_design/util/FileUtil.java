package com.chat.graduated_design.util;

import java.io.FileNotFoundException;
import java.text.DecimalFormat;

import com.chat.graduated_design.entity.file.FileStorage;

import org.springframework.util.ResourceUtils;

/**
 * @program: Graduated_Design
 * @description: 文件大小转换工具类
 * @author: 常笑男
 * @create: 2022-03-10 15:52
 **/
public class FileUtil {
    public static String getSize(long size) {
        StringBuffer bytes = new StringBuffer();
        DecimalFormat format = new DecimalFormat("###.0");
        if (size >= 1024 * 1024 * 1024) {
            double i = (size / (1024.0 * 1024.0 * 1024.0));
            bytes.append(format.format(i)).append("GB");
        }
        else if (size >= 1024 * 1024) {
            double i = (size / (1024.0 * 1024.0));
            bytes.append(format.format(i)).append("MB");
        }
        else if (size >= 1024) {
            double i = (size / (1024.0));
            bytes.append(format.format(i)).append("KB");
        }
        else if (size < 1024) {
            if (size <= 0) {
                bytes.append("0B");
            }
            else {
                bytes.append((int) size).append("B");
            }
        }
        return bytes.toString();
    }
    /**
     * 将文件物理地址返回
     * @param fileStorage
     * @return
     */
    public static String getPathByFileStorageNo(FileStorage fileStorage){
        String root=null;
        try {
            root=ResourceUtils.getURL("classpath:static/image").getPath().replace("%20", " ").substring(1);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        root+="/"+fileStorage.getPath()+"/"+fileStorage.getUuid();
        return root;
    }
}
