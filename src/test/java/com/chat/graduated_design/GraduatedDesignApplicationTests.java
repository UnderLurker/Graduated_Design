package com.chat.graduated_design;

import com.chat.graduated_design.entity.mailUtil.impl.MailActiveImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GraduatedDesignApplicationTests {

//    @Autowired
//    private bookMapper bookmapper;
//
//    @Test
//    void contextLoads() {
//        System.out.println(("----- selectAll method test ------"));
//        List<book> userList = bookmapper.selectList(null);
//        System.out.println(userList.toString());
//    }
    @Test
    void stringTest(){
        System.out.println("---------------------");
        System.out.println("   123   ".trim());
    }
    @Autowired
    private MailActiveImpl util;
    @Test
    void mailUtilTest(){
        System.out.println("----------------------");
//        util.sendMimeMail("1481167030@qq.com");
        System.out.println(util.toString());
    }
}
