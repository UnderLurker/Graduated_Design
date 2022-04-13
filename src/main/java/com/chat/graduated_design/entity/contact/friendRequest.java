package com.chat.graduated_design.entity.contact;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * @program: Graduated_Design
 * @description: 好友请求类
 * @author: 常笑男
 * @create: 2022-03-28 12:25
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class friendRequest {
    @TableId(type = IdType.AUTO)
    private Integer no=null;
    private Integer requestId;
    private Integer receiveId;
}
