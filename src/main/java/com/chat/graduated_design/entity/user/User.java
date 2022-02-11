package com.chat.graduated_design.entity.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class User {
    private String name;
    private Integer id=null;
    private String password=null;
    private String email=null;
    private String gender=null;
    //用户电话号
    private String phone=null;
    private String nickname;
    private boolean active=false;

    private static Integer count=1;

    public static Integer getCount() {
        return count;
    }

    public static void setCount(Integer count) {
        User.count = count;
    }

}
