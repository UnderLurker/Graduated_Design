package com.chat.graduated_design.entity.contact;

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
    private Integer userid;
    private Integer contactid;
    private String folder;
    private Integer unread=0;
    private String headportrait=null;
    private boolean doNotDisturb=false;

    @Override
    public int compareTo(contact o) {
        return this.contactid-o.getContactid();
    }
}
