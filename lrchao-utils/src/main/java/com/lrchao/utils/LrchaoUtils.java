package com.lrchao.utils;

import android.content.Context;

/**
 * Description: 使用LrchaoUtils 必须先初始化
 *
 * @author lrc19860926@gmail.com
 * @date 2016/10/14 上午10:48
 */

public class LrchaoUtils {

    private static LrchaoUtils sInstance;

    private Context mContext;

    public static LrchaoUtils getInstance() {
        synchronized (LrchaoUtils.class) {
            if (sInstance == null) {
                sInstance = new LrchaoUtils();
            }
        }
        return sInstance;
    }

    /**
     * 在
     *
     * @param context Application
     */
    public void init(Context context) {
        mContext = context;
    }

    public Context getContext() {
        return mContext;
    }

}
