package com.chat.graduated_design.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chat.graduated_design.entity.chat.chatInfo;
import com.chat.graduated_design.mapper.chatInfoMapper;
import com.chat.graduated_design.service.chatInfoService;
import org.springframework.stereotype.Service;

/**
 * @program: Graduated_Design
 * @description:
 * @author: 常笑男
 * @create: 2022-02-14 10:56
 **/
@Service
public class chatInfoServiceImpl extends ServiceImpl<chatInfoMapper, chatInfo> implements chatInfoService {
}
