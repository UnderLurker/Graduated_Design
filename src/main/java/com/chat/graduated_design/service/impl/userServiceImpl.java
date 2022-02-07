package com.chat.graduated_design.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chat.graduated_design.entity.User;
import com.chat.graduated_design.mapper.userMapper;
import com.chat.graduated_design.service.userService;
import org.springframework.stereotype.Service;

@Service
public class userServiceImpl extends ServiceImpl<userMapper, User> implements userService {
}
