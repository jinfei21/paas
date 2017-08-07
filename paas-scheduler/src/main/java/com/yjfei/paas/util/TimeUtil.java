package com.yjfei.paas.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.time.DurationFormatUtils;

public class TimeUtil {


    private static final String DURATION_FORMAT = "mm:ss.S";

    public static String duration(final long start) {
        return DurationFormatUtils.formatDuration(Math.max(System.currentTimeMillis() - start, 0), DURATION_FORMAT);
    }


    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static String convertDateToString(Date date) {
        if (date != null)
            return sdf.format(date);
        return null;
    }

    /**
     * need further consideration for perfection
     */
    public static Date convertStringToDate(String string) {
        try {
            return fmt.parse(string);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

}
