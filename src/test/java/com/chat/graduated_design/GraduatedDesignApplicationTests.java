package com.chat.graduated_design;

import com.chat.graduated_design.util.VideoUtil;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

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
