package com.chat.graduated_design.entity.chat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

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

    @Override
    public int compareTo(chatInfo o) {
        return this.time.compareTo(o.getTime());
    }
}
