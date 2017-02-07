package com.lrchao.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;

import java.io.File;
import java.util.List;

/**
 * Description: 调用系统Intent的工具类
 *
 * @author lrc19860926@gmail.com
 * @date 2016/10/14 下午2:42
 */

public final class IntentUtils {

    private static final String ACTION_VPN_SETTING_LIST =
            "android.net.vpn.SETTINGS";

    private IntentUtils() {
    }

    /**
     * 跳转到系统设置的VPN页面
     */
    public static void navToVpnSetting() {
        Intent intent = new Intent(ACTION_VPN_SETTING_LIST);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(LrchaoUtils.getInstance().getContext(), intent);
    }

    /**
     * @param packageName  包名
     * @param activityPath activity全路径，包括包名
     */
    public static void navToOtherAppActivity(String packageName, String activityPath) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(packageName, activityPath));
        startActivity(LrchaoUtils.getInstance().getContext(), intent);
    }

    /**
     * 跳转到辅助功能的设置
     */
    public static void navToAccessibilitySettings() {
        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
        startActivity(LrchaoUtils.getInstance().getContext(), intent);
    }

    /**
     * 回到桌面
     */
    public static void navToHome() {
        Intent i = new Intent(Intent.ACTION_MAIN);
        i.addCategory(Intent.CATEGORY_HOME);
        startActivity(LrchaoUtils.getInstance().getContext(), i);
    }

    /**
     * 跳转到系统的设置页面
     */
    public static void navToSetting() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_SETTINGS);
        startActivity(LrchaoUtils.getInstance().getContext(), intent);
    }

    /**
     * 跳转到系统的安全页面
     */
    public static void navToSecuritySetting() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_SECURITY_SETTINGS);
        startActivity(LrchaoUtils.getInstance().getContext(), intent);
    }

    /**
     * 跳转到系统WI-FI设置页面
     */
    public static void navToWifiSetting() {
        Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(LrchaoUtils.getInstance().getContext(), intent);
    }

    /**
     * 调整到设置的应用详情页面
     */
    public static void navToAppDetailSetting(String packageName) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", packageName, null);
        intent.setData(uri);
        startActivity(LrchaoUtils.getInstance().getContext(), intent);
    }

    /**
     * 调用系统的安全程序安装Apk
     *
     * @param context     Context
     * @param apkFilePath 文件路径
     * @return 是否安装成功
     */
    public static boolean installAPK(Context context, String apkFilePath) {
        File apkFile = new File(apkFilePath);
        if (!apkFile.exists()) {
            return false;
        }
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setDataAndType(Uri.fromFile(apkFile),
                "application/vnd.android.package-archive");
        context.startActivity(i);
        return true;
    }

    /**
     * 拨打电话
     *
     * @param context     Context
     * @param phoneNumber 手机号码
     */
    public static void callPhone(Context context, String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri data = Uri.parse("tel:" + phoneNumber);
        intent.setData(data);
        context.startActivity(intent);
    }

    /**
     * 返回跳转到系统相册页的 intent
     */
    public static Intent getAlbumIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*");
        return intent;
    }

    /**
     * 当从相册获取图片的时候，在OnActivityResult 方法中获取图片的路径
     *
     * @param data    数据
     * @param context 上下文
     * @return 字符串
     */
    public static String getPickPhotoFilePath(Intent data, Activity context) {
        Uri uri = data.getData();
        return getPickPhotoFilePath(uri, context);
    }

    /**
     * 当从相册获取图片的时候，在OnActivityResult 方法中获取图片的路径
     *
     * @param uri     url
     * @param context Activity
     * @return string
     */
    public static String getPickPhotoFilePath(Uri uri, Activity context) {
        String imgPath = null;
        if (uri == null || context == null) {
            return "";
        }
        String[] proj = {MediaStore.Images.Media.DATA};
        if ("content".equals(uri.getScheme())) {
            @SuppressWarnings("deprecation") Cursor cursor = context.managedQuery(uri, proj, null,
                    null, null);
            if (cursor != null) {
                int columnIndex = cursor.getColumnIndexOrThrow(proj[0]);
                cursor.moveToFirst();
                imgPath = cursor.getString(columnIndex);
            }
        } else if ("file".equals(uri.getScheme())) {
            imgPath = uri.getPath();
        }

        if (TextUtils.isEmpty(imgPath)) {
            imgPath = null;
        }
        return imgPath;
    }

    /**
     * 返回跳转到系统相机页的 intent
     *
     * @param filePath 拍照后保存的文件地址
     */
    public static Intent getCameraIntent(String filePath) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File outputFile = new File(filePath);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(outputFile));
        intent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, true);
        return intent;
    }

    /**
     * 通过包名启动app
     */
    public static void navToOtherApp(Context context, String packageName) {

        // 通过包名获取此APP详细信息，包括Activities、services、versioncode、name等等
        PackageInfo packageinfo = null;
        try {
            packageinfo = context.getPackageManager().getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageinfo == null) {
            return;
        }

        // 创建一个类别为CATEGORY_LAUNCHER的该包名的Intent
        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resolveIntent.setPackage(packageinfo.packageName);

        // 通过getPackageManager()的queryIntentActivities方法遍历
        List<ResolveInfo> resolveInfoList = context.getPackageManager()
                .queryIntentActivities(resolveIntent, 0);

        ResolveInfo resolveinfo = resolveInfoList.iterator().next();
        if (resolveinfo != null) {
            // packagename = 参数packname
            String pn = resolveinfo.activityInfo.packageName;
            // 这个就是我们要找的该APP的LAUNCHER的Activity[组织形式：packagename.mainActivityname]
            String className = resolveinfo.activityInfo.name;
            // LAUNCHER Intent
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);

            // 设置ComponentName参数1:packagename参数2:MainActivity路径
            ComponentName cn = new ComponentName(pn, className);
            intent.setComponent(cn);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

    /**
     * 统一调用startActivity
     *
     * @param intent Intent
     */
    private static void startActivity(Context context, Intent intent) {

        try {
            if (context != null) {
                if (context == LrchaoUtils.getInstance().getContext()) {
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }
                context.startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
