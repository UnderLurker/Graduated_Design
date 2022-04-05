package com.chat.graduated_design.entity.folder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: Graduated_Design
 * @description: 服务器返回的分组数据
 * @author: 常笑男
 * @create: 2022-02-18 17:37
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddFolder {
    private Integer contactid;
    private String headportrait;
    private boolean doNotDisturb;
    private String nickname;
}
