package com.chat.graduated_design;

import com.chat.graduated_design.mapper.bookMapper;
import com.chat.graduated_design.entity.book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class GraduatedDesignApplicationTests {

    @Autowired
    private bookMapper bookmapper;

    @Test
    void contextLoads() {
        System.out.println(("----- selectAll method test ------"));
        List<book> userList = bookmapper.selectList(null);
        System.out.println(userList.toString());
    }

}
