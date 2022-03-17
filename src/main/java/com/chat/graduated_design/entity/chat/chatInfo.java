package com.chat.graduated_design.entity.chat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;


/**
 * @program: Graduated_Design
 * @description: 聊天信息实体类
 * @author: 常笑男
 * @create: 2022-02-14 10:12
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class chatInfo implements Comparable<chatInfo>{
    @TableId(type = IdType.AUTO)
    private Integer chatNo=null;
    //false未读 true已读
    private boolean readFlag=false;
    private String content=null;
    private Date time;
    //目标用户id
    private Integer dest=null;
    //发送的用户id
    private Integer origin=null;
    //false为聊天 true为文件
    private boolean file=false;
    private String size;

    @Override
    public int compareTo(chatInfo o) {
        return this.time.compareTo(o.getTime());
    }

    public chatInfo(boolean readFlag,String content,Date time,Integer dest,Integer origin,boolean file,String size){
        this.readFlag=readFlag;
        this.content=content;
        this.time=time;
        this.dest=dest;
        this.origin=origin;
        this.file=file;
        this.size=size;
    }
}
