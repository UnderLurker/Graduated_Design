package com.chat.graduated_design;

import java.io.FileNotFoundException;

import com.chat.graduated_design.service.impl.MailForgetCodeImpl;
import com.chat.graduated_design.service.impl.contactServiceImpl;
import com.chat.graduated_design.util.VideoUtil;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.ResourceUtils;

@SpringBootTest
class GraduatedDesignApplicationTests {
    @Test
    void fileTest(){
        VideoUtil videoUtil=new VideoUtil("F:\\Program Files\\Graduated_Design\\target\\classes\\static\\image\\mp4\\",
                "F:\\Program Files\\Graduated_Design\\target\\classes\\static\\image\\thumbnail\\");
        String name=videoUtil.CreateJPGByFileType("video/mp4","1.mp4");
        System.out.println(name);
    }

    @Autowired
    private contactServiceImpl contactService;
    @Test
    void sqlTest(){
        String rootPath="";
        try {
            rootPath=ResourceUtils.getURL("classpath:static/image").getPath().replace("%20"," ").substring(1);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println(rootPath);
    }
    @Autowired
    private MailForgetCodeImpl mail;
    @Test
    void forgetText(){
        mail.sendMimeMail("1481167030@qq.com", "1111");
    }
}
