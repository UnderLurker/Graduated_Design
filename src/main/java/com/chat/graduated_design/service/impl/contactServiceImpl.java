package com.chat.graduated_design.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chat.graduated_design.entity.contact.contact;
import com.chat.graduated_design.mapper.contactMapper;
import com.chat.graduated_design.service.contactService;
import org.springframework.stereotype.Service;

/**
 * @program: Graduated_Design
 * @description:
 * @author: 常笑男
 * @create: 2022-02-14 11:35
 **/
@Service
public class contactServiceImpl extends ServiceImpl<contactMapper, contact> implements contactService {
}
