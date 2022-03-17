package com.chat.graduated_design;

import com.chat.graduated_design.entity.contact.ResponseContact;
import com.chat.graduated_design.entity.contact.contact;
import com.chat.graduated_design.entity.user.User;
import com.chat.graduated_design.service.impl.*;
import com.chat.graduated_design.util.VideoUtil;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.ResourceUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

@SpringBootTest
class GraduatedDesignApplicationTests {
    @Test
    void fileTest(){
        VideoUtil videoUtil=new VideoUtil("F:\\Program Files\\Graduated_Design\\target\\classes\\static\\image\\mp4\\",
                "F:\\Program Files\\Graduated_Design\\target\\classes\\static\\image\\thumbnail\\");
        String name=videoUtil.CreateJPGByFileType("video/mp4","1.mp4");
        System.out.println(name);
    }
}
