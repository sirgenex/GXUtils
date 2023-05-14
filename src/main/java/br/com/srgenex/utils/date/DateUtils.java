package br.com.srgenex.utils.date;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;

@SuppressWarnings("unused")
public class DateUtils {

    public static String getToday(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(getDate());
    }

    public static Integer getCurrentHour(){
        return LocalTime.now().getHour();
    }

    public static Date getDate(){
        return new Date();
    }

    public static Integer getCurrentDayOfMonth(){
        return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    }

    public static Integer getCurrentDayOfWeek(){
        return Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
    }

}
