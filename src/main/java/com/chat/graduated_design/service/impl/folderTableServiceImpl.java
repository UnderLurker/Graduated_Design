package com.chat.graduated_design.service.impl;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chat.graduated_design.entity.folder.folderTable;
import com.chat.graduated_design.mapper.folderTableMapper;
import com.chat.graduated_design.service.folderTableService;
import org.springframework.stereotype.Service;

/**
 * @program: Graduated_Design
 * @description:
 * @author: 常笑男
 * @create: 2022-02-14 13:01
 **/
@Service
public class folderTableServiceImpl  extends ServiceImpl<folderTableMapper, folderTable> implements folderTableService {
    /**
     * 
     * @param id
     * @return 用户分类列表
     */
    public List<Map<String,Object>> selectUserClassify(Integer id){
        QueryWrapper<folderTable> folderTableQueryWrapper=new QueryWrapper<>();
        folderTableQueryWrapper.eq("user_id",id);
        return this.listMaps(folderTableQueryWrapper);
    }
    
}
