package com.chat.graduated_design;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.chat.graduated_design.mapper")
public class GraduatedDesignApplication {

    public static void main(String[] args) {
        SpringApplication.run(GraduatedDesignApplication.class, args);
    }

}
