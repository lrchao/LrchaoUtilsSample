package com.lrchao.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Description: 处理时间，日期等工具类
 *
 * @author lrc19860926@gmail.com
 * @date 2016/10/14 上午11:06
 */

public final class DateUtils {

    private static final String SD_YYYY_MM_DD_HYPHEN = "yyyy-MM-dd";
    private static final String SD_YYYY_MM_DD_HH_MM_SS_HYPHEN = "yyyy-MM-dd-HH-mm-ss";
    private static final String SD_YYYY_MM_DD_HH_MM_SS_CN = "yyyy年MM月dd日HH时mm分";
    private static final String SD_YYYY_MM_DD_HH_MM_SS_COLON = "yyyy-MM-dd HH:mm:ss";

    private DateUtils() {

    }

    /**
     * @param time long
     * @return yyyy-MM-dd
     */
    public static String getYMDHyphen(long time) {
        return format(SD_YYYY_MM_DD_HYPHEN, new Date(time));
    }

    /**
     * @param time long
     * @return yyyy-MM-dd-HH-mm-ss
     */
    public static String getYMDHMSHyphen(long time) {
        return format(SD_YYYY_MM_DD_HH_MM_SS_HYPHEN, new Date(time));
    }

    /**
     * @param date Date
     * @return yyyy年MM月dd日HH时mm分
     */
    public static String getYMDHMSCN(Date date) {
        return format(SD_YYYY_MM_DD_HH_MM_SS_CN, date);
    }

    /**
     * @param time long
     * @return yyyy年MM月dd日HH时mm分
     */
    public static String getYMDHMSCN(long time) {
        return format(SD_YYYY_MM_DD_HH_MM_SS_CN, new Date(time));
    }

    /**
     * @param time long
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String getYMDHMSColon(long time) {
        return format(SD_YYYY_MM_DD_HH_MM_SS_COLON, new Date(time));
    }


    /**
     * 格式化时间使用
     *
     * @param type 格式化样式
     * @param date 格式化的时间
     * @return 格式化后的时间字符串
     */
    private static String format(String type, Date date) {
        SimpleDateFormat format = new SimpleDateFormat(type, Locale.US);
        return format.format(date);
    }

    /**
     * 计算两个时间date间的时间差，按天 小时 显示
     *
     * @param duration 秒
     * @return String
     */
    public static String getResidueTime(long duration) {
        long minutesInMilli = 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = duration / daysInMilli;
        duration = duration % daysInMilli;
        long elapsedHours = duration / hoursInMilli;

        return elapsedDays + "天" + elapsedHours + "小时";

    }

    /**
     * 获取今日24点时间戳
     *
     * @return long
     */
    public static long getTimesTodaymorning() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 24);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }
}
