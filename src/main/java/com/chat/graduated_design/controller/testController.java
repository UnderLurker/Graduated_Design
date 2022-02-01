package com.chat.graduated_design.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chat.graduated_design.mapper.bookMapper;
import com.chat.graduated_design.entity.book;
import com.chat.graduated_design.service.impl.bookServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class testController {
    @Autowired
    private bookServiceImpl service;

////    @ResponseBody
//    @RequestMapping("/")
//    public ModelAndView test(){
//        ModelAndView mav=new ModelAndView();
//        List<book> res=service.list();
//        mav.addObject("list",res);
//        mav.setViewName("index");
//        //查询分页
////        Page<book> page=new Page<>(1,2);//第一页（每页两条）
////        Page<book> resPage=service.page(page,null);
//
//
//        return mav;
//    }
    @RequestMapping("/add")
    public String fun(){
        return "form";
    }

    @RequestMapping(value = "/form",method = RequestMethod.PUT)
    public ModelAndView form(book b){
        ModelAndView mav=new ModelAndView();
        service.save(b);
        mav.setViewName("redirect:/");
        return mav;
    }
    @RequestMapping("/delete/{bid}")
    public String delete(@PathVariable("bid") String bid){
        service.removeById(bid);
        return "redirect:/";
    }

}
