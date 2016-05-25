package com.ryan.teapottoday.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.github.xhinliang.lunarcalendar.LunarCalendar;

/**
 * Created by rory9 on 2016/5/15.
 */
public class DateUtils {


    public static String getToday(String flag) {
        return getDateAgo(flag, 0);
    }

    public static String getDateAgo(String flag, int days) {
        //get time
        Date date = new Date(System.currentTimeMillis() - 1000 * 60 * 60 * 24 * days);
        //date.getTime();
        //System.currentTimeMillis();

        //format time 4月13日\n三月初七
        SimpleDateFormat sdf = new SimpleDateFormat("", Locale.SIMPLIFIED_CHINESE);

        //公历
        sdf.applyPattern("MM月dd日");
        String timeSet = sdf.format(date);

        //星期
        sdf.applyPattern("EE");
        String week = sdf.format(date);

        //农历
        sdf.applyPattern("yyyy");
        String year = sdf.format(date);
        sdf.applyPattern("MM");
        String month = sdf.format(date);
        sdf.applyPattern("dd");
        String day = sdf.format(date);

        LunarCalendar lunarCalendar = LunarCalendar.getInstance(Integer.valueOf(year), Integer.valueOf(month), Integer.valueOf(day));
        String lunarMonth = lunarCalendar.getLunarMonth();
        String lunarDay = lunarCalendar.getLunarDay();
        String lunarTimeSet = lunarMonth + "月" + lunarDay;

        switch (flag) {
            case "gregorian": {
                return timeSet;
            }
            case "lunar": {
                return lunarTimeSet;
            }
            case "week": {
                return week;
            }
            default:
            case "all": {
                return timeSet + "\n" + lunarTimeSet;
            }
        }
    }


}
