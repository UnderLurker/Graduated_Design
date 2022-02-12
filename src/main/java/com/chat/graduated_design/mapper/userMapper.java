package com.chat.graduated_design.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chat.graduated_design.entity.user.User;
import org.springframework.stereotype.Repository;

@Repository
public interface userMapper extends BaseMapper<User> {
}
