package com.lrchao.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;

import java.lang.reflect.Method;

/**
 * Description: 网络相关的工具类
 *
 * @author lrc19860926@gmail.com
 * @date 2016/10/14 下午2:46
 */

public final class NetworkUtils {

    private static final String TAG = "NetworkUtils";

    /**
     * 链接状态常量
     */
    private static final String TYPE_WIFI = "WIFI";

    private NetworkUtils() {
    }

    /**
     * 判断网络是否可用
     */
    public static boolean isNetworkAvailable() {
        ConnectivityManager connMgr = (ConnectivityManager)
                LrchaoUtils.getInstance().getContext().
                        getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    /**
     * 移动数据网络是否打开
     */
    public static boolean isMobileNetworkAvailable() {
        ConnectivityManager cm;
        cm = (ConnectivityManager) LrchaoUtils.getInstance().getContext().
                getSystemService(Context.CONNECTIVITY_SERVICE);

        Class cmClass = cm.getClass();
        Class[] argClasses = null;
        Object[] argObject = null;
        Boolean isOpen = false;
        try {
            Method method = cmClass.getMethod("getMobileDataEnabled", argClasses);
            isOpen = (Boolean) method.invoke(cm, argObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isOpen;
    }

    /**
     * @param context Context
     * @return true 已连接WIFI
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static boolean isWifiConnected(Context context) {

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (OSUtils.hasLollipop()) {
            Network[] networks = connectivityManager.getAllNetworks();
            if (networks != null && networks.length > 0) {
                int size = networks.length;
                for (int i = 0; i < size; i++) {
                    Log.d(TAG, "=====状态====" + connectivityManager.getNetworkInfo(networks[i]).getState());
                    Log.d(TAG, "=====类型====" + connectivityManager.getNetworkInfo(networks[i]).getTypeName());
                    if (connectivityManager.getNetworkInfo(networks[i]).getState() == NetworkInfo.State.CONNECTED) {

                        if (TYPE_WIFI.equals(connectivityManager.getNetworkInfo(networks[i]).getTypeName())) {
                            return true;
                        }

                    }
                }
            }
        } else {
            NetworkInfo[] networkInfos = connectivityManager.getAllNetworkInfo();
            if (networkInfos != null && networkInfos.length > 0) {
                int size = networkInfos.length;
                for (int i = 0; i < size; i++) {
                    Log.d(TAG, "=====状态====" + networkInfos[i].getState());
                    Log.d(TAG, "=====类型====" + networkInfos[i].getTypeName());
                    if (networkInfos[i].getState() == NetworkInfo.State.CONNECTED) {
                        if (TYPE_WIFI.equals(networkInfos[i].getTypeName())) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}
