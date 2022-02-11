package com.chat.graduated_design.entity.file;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: Graduated_Design
 * @description: 文件上传实体信息响应类
 * @author: 常笑男
 * @create: 2022-02-10 13:43
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class upLoadFileResponse {
    private String fileName;
    private String fileDownloadUri;
    private String fileType;
    private long size;
}
