package com.lrchao.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Description: 随机数工具类
 *
 * @author lrc19860926@gmail.com
 * @date 2016/11/3 下午2:50
 */

public final class RandomUtils {

    private RandomUtils() {
    }

    /**
     * 获取中间的随机数
     *
     * @param min 最小值
     * @param max 最大值
     */
    public static int getRandomInt(int min, int max) {
        return new Random().nextInt(max - min) + min;
    }

    /**
     * 从string数组中随机取一个
     */
    public static String getRandomString(String[] arr) {
        int index = getRandomInt(0, arr.length);
        return arr[index];
    }

    public static <T> T getRandomForList(List<T> list) {
        int index = getRandomInt(0, list.size());
        return list.get(index);
    }

    /**
     * 生成随机字符串
     *
     * @param length length表示生成字符串的长度
     */
    public static String getRandomString(int length) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    public static String getRandomInt(int length) {
        String base = "0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 获取随机的时间
     */
    public static Date getRandomDate(String beginDate, String endDate) {

        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date start = format.parse(beginDate);// 构造开始日期
            Date end = format.parse(endDate);// 构造结束日期
            // getTime()表示返回自 1970 年 1 月 1 日 00:00:00 GMT 以来此 Date 对象表示的毫秒数。
            if (start.getTime() >= end.getTime()) {
                return null;
            }
            long date = random(start.getTime(), end.getTime());

            return new Date(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static long random(long begin, long end) {
        long rtn = begin + (long) (Math.random() * (end - begin));
        // 如果返回的是开始时间和结束时间，则递归调用本函数查找随机值
        if (rtn == begin || rtn == end) {
            return random(begin, end);
        }
        return rtn;
    }
}
