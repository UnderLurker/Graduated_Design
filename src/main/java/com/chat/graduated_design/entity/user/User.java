package com.chat.graduated_design.entity.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class User implements Comparable<User>{
    private String name;
    @TableId(type = IdType.AUTO)
    private Integer id=null;
    private String password=null;
    private String email=null;
    private String gender=null;
    //用户电话号
    private String phone=null;
    private String nickname;
    private boolean active=false;
    private Date loginTime;
    private Date preLoginTime;
//    private boolean inline=false;

    public User(Map<String,Object> map){
        this.id=Integer.parseInt(map.get("id").toString());
        this.password=(String) map.get("password");
        this.name=(String) map.get("name");
        this.nickname=(String) map.get("nickname");
        this.active=(boolean) map.get("active");

        try{
            this.email=(String) map.get("email");
            this.phone=(String) map.get("phone");
            this.gender=(String) map.get("gender");
        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return name.equals(user.name) && email.equals(user.email) && gender.equals(user.gender) && phone.equals(user.phone) && nickname.equals(user.nickname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, email, gender, phone, nickname);
    }

    @Override
    public int compareTo(User o) {
        return this.id-o.getId();
    }
}
