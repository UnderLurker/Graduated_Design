package com.chat.graduated_design.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chat.graduated_design.entity.file.FileStorage;
import org.springframework.stereotype.Repository;

@Repository
public interface fileMapper extends BaseMapper<FileStorage> {
}
