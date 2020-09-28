package cn.qmulin.steadyweather.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TimeUitl {
    /**
     *
     * @param time
     * @return
     * @throws ParseException
     */
    public static String dayForWeek(String time) throws ParseException {
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(format.parse(time));
        int dayForWeek;
        String week="";
        dayForWeek=calendar.get(Calendar.DAY_OF_WEEK);
        switch (dayForWeek){
            case 1:
            week="星期日";
            break;
            case 2:
                week = "星期一";
                break;
            case 3:
                week = "星期二";
                break;
            case 4:
                week = "星期三";
                break;
            case 5:
                week = "星期四";
                break;
            case 6:
                week = "星期五";
                break;
            case 7:
                week = "星期六";
                break;
        }
        return week;
        }
}
