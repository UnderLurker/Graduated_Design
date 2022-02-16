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
public class chatInfo {
    private Integer userId;
    private Integer contactId;
    private boolean readFlag=false;
    private String content=null;
    private Date time;
    //false是user的信息 true是contact的信息
    private boolean belong=false;
}
