package com.chat.graduated_design.service.impl;

import com.chat.graduated_design.service.mailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * @program: Graduated_Design
 * @description: 邮件激活工具类
 * @author: 常笑男
 * @create: 2022-02-09 10:49
 **/
@Component
public class MailActiveImpl implements mailService {
    @Value("${mail.from}")
    private String mailFrom;

    @Value("${mail.activeSubject}")
    private String activeSubject;

    @Value("${mail.preActiveContent}")
    private String preActiveContent;

    @Value("${mail.subActiveContent}")
    private String subActiveContent;

    @Value("${mail.activeContent}")
    private String activeContent;

    @Autowired
    private JavaMailSenderImpl javaMailSenderImpl;

    @Override
    public void sendMimeMail(String toEmail,String code) {
        MimeMessage mimeMessage = javaMailSenderImpl.createMimeMessage();
        try {
            // 开启文件上传
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            // 设置文件主题
            mimeMessageHelper.setSubject(this.activeSubject);
            // 设置文件内容 第二个参数设置是否支持html
            mimeMessageHelper.setText(this.activeContent, true);
            // 设置发送到的邮箱
            mimeMessageHelper.setTo(toEmail);
            // 设置发送人和配置文件中邮箱一致
            mimeMessageHelper.setFrom(this.mailFrom);
            // 上传附件
            // mimeMessageHelper.addAttachment("", new File(""));
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        javaMailSenderImpl.send(mimeMessage);
    }

    @Override
    public void sendMimeMailWithId(String toEmail, Integer id) {
        MimeMessage mimeMessage = javaMailSenderImpl.createMimeMessage();
        try {
            // 开启文件上传
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            // 设置文件主题
            mimeMessageHelper.setSubject(this.activeSubject);
            // 设置文件内容 第二个参数设置是否支持html
            mimeMessageHelper.setText(this.preActiveContent+id.toString()+this.subActiveContent, true);
            // 设置发送到的邮箱
            mimeMessageHelper.setTo(toEmail);
            // 设置发送人和配置文件中邮箱一致
            mimeMessageHelper.setFrom(this.mailFrom);
            // 上传附件
            // mimeMessageHelper.addAttachment("", new File(""));
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        javaMailSenderImpl.send(mimeMessage);
    }

    @Override
    public void sendSimpleMail(String toEmail) {
        SimpleMailMessage simpleMailMessage=setSimpleMail(toEmail,this.activeContent);
        javaMailSenderImpl.send(simpleMailMessage);
    }

    @Override
    public void sendSimpleMailWithId(String toEmail, Integer id) {
        SimpleMailMessage simpleMailMessage=setSimpleMail(toEmail,this.preActiveContent+id.toString()+this.subActiveContent);
        javaMailSenderImpl.send(simpleMailMessage);
    }

    private SimpleMailMessage setSimpleMail(String toEmail,String content){
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        // 设置邮件主题
        simpleMailMessage.setSubject(this.activeSubject);
        // 设置要发送的邮件内容
        simpleMailMessage.setText(content);
        // 要发送的目标邮箱
        simpleMailMessage.setTo(toEmail);
        // 发送者邮箱和配置文件中的邮箱一致
        simpleMailMessage.setFrom(this.mailFrom);
        return simpleMailMessage;
    }

    @Override
    public String toString() {
        this.activeContent=this.preActiveContent+'1'+this.subActiveContent;
        return "mailActiveUtil{" +
                "mailFrom='" + mailFrom + '\'' +
                ", activeSubject='" + activeSubject + '\'' +
                ", preActiveContent='" + preActiveContent + '\'' +
                ", subActiveContent='" + subActiveContent + '\'' +
                ", activeContent='" + activeContent + '\'' +
                ", javaMailSenderImpl=" + javaMailSenderImpl +
                '}';
    }
}
