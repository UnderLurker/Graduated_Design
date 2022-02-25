package com.chat.graduated_design.entity.folder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @program: Graduated_Design
 * @description: 自定义服务器返回数据结构
 * @author: 常笑男
 * @create: 2022-02-18 17:57
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomFolder {
    private String folderName;
    private List<AddFolder> selectedPerson;
}
