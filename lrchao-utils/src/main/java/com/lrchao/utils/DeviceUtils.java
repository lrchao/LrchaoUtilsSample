package com.lrchao.utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Point;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.text.TextUtils;
import android.view.Display;
import android.view.WindowManager;

/**
 * Description: 设备硬件相关的工具类
 *
 * @author lrc19860926@gmail.com
 * @date 2016/10/14 下午2:33
 */

public final class DeviceUtils {

    private DeviceUtils() {
    }

    /**
     * 获取屏幕宽高
     *
     * @return Point x: width y : height
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public static Point getScreenWH() {
        WindowManager windowManager = (WindowManager) LrchaoUtils.getInstance().getContext()
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point info = new Point();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            display.getSize(info);
        } else {
            info = new Point(display.getWidth(), display.getHeight());
        }
        return info;
    }

    /**
     * 获取mac地址
     */
    @SuppressLint("HardwareIds")
    private static String getMacAddress() {
        String wifiMac = "";
        try {
            Context context = LrchaoUtils.getInstance().getContext();
            WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = wifi.getConnectionInfo();
            wifiMac = info.getMacAddress();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return wifiMac;
    }

    /**
     * 获得去掉冒号的macaddress
     *
     * @param macAddress 原始的mac地址
     */
    private static String getClearlyMacAddress(String macAddress) {
        if (!TextUtils.isEmpty(macAddress)) {
            return macAddress.replace(":", "");
        }
        return "";
    }


    /**
     * 获取设备唯一标识
     * deviceId +
     */
    public static String getUniqueID() {

        Context context = LrchaoUtils.getInstance().getContext();

        String androidId = getAndroidId(context);
        String macAddress = getClearlyMacAddress(getMacAddress());
        String serialNumber = getSerialNumber();
        return MD5Utils.getMD5(androidId + macAddress + serialNumber);
    }

    /**
     * 获取android id
     *
     * @param context Context
     */
    @SuppressLint("HardwareIds")
    private static String getAndroidId(Context context) {

        return android.provider.Settings.Secure.getString(context.getContentResolver(),
                android.provider.Settings.Secure.ANDROID_ID);
    }

    /**
     * 获取Serial Number
     */
    public static String getSerialNumber() {
        return Build.SERIAL;
    }

}
