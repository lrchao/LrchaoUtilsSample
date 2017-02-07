package com.lrchao.utils;

import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.provider.Settings;
import android.text.TextUtils;

import java.io.File;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Description:  应用相关的工具类
 * 例如获取包名，应用图标等
 *
 * @author lrc19860926@gmail.com
 * @date 2016/10/14 上午10:44
 */

public final class AppUtils {

    private static final String MEM_TOTAL = "MemTotal:";
    private static final String MEM_FREE = "MemFree:";

    private AppUtils() {
    }

    /**
     * 获取目标apk的信息
     */
    public static String getPackageNameFromApk(File apkFile) {
        String packageName = "";
        PackageManager pm = LrchaoUtils.getInstance().getContext().getPackageManager();
        PackageInfo info = pm.getPackageArchiveInfo(apkFile.getAbsolutePath(), PackageManager.GET_ACTIVITIES);
        if (info != null) {
            ApplicationInfo appInfo = info.applicationInfo;
            packageName = appInfo.packageName;  //得到安装包名称
        }
        return packageName;
    }


    /**
     * 获得当前进程的名字
     *
     * @return 进程号
     */
    public static String getCurProcessName() {

        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) LrchaoUtils.getInstance().getContext()
                .getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

    /**
     * 遍历所有app信息
     * 填充相关市场数据
     */
    public static List<PackageInfo> getInstalledAppList() {
        PackageManager packageManager = LrchaoUtils.getInstance().getContext().getPackageManager();
        //获取手机内所有应用
        return packageManager.getInstalledPackages(0);
    }

    /**
     * 检查是否已经安装
     */
    public static boolean isInstalled(String packageName) {
        PackageInfo packageInfo;
        try {
            packageInfo = LrchaoUtils.getInstance().getContext().getPackageManager().getPackageInfo(
                    packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
        }
        return packageInfo != null;
    }

    /**
     * 检查是否开启"允许安装位置来源的应用"
     */
    public static boolean isAllowInstallUnknownApp() {
        return Settings.Secure.getInt(LrchaoUtils.getInstance().getContext().getContentResolver(),
                Settings.Secure.INSTALL_NON_MARKET_APPS, 0) == 1;
    }

    /**
     * 判断 悬浮窗口权限是否打开
     *
     * @return true 允许  false禁止
     */
    @SuppressWarnings("WrongConstant")
    public static boolean checkAppOps(Context context) {
        try {
            Object object = context.getSystemService("appops");
            if (object == null) {
                return false;
            }
            Class localClass = object.getClass();
            Class[] arrayOfClass = new Class[3];
            arrayOfClass[0] = Integer.TYPE;
            arrayOfClass[1] = Integer.TYPE;
            arrayOfClass[2] = String.class;
            Method method = localClass.getMethod("checkOp", arrayOfClass);
            if (method == null) {
                return false;
            }
            Object[] arrayOfObject1 = new Object[3];
            arrayOfObject1[0] = Integer.valueOf(24);
            arrayOfObject1[1] = Integer.valueOf(Binder.getCallingUid());
            arrayOfObject1[2] = context.getPackageName();
            int m = ((Integer) method.invoke(object, arrayOfObject1)).intValue();
            return m == AppOpsManager.MODE_ALLOWED;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * 是否为低内存
     *
     * @param percent 小于该百分比 则视为低内存
     */
    public static boolean isLowMemory(int percent) {

        ShellUtils.CommandResult result = ShellUtils.execCommand(ShellUtils.COMMAND_CAT_MEMINFO, false);
        String content = result.successMsg;
        if (!TextUtils.isEmpty(content)) {

            long total = 0L;
            long free = 0L;

            String[] lineArr = content.split("kB");
            if (lineArr != null && lineArr.length > 0) {
                for (String line : lineArr) {

                    if (line.contains(MEM_TOTAL)) {
                        total = getTotalMemory(line);
                    }

                    if (line.contains(MEM_FREE)) {
                        free = getFreeMemory(line);
                    }

                    if (total > 0 && free > 0) {
                        break;
                    }
                }

                if (total > 0 && free > 0) {
                    // 计算百分比
                    float freePercent = (float) free / total * 100;
                    LogUtils.d("memory free percent==" + freePercent);
                    if (freePercent < percent) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static long getFreeMemory(String line) {
        String[] totalArr = line.split(MEM_FREE);
        return Long.parseLong(totalArr[1].trim());
    }

    private static long getTotalMemory(String line) {
        String[] totalArr = line.split(MEM_TOTAL);
        return Long.parseLong(totalArr[1].trim());
    }

}
