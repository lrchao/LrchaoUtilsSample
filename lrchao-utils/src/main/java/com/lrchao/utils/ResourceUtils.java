package com.lrchao.utils;

import android.annotation.TargetApi;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ArrayRes;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

/**
 * Description: 操作资源文件的工具类
 *
 * @author lrc19860926@gmail.com
 * @date 2016/10/14 下午2:49
 */

public final class ResourceUtils {


    private ResourceUtils() {
    }

    /**
     * 获取arrays.xml中的字符串数组
     *
     * @param arrId Resource id  R.array.xxx
     * @return string数组
     */
    public static String[] getStringArray(@ArrayRes int arrId) {
        return LrchaoUtils.getInstance().getContext().getResources().getStringArray(arrId);
    }

    /**
     * 获取arrays.xml中的int数组
     *
     * @param arrId Resource id  R.array.xxx
     * @return int数组
     */
    public static int[] getIntArray(@ArrayRes int arrId) {
        return LrchaoUtils.getInstance().getContext().getResources().getIntArray(arrId);
    }

    /**
     * 获取strings.xml的文本
     *
     * @param resId      strings.xml
     * @param formatArgs 替换的参数
     * @return String
     */
    public static String getString(@StringRes int resId, Object... formatArgs) {
        return LrchaoUtils.getInstance().getContext().getString(resId, formatArgs);
    }

    /**
     * 获取color 颜色
     *
     * @param colorId colors.xml
     * @return color
     */
    @TargetApi(Build.VERSION_CODES.M)
    public static int getColor(@ColorRes int colorId) {
        if (OSUtils.hasM()) {
            return LrchaoUtils.getInstance().getContext().getResources().getColor(colorId, null);
        } else {
            return LrchaoUtils.getInstance().getContext().getResources().getColor(colorId);
        }
    }

    /**
     * 获取dimen对应的值
     *
     * @param dimenId dimenId
     */
    public static float getDimen(@DimenRes int dimenId) {
        return LrchaoUtils.getInstance().getContext().getResources().getDimensionPixelSize(dimenId);
    }

    /**
     * 获取Dawable
     *
     * @param drawableId 资源文件ID
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static Drawable getDrawable(@DrawableRes int drawableId) {
        if (OSUtils.hasLollipop()) {
            return LrchaoUtils.getInstance().getContext().getResources().getDrawable(drawableId,
                    LrchaoUtils.getInstance().getContext().getTheme());
        } else {
            return LrchaoUtils.getInstance().getContext().getResources().getDrawable(drawableId);
        }

    }
}
