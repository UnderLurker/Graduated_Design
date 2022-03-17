package com.chat.graduated_design.entity.file;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: Graduated_Design
 * @description: 视频首帧图片实体类
 * @author: 常笑男
 * @create: 2022-03-10 16:29
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class videoThumbnail {
    @TableId
    private Integer chatNo;
    private String uuid;
    private Integer fileStorageNo;
    private String type=null;
}
