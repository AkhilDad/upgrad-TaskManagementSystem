package com.upgrad.tms.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {
    public interface DateFormat {
        public static final String YEAR_MONTH_DAY_HOUR_MIN_HYPHEN_SEPARATED = "yyyy-MM-dd HH:mm";
        public static final String DAY_MONTH_YEAR_HOUR_MIN_SLASH_SEPARATED = "dd/MM/yyyy HH:mm";
        public static final String DAY_MONTH_YEAR_SLASH_SEPARATED = "dd/MM/yyyy";
    }

    public static Date getFormattedDate(String dateString, String format) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        try {
            return formatter.parse(dateString);
        } catch (ParseException e) {
            throw e;
        }
    }

    public static String getFormattedDate(Date date, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(date);
    }

    public static boolean isSameDate(Date date1, Date date2) {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date1);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(date2);
        return (calendar1.get(Calendar.DATE) == calendar2.get(Calendar.DATE)
                && calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH)
                && calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR));
    }
}

