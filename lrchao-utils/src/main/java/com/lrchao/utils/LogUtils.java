package com.lrchao.utils;

import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

/**
 * Description: Log工具类
 *
 * @author lrc19860926@gmail.com
 * @date 2016/10/17 下午4:48
 */

public final class LogUtils {

    private static final String TAG = "Jia";

    private static boolean sIsDebug = true;

    static {
        com.orhanobut.logger.Logger.init(TAG)
                .methodCount(1)
                .methodOffset(1);
    }

    private LogUtils() {
    }

    /**
     * @param isDebug true : debug model
     */
    public static void debug(boolean isDebug) {
        sIsDebug = isDebug;
    }

    /**
     * AccessibilityService
     *
     * @param event              事件
     * @param rootInActiveWindow 根节点
     */
    public static void accessibilityEvent(AccessibilityEvent event, AccessibilityNodeInfo rootInActiveWindow) {

        int eventType = event.getEventType();
        String type = "";
        CharSequence className = "";
        CharSequence packageName = "";

        switch (eventType) {
            case AccessibilityEvent.TYPE_VIEW_CLICKED:
                type = "TYPE_VIEW_CLICKED";
                className = event.getClassName();
                packageName = getRootInActiveWindowPackageName(rootInActiveWindow);
                break;
            case AccessibilityEvent.TYPE_VIEW_LONG_CLICKED:
                type = "TYPE_VIEW_LONG_CLICKED";
                className = event.getClassName();
                packageName = getRootInActiveWindowPackageName(rootInActiveWindow);
                break;
            case AccessibilityEvent.TYPE_VIEW_SELECTED:
                type = "TYPE_VIEW_SELECTED";
                className = event.getClassName();
                packageName = getRootInActiveWindowPackageName(rootInActiveWindow);
                break;
            case AccessibilityEvent.TYPE_VIEW_FOCUSED:
                type = "TYPE_VIEW_FOCUSED";
                className = event.getClassName();
                packageName = getRootInActiveWindowPackageName(rootInActiveWindow);
                break;
            case AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED:
                type = "TYPE_VIEW_TEXT_CHANGED";
                className = event.getClassName();
                packageName = getRootInActiveWindowPackageName(rootInActiveWindow);
                break;
            case AccessibilityEvent.TYPE_VIEW_TEXT_SELECTION_CHANGED:
                type = "TYPE_VIEW_TEXT_SELECTION_CHANGED";
                className = event.getClassName();
                packageName = getRootInActiveWindowPackageName(rootInActiveWindow);
                break;
            case AccessibilityEvent.TYPE_VIEW_TEXT_TRAVERSED_AT_MOVEMENT_GRANULARITY:
                type = "TYPE_VIEW_TEXT_TRAVERSED_AT_MOVEMENT_GRANULARITY";
                className = event.getClassName();
                packageName = getRootInActiveWindowPackageName(rootInActiveWindow);
                break;
            case AccessibilityEvent.TYPE_VIEW_SCROLLED:
                type = "TYPE_VIEW_SCROLLED";
                className = event.getClassName();
                packageName = getRootInActiveWindowPackageName(rootInActiveWindow);
                break;
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                type = "TYPE_WINDOW_STATE_CHANGED";
                className = event.getClassName();
                packageName = getRootInActiveWindowPackageName(rootInActiveWindow);
                break;
            case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED:
                type = "TYPE_WINDOW_CONTENT_CHANGED";
                className = event.getClassName();
                packageName = getRootInActiveWindowPackageName(rootInActiveWindow);
                break;
            case AccessibilityEvent.TYPE_WINDOWS_CHANGED:
                type = "TYPE_WINDOWS_CHANGED";
                className = event.getClassName();
                packageName = getRootInActiveWindowPackageName(rootInActiveWindow);
                break;
            case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:
                type = "TYPE_NOTIFICATION_STATE_CHANGED";
                // rootInActiveWindow == null
                className = event.getClassName();
                break;
            case AccessibilityEvent.TYPE_VIEW_HOVER_ENTER:
                type = "TYPE_VIEW_HOVER_ENTER";
                className = event.getClassName();
                packageName = getRootInActiveWindowPackageName(rootInActiveWindow);
                break;
            case AccessibilityEvent.TYPE_VIEW_HOVER_EXIT:
                type = "TYPE_VIEW_HOVER_EXIT";
                className = event.getClassName();
                packageName = getRootInActiveWindowPackageName(rootInActiveWindow);
                break;
            case AccessibilityEvent.TYPE_TOUCH_INTERACTION_START:
                type = "TYPE_TOUCH_INTERACTION_START";
                className = event.getClassName();
                packageName = getRootInActiveWindowPackageName(rootInActiveWindow);
                break;
            case AccessibilityEvent.TYPE_TOUCH_INTERACTION_END:
                type = "TYPE_TOUCH_INTERACTION_END";
                className = event.getClassName();
                packageName = getRootInActiveWindowPackageName(rootInActiveWindow);
                break;
            default:
                break;
        }

        LogUtils.i("accessibilityEvent=type：" + type +
                " className：" + className +
                " packageName：" + packageName);
    }

    private static CharSequence getRootInActiveWindowPackageName(AccessibilityNodeInfo rootInActiveWindow) {
        if (rootInActiveWindow != null) {
            return rootInActiveWindow.getPackageName();
        }
        return "";
    }

    //====================================
    // verbose
    //====================================

    /**
     * 输出v
     *
     * @param format 格式化string
     * @param args   参数数组
     */
    public static void v(String format, Object... args) {
        if (sIsDebug) {
            com.orhanobut.logger.Logger.v(format, args);
        }
    }

    /**
     * 输出v
     *
     * @param tag 标记
     * @param msg 输出呢呢绒
     */
    public static void v(String tag, String msg) {
        if (sIsDebug) {
            com.orhanobut.logger.Logger.t(tag);
            com.orhanobut.logger.Logger.v(msg);
        }
    }

    //====================================
    // debug
    //====================================

    /**
     * 输出d
     *
     * @param format 格式化string
     * @param args   参数数组
     */
    public static void d(String format, Object... args) {
        if (sIsDebug) {
            com.orhanobut.logger.Logger.d(format, args);
        }
    }

    /**
     * 输出d
     *
     * @param tag 标记
     * @param msg 输出呢呢绒
     */
    public static void d(String tag, String msg) {
        if (sIsDebug) {
            setTag(tag);
            com.orhanobut.logger.Logger.d(msg);
        }
    }

    //====================================
    // info
    //====================================

    /**
     * 输出i
     *
     * @param format 格式化string
     * @param args   参数数组
     */
    public static void i(String format, Object... args) {
        if (sIsDebug) {
            com.orhanobut.logger.Logger.i(format, args);
        }
    }

    /**
     * 输出i
     *
     * @param tag 标记
     * @param msg 输出呢呢绒
     */
    public static void i(String tag, String msg) {
        if (sIsDebug) {
            setTag(tag);
            com.orhanobut.logger.Logger.i(msg);
        }
    }

    //====================================
    // warn
    //====================================

    /**
     * 输出w
     *
     * @param format 格式化string
     * @param args   参数数组
     */
    public static void w(String format, Object... args) {
        if (sIsDebug) {
            com.orhanobut.logger.Logger.w(format, args);
        }
    }

    /**
     * 输出w
     *
     * @param tag 标记
     * @param msg 输出呢呢绒
     */
    public static void w(String tag, String msg) {
        if (sIsDebug) {
            setTag(tag);
            com.orhanobut.logger.Logger.w(msg);
        }
    }

    //====================================
    // error
    //====================================

    /**
     * 输出e
     *
     * @param format 格式化string
     * @param args   参数数组
     */
    public static void e(String format, Object... args) {
        if (sIsDebug) {
            com.orhanobut.logger.Logger.e(format, args);
        }
    }

    /**
     * 输出e
     *
     * @param tag 标记
     * @param msg 输出呢呢绒
     */
    public static void e(String tag, String msg) {
        if (sIsDebug) {
            setTag(tag);
            com.orhanobut.logger.Logger.e(msg);
        }
    }

    /**
     * 输出e
     *
     * @param throwable Throwable
     * @param format    格式化string
     * @param args      参数数组
     */
    public static void e(Throwable throwable, String format, Object... args) {
        if (sIsDebug) {
            com.orhanobut.logger.Logger.e(throwable, format, args);
        }
    }

    //=================WTF===================

    /**
     * 输出错误
     *
     * @param format 格式化string
     * @param args   参数数组
     */
    public static void wtf(String format, Object... args) {
        if (sIsDebug) {
            com.orhanobut.logger.Logger.wtf(format, args);
        }
    }

    /**
     * 打印crash
     *
     * @param throwable Throwable
     */
    public static void wtf(Throwable throwable) {
        if (sIsDebug) {
            Log.wtf(TAG, throwable);
        }
    }

    //=================JSON===================

    /**
     * 输出JSON歌是
     *
     * @param jsonStr JSON字符串
     */
    public static void json(String jsonStr) {
        if (sIsDebug && !TextUtils.isEmpty(jsonStr)) {
            try {
                com.orhanobut.logger.Logger.json(jsonStr);
            } catch (Exception e) {
                e.printStackTrace();
            } catch (Error error) {
                error.printStackTrace();
            }

        }
    }

    //=================private===================

    /**
     * 设置后的第一次调用有效
     *
     * @param tag Tag,拼接在默认的tag后面
     */
    private static void setTag(String tag) {
        com.orhanobut.logger.Logger.t(tag);
    }
}
