package com.a91coding.delayedtask.util;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2017/03/23.
 */

public class TimeHelper {

    public static String getCurrentTime(String pattern) {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        return formatter.format(curDate);
    }

    public static int timeCompare(String firstTime, String secondTime, String pattern) {
        SimpleDateFormat dateFormat;
        //格式化时间
        int result = 0;
        dateFormat = new SimpleDateFormat(pattern);
        try {
            Date firstDateTime = dateFormat.parse(firstTime);
            Date secondDateTime = dateFormat.parse(secondTime);
            /*
            //判断是否大于两天
            if(((endTime.getTime() - firstTime.getTime())/(24*60*60*1000))>=2) {
                Log.v("hi","大于两天");
            }else{
                Log.v("hi","小于两天");
            }
            */
            long r =  firstDateTime.getTime() - secondDateTime.getTime();
            if (r > 0) {
                result = 1;
            } else if (r < 0) {
                result = -1;
            } else {
                result = 0;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static int timeCompare(String date1, String date2) {
        return timeCompare(date1, date2, "yyyy/MM/dd HH:mm:ss");
    }
}
