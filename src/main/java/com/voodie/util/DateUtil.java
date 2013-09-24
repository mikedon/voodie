package com.voodie.util;

import java.util.Calendar;
import java.util.Date;

/**
 * Voodie
 * User: MikeD
 */
public class DateUtil {

    public static Date truncateHours(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
}
