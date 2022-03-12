package com.chat.graduated_design.util;

import com.chat.graduated_design.message.Response;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @program: Graduated_Design
 * @description: 获取时间
 * @author: 常笑男
 * @create: 2022-03-06 16:49
 **/
public class DateUtil {
    public static Date getCurrentTime(){
        Date date=new Date();
        Date saveDate=null;
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            saveDate=format.parse(format.format(date));
        } catch (
                ParseException e) {
            e.printStackTrace();
        }
        return saveDate;
    }
}
