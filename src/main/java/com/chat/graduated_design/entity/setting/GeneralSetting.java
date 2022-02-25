package com.chat.graduated_design.entity.setting;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: Graduated_Design
 * @description: 通用设置实体类
 * @author: 常笑男
 * @create: 2022-02-20 12:22
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GeneralSetting {
    private Integer id;
    private Integer fontSize=16;
    private boolean sendStyle=true;
}
