package com.chat.graduated_design.entity.file;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * @program: Graduated_Design
 * @description: 前端查询文件返回的类
 * @author: 常笑男
 * @create: 2022-04-02 10:15
*/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseFile {
    private String uuid;
    private String originName;
    private Integer fileStorageNo;
    private Date time;
    private String size;
}
