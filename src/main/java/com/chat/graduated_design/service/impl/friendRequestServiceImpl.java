package com.chat.graduated_design.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chat.graduated_design.entity.contact.friendRequest;
import com.chat.graduated_design.mapper.friendRequestMapper;
import com.chat.graduated_design.service.friendRequestService;

import org.springframework.stereotype.Service;

@Service
public class friendRequestServiceImpl extends ServiceImpl<friendRequestMapper, friendRequest> implements friendRequestService{
    
}
