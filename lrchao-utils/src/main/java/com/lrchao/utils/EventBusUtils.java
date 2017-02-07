package com.lrchao.utils;

import org.greenrobot.eventbus.EventBus;

/**
 * Description: EventBus 工具类
 * 1.用来Post
 * 2.注册接受对象
 * 3.反注册接受对象
 *
 * @author lrc19860926@gmail.com
 * @date 2016/10/14 下午3:07
 */

public final class EventBusUtils {

    private EventBusUtils() {
    }

    /**
     * 注册
     */
    public static void register(Object object) {

        try {
            if (object != null) {
                EventBus.getDefault().register(object);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    /**
     * 反注册
     */
    public static void unregister(Object object) {
        try {
            if (object != null) {
                EventBus.getDefault().unregister(object);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    /**
     * Post
     */
    public static void post(Object object) {
        try {
            EventBus.getDefault().post(object);
        } catch (Throwable e) {
            e.printStackTrace();
        }

    }
}
