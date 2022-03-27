package com.chat.graduated_design.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @program: Graduated_Design
 * @description: 获取时间
 * @author: 常笑男
 * @create: 2022-03-06 16:49
 **/
public class DateUtil {
    public static Date getCurrentTime() {
        Date date = new Date();
        Date saveDate = null;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            saveDate = format.parse(format.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return saveDate;
    }

    /**
    * 计算两个日期之间相差的天数
    * @param smdate 较小的时间
    * @param bdate  较大的时间
    * @return 相差天数
    * @throws ParseException
    */
    public static int daysBetween(Date smdate,Date bdate) throws ParseException{
        if(smdate==null){
            smdate=DateUtil.getCurrentTime();
        }
        if(bdate==null){
            bdate=DateUtil.getCurrentTime();
        }
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM- dd");
        smdate=sdf.parse(sdf.format(smdate));
        bdate=sdf.parse(sdf.format(bdate));
        Calendar cal = Calendar.getInstance();
        cal.setTime(smdate);
        long time1 = cal.getTimeInMillis ();
        cal.setTime(bdate);
        long time2 = cal.getTimeInMillis ();
        long between_days=(time2-time1)/ (1000*3600*24);
        return Integer.parseInt(String.valueOf (between_days));
    }
}
