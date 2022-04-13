package com.chat.graduated_design.entity.contact;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: Graduated_Design
 * @description: 联系人类
 * @author: 常笑男
 * @create: 2022-02-14 10:59
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class contact implements Comparable<contact>{

    public static final int NORMAL = 0;     //正常
    public static final int ACTIVE = 1;     //主动拉黑
    public static final int PASSIVE = 2;    //被拉黑
    public static final int MUTUAL = 3;     //都拉黑

    @TableId(type = IdType.AUTO)
    private Integer contactNo=null;
    private Integer userid;
    private Integer contactid;
    private String folder;
    private String headportrait=null;
    private boolean doNotDisturb=false;
    //用户为联系人所设置的昵称
    private String name=null;
    //黑名单
    private Integer blackList;

    @Override
    public int compareTo(contact o) {
        return this.contactid-o.getContactid();
    }
}
