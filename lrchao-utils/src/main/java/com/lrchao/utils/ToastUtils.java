package com.lrchao.utils;

import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * Description: Toast帮助类
 *
 * @author lrc19860926@gmail.com
 * @date 2016/10/14 下午2:57
 */

public final class ToastUtils {

    private ToastUtils() {
    }

    /**
     * 显示系统的toast
     *
     * @param resId strings.xml
     */
    public static void show(@StringRes int resId) {
        Toast.makeText(LrchaoUtils.getInstance().getContext(), resId, Toast.LENGTH_LONG).show();
    }

    /**
     * 显示系统的toast
     *
     * @param text CharSequence
     */
    public static void show(CharSequence text) {
        if (!TextUtils.isEmpty(text)) {
            Toast.makeText(LrchaoUtils.getInstance().getContext(), text, Toast.LENGTH_LONG).show();
        }
    }
}
