package com.chat.graduated_design.service.impl;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chat.graduated_design.entity.file.FileStorage;
import com.chat.graduated_design.entity.file.videoThumbnail;
import com.chat.graduated_design.mapper.videoThumbnailMapper;
import com.chat.graduated_design.service.videoThumbnailService;
import org.springframework.stereotype.Service;

/**
 * @program: Graduated_Design
 * @description: 视频文件首帧图片服务类
 * @author: 常笑男
 * @create: 2022-03-10 16:34
 **/
@Service
public class videoThumbnailServiceImpl extends ServiceImpl<videoThumbnailMapper, videoThumbnail> implements videoThumbnailService {

    /**
     * 
     * @param chatNoList 主键列表
     * @return listByIds结果的map形式
     */
    public Map<Integer,Map<String,Object>> mapByIds(List<Integer> chatNoList){
        //将信息存储为Map形式，方便查找
        List<videoThumbnail> thumbnails=this.listByIds(chatNoList);
        Map<Integer,Map<String,Object>> thumbnailMap=new HashMap<>();
        for(videoThumbnail item : thumbnails){
            Map<String,Object> info=new HashMap<>();
            info.put("uuid", item.getUuid());
            info.put("fileStorageNo", item.getFileStorageNo());
            info.put("filetype",item.getType().split("/")[0]);
            thumbnailMap.put(item.getChatNo(), info);
        }
        return thumbnailMap;
    }

    /**
     * 查找所有符合fileStorageNos的实体
     * @param fileStorageNos
     * @return
     */
    public List<videoThumbnail> listByFileStorageNo(List<Integer> fileStorageNos){
        List<videoThumbnail> result=new LinkedList<>();
        for(Integer no : fileStorageNos){
            QueryWrapper<videoThumbnail> query=new QueryWrapper<>();
            query.eq("file_storage_no", no);
            videoThumbnail entity=this.getOne(query);
            result.add(entity);
        }
        return result;
    }
}
