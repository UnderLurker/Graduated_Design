package com.chat.graduated_design.entity.file;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;


/**
 * @program: Graduated_Design
 * @description: 文件存储类
 * @author: 常笑男
 * @create: 2022-02-11 17:20
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileStorage {
    @TableId(type = IdType.AUTO)
    private Integer no;
    private Integer id=null;
    private Integer receiveId;
    private String uuid=null;
    private String originname=null;
    private Date datetime;
    private Integer folder;
    private String path=null;
}
