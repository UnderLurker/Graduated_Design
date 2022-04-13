package com.chat.graduated_design.controller.account;

import com.chat.graduated_design.entity.contact.contact;
import com.chat.graduated_design.entity.folder.AddFolder;
import com.chat.graduated_design.entity.folder.CustomFolder;
import com.chat.graduated_design.entity.folder.folderTable;
import com.chat.graduated_design.message.Response;
import com.chat.graduated_design.service.impl.contactServiceImpl;
import com.chat.graduated_design.service.impl.folderTableServiceImpl;
import com.chat.graduated_design.util.ObjectUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @program: Graduated_Design
 * @description: 用户分组控制器
 * @author: 常笑男
 * @create: 2022-02-18 16:54
 **/
@RestController
public class folderController {
    @Autowired
    private folderTableServiceImpl folderTableService;
    @Autowired
    private contactServiceImpl contactService;

    @PostMapping("/folder/put/{id}")
    public Response addFolderById(@PathVariable("id") Integer id, @RequestBody CustomFolder customFolder){
        //为folder_table添加
        folderTable folderEntity=new folderTable(null,id,customFolder.getFolderName());
        folderTableService.save(folderEntity);
        //为contact表添加
        List<contact> contactBatch=new LinkedList<>();
        for(AddFolder item : customFolder.getSelectedPerson()){
            contact person=new contact(null,id, item.getContactid(),
                    customFolder.getFolderName(), item.getHeadportrait().split("/")[2], item.isDoNotDisturb(),item.getNickname(),item.getBlackList());
            contactBatch.add(person);
        }
        contactService.saveBatch(contactBatch);

        return Response.ok("/addFolderById","您已成功添加该分组！！");
    }

    @GetMapping("/folder/delete/{userId}/{classify}")
    public Response deleteFolder(@PathVariable("userId") Integer userId,@PathVariable("classify") String classify){
        boolean flag1=folderTableService.folderDelete(userId, classify);
        boolean flag2=contactService.folderDelete(userId, classify);
        if(!flag1||!flag2) return Response.error("/folder/delete/");
        return Response.ok("/folder/delete/"+userId+classify);
    }

    @PostMapping("/folder/edit/{userId}")
    public Response editFolder(@PathVariable("userId") Integer userId,@RequestBody Map<String,Object> info) throws Exception{
        Map<String,Object> selected=(Map<String,Object>) (info.get("finalFolder"));
        String folderName=(String) selected.get("folderName");
        List<Object> selectedPerson=ObjectUtil.objToList(selected.get("selectedPerson"));
        List<Object> removeFolderContact=ObjectUtil.objToList(info.get("removeFolderContact"));

        //删除移除的联系人
        boolean flag1=contactService.removeBatch(userId, removeFolderContact, folderName);
        //添加联系人关系
        boolean flag2=contactService.saveBatch(userId, selectedPerson, folderName);
        if(!flag2||!flag1) return Response.error("/folder/edit/");

        return Response.ok("");
    }

}
