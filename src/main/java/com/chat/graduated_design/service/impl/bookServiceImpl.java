package com.chat.graduated_design.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chat.graduated_design.entity.book;
import com.chat.graduated_design.mapper.bookMapper;
import com.chat.graduated_design.service.bookService;
import org.springframework.stereotype.Service;

@Service
public class bookServiceImpl extends ServiceImpl<bookMapper, book> implements bookService {
}
