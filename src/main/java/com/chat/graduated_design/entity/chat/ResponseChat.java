package com.chat.graduated_design.entity.chat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseChat extends chatInfo {
    private Object thumbnail;
    private Object fileStorageNo;
    private Object filetype;
    private String shareContactNickName;
    
    public ResponseChat(chatInfo chat,Object thumbnail,Object fileStorageNo,Object filetype,String shareContactNickName){
        super(chat.getChatNo(),
                chat.isReadFlag(),
                chat.getContent(),
                chat.getTime(),
                chat.getDest(),
                chat.getOrigin(),
                chat.isFile(),
                chat.getSize(),
                chat.getShare());
        this.thumbnail=thumbnail;
        this.fileStorageNo=fileStorageNo;
        this.filetype=filetype;
        this.shareContactNickName=shareContactNickName;
    }
}
