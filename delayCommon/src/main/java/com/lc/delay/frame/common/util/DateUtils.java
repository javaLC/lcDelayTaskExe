package com.lc.delay.frame.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author liuchong
 * @version DateUtils.java, v 0.1 2020年02月23日 11:56
 */
public class DateUtils {

    /** yyyy-MM-dd HH:mm:ss */
    public static final String yyyy_MM_dd_HH_mm_ss = "yyyy-MM-dd HH:mm:ss";
    public static final String yyyy_MM_dd_HH_mm_ss_SSS = "yyyy-MM-dd HH:mm:ss SSS";

    /**
     *
     * @param date
     * @param pattern
     * @return
     */
    public static String formatDate(Date date, String pattern) {
        if(date == null) {
            return null;
        }

        try {
            return new SimpleDateFormat(pattern).format(date);
        } catch (Exception e) {
            return null;
        }
    }

}
