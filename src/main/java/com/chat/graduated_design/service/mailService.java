package com.chat.graduated_design.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;

public interface mailService {
    /**
     * 发送复杂邮件
     * @param toEmail 目标邮箱
     */
    default public void sendMimeMail(String toEmail){

    }
    /**
     * 发送复杂邮件
     * @param toEmail 目标邮箱
     * @param id 用户的唯一id
     */
    default public void sendMimeMailWithId(String toEmail,Integer id){

    }

    /**
     * 发送简单邮件
     * @param toEmail 目标邮箱
     */
    default public void sendSimpleMail(String toEmail){

    }
    /**
     * 发送复杂邮件
     * @param toEmail 目标邮箱
     * @param id 用户的唯一id
     */
    default public void sendSimpleMailWithId(String toEmail,Integer id){

    }

}
