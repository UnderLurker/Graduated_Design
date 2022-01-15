package com.chat.graduated_design.controller;

import com.chat.graduated_design.mapper.bookMapper;
import com.chat.graduated_design.entity.book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class testController {
    @Autowired
    private bookMapper bookmapper;

//    @ResponseBody
    @RequestMapping("/")
    public ModelAndView test(){
        ModelAndView mav=new ModelAndView();
        List<book> res=bookmapper.selectList(null);
        mav.addObject("list",res);
        mav.setViewName("index");
        return mav;
    }
}
