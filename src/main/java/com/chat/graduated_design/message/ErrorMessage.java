package com.chat.graduated_design.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: Graduated_Design
 * @description: 错误信息类
 * @author: 常笑男
 * @create: 2022-02-07 09:50
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorMessage {
    private String email=null;
    private String phone=null;
    private String face=null;

    public void clear(){
        this.email=null;
        this.face=null;
        this.phone=null;
    }
}
