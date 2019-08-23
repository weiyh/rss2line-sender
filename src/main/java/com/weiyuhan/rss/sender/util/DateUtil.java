package com.weiyuhan.rss.sender.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateUtil {
    public static final String YYYY_MM_DD_HH_MM_SS_TZ = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    public static final String TIMEZONE = "GMT+8";

    public static Date addHourOffsetToDate(Date calendarDate, int offset) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(calendarDate);
        calendar.add(Calendar.HOUR, offset);
        return calendar.getTime();
    }

    public static Date parseToDate(String date) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS_TZ);
        formatter.setTimeZone(TimeZone.getTimeZone(TIMEZONE));
        return formatter.parse(date);
    }
}
