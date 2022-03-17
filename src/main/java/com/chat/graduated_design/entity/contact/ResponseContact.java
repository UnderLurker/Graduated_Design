package com.chat.graduated_design.entity.contact;

import com.chat.graduated_design.entity.chat.ResponseChat;
import com.chat.graduated_design.entity.chat.chatInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @program: Graduated_Design
 * @description: 返回前端的联系人信息
 * @author: 常笑男
 * @create: 2022-02-16 10:07
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseContact extends contact{
    private String nickname;
    private String phone;
    private List<ResponseChat> chatInfoList;

    public ResponseContact(contact person,String nickname,String phone,List<ResponseChat> chatInfoList){
        super(person.getContactNo(),
                person.getUserid(),
                person.getContactid(),
                person.getFolder(),
                person.getHeadportrait(),
                person.isDoNotDisturb(),
                person.getUnread());
        this.nickname=nickname;
        this.phone=phone;
        this.chatInfoList=chatInfoList;
    }

}
