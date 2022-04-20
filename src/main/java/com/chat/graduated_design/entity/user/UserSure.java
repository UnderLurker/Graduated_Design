package com.chat.graduated_design.entity.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * @program: Graduated_Design
 * @description: 用户修改密码确认类
 * @author: 常笑男
 * @create: 2022-04-20 19:05
*/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSure {
    private String name=null;
    private String email=null;
    private String phone=null;
    private String password=null;
    private String code=null;
}
