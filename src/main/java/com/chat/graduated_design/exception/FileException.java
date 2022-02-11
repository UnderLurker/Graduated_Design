package com.chat.graduated_design.exception;

/**
 * @program: Graduated_Design
 * @description: 异常信息类
 * @author: 常笑男
 * @create: 2022-02-10 13:40
 **/
public class FileException extends RuntimeException{
    public FileException(String message) {
        super(message);
    }

    public FileException(String message, Throwable cause) {
        super(message, cause);
    }
}
