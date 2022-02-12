package com.chat.graduated_design.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chat.graduated_design.entity.file.FileStorage;
import com.chat.graduated_design.entity.user.User;
import com.chat.graduated_design.mapper.fileMapper;
import com.chat.graduated_design.mapper.userMapper;
import com.chat.graduated_design.service.fileDataService;
import com.chat.graduated_design.service.userService;
import org.springframework.stereotype.Service;

/**
 * @program: Graduated_Design
 * @description: 文件存储数据库
 * @author: 常笑男
 * @create: 2022-02-11 17:33
 **/
@Service
public class fileDataServiceImpl  extends ServiceImpl<fileMapper, FileStorage> implements fileDataService {
}
