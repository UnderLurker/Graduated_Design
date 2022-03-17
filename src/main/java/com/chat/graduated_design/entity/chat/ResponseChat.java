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
    
    public ResponseChat(chatInfo chat,Object thumbnail,Object fileStorageNo,Object filetype){
        super(chat.getChatNo(),chat.isReadFlag(),chat.getContent(),chat.getTime(),chat.getDest(),chat.getOrigin(),chat.isFile(),chat.getSize());
        this.thumbnail=thumbnail;
        this.fileStorageNo=fileStorageNo;
        this.filetype=filetype;
    }
}
