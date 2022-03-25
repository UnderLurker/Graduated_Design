package com.chat.graduated_design.entity.folder;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: Graduated_Design
 * @description: 分类存储实体类
 * @author: 常笑男
 * @create: 2022-02-14 12:44
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class folderTable {
    @TableId(type = IdType.AUTO)
    private Integer no;
    private Integer userId;
    private String folder;
}
