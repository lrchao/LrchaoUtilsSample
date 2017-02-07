package com.lrchao.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;

import java.io.File;

/**
 * Description: 调用系统Intent的工具类
 *
 * @author lrc19860926@gmail.com
 * @date 2016/10/14 下午2:42
 */

public final class IntentUtils {

    private IntentUtils() {
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
}
