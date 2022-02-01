package com.chat.graduated_design.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@TableName("book")
public class book {
    @TableId
    private String bid;
    @TableField(exist = true)
    private String bname;
    private double price;
    private String author;
    private String image;
    private String cid;
}
