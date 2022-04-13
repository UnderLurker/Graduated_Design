package com.chat.graduated_design.controller;

import com.chat.graduated_design.entity.file.FileStorage;
import com.chat.graduated_design.entity.file.ResponseFile;
import com.chat.graduated_design.entity.file.videoThumbnail;
import com.chat.graduated_design.message.Response;
import com.chat.graduated_design.service.impl.chatInfoServiceImpl;
import com.chat.graduated_design.service.impl.fileDataServiceImpl;
import com.chat.graduated_design.service.impl.userServiceImpl;
import com.chat.graduated_design.service.impl.videoThumbnailServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @program: Graduated_Design
 * @description: 返回搜素信息控制器
 * @author: 常笑男
 * @create: 2022-02-16 15:42
 **/
@RestController
public class searchController {
    @Autowired
    private userServiceImpl userService;
    @Autowired
    private fileDataServiceImpl fileDataService;
    @Autowired
    private videoThumbnailServiceImpl videoThumbnailService;
    @Autowired
    private chatInfoServiceImpl chatInfoService;

    @GetMapping("/search/{id}")
    public Response search(@PathVariable("id") Integer id,
                            @RequestParam("content") String content){
        Map<String,Object> res=new HashMap<>();
            //查找相似用户
        List<Map<String,Object>> queryUser=userService.querylike("nickname",content,fileDataService);
        res.put("user",queryUser);
        //文件
        List<ResponseFile> responseFile=this.selectByIdAndType(id, "/", content);
        res.put("file", responseFile);
        //图片
        List<ResponseFile> responsePhoto=this.selectByIdAndType(id, "image", content);
        res.put("photo", responsePhoto);
        //视频
        List<ResponseFile> responseVideo=this.selectByIdAndType(id, "video", content);
        res.put("video", responseVideo);
        //音频
        List<ResponseFile> responseMusic=this.selectByIdAndType(id, "audio", content);
        res.put("music", responseMusic);
        return Response.ok("/search",res);
    }

    private List<ResponseFile> selectByIdAndType(Integer id,String type, String content){
        List<ResponseFile> result=new LinkedList<>();
        List<FileStorage> allFiles=fileDataService.selectById(id,content);
        List<Integer> allKeys=new LinkedList<>();
        for(FileStorage file : allFiles){
            allKeys.add(file.getNo());
        }
        List<videoThumbnail> fileInfo=videoThumbnailService.listByFileStorageNo(allKeys);
        //同时遍历文件和信息
        for(int index1=0,index2=0;index1<allFiles.size()&&index2<fileInfo.size();){
            Integer no1=allFiles.get(index1).getNo();
            Integer no2=fileInfo.get(index2).getFileStorageNo();
            if(no1>no2){
                fileInfo.remove(fileInfo.get(index2));
                continue;
            }else if(no1<no2){
                allFiles.remove(allFiles.get(index1));
                continue;
            }
            String fileType=fileInfo.get(index2).getType().split("/")[0];
            if(!fileType.equals(type)&&!fileInfo.get(index2).getType().contains(type)){
                allFiles.remove(allFiles.get(index1));
                fileInfo.remove(fileInfo.get(index2));
                continue;
            }
            else{
                String thumbnailUuid=fileInfo.get(index2).getUuid();
                String fileUuid=allFiles.get(index1).getUuid();
                String uuid=thumbnailUuid==null?fileUuid:thumbnailUuid;
                String size=chatInfoService.getSizeByField(fileInfo.get(index2).getChatNo());
                ResponseFile info=new ResponseFile(uuid,allFiles.get(index1).getOriginname(),no2,allFiles.get(index1).getDatetime(),size);
                result.add(info);
                index1++;
                index2++;
            }
        }
        return result;
    }

}
