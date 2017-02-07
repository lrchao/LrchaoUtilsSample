package com.lrchao.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;


/**
 * Description: 权限的工具类
 * <p>
 * Dangerous permissions:
 * READ_CALENDAR
 * WRITE_CALENDAR
 * CAMERA
 * READ_CONTACTS
 * WRITE_CONTACTS
 * GET_ACCOUNTS
 * ACCESS_FINE_LOCATION
 * ACCESS_COARSE_LOCATION
 * RECORD_AUDIO
 * READ_PHONE_STATE
 * CALL_PHONE
 * READ_CALL_LOG
 * WRITE_CALL_LOG
 * ADD_VOICEMAIL
 * USE_SIP
 * PROCESS_OUTGOING_CALLS
 * BODY_SENSORS
 * SEND_SMS
 * RECEIVE_SMS
 * READ_SMS
 * RECEIVE_WAP_PUSH
 * RECEIVE_MMS
 * READ_EXTERNAL_STORAGE
 * WRITE_EXTERNAL_STORAGE
 *
 * @author lrc19860926@gmail.com
 * @date 2016/10/17 下午5:31
 */

public final class PermissionUtils {

    private PermissionUtils() {
    }

    /**
     * 检查权限是否开启
     *
     * @param context    Context
     * @param permission Manifest.permission.XXX
     * @return true : 已经授权  false: 没有授权
     */
    public static boolean checkSelfPermission(Context context, String permission) {
        if (context != null && !TextUtils.isEmpty(permission)) {
            return ActivityCompat.checkSelfPermission(context, permission) ==
                    PackageManager.PERMISSION_GRANTED;
        }
        return false;
    }

    /**
     * 检查是否需要申请权限，如果用户点击“不再询问”后，返回为false，需要app 提示用户
     *
     * @param activity   Activity
     * @param permission Manifest.permission.XXX
     * @return true: 不能直接申请权限，false: 可以直接申请权限，但申请的结果，有可能是已经被用户拒绝了的
     */
    public static boolean checkShouldShowRequest(Activity activity, String permission) {
        if (activity != null && !TextUtils.isEmpty(permission)) {
            return ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    permission);
        }
        return false;
    }

    /**
     * 请求权限
     *
     * @param activity              Activity
     * @param permission            Manifest.permission.XXX
     * @param permissionRequestCode onRequestPermissionsResult request code
     */
    public static void requestPermission(Activity activity, String permission, int permissionRequestCode) {
        if (activity != null && permissionRequestCode > 0) {
            ActivityCompat.requestPermissions(
                    activity,
                    new String[]{permission},
                    permissionRequestCode);
        }
    }

    /**
     * 验证是否所申请的几个权限都有开通
     * Check that all given permissions have been granted by verifying that each entry in the
     * given array is of the value {@link PackageManager#PERMISSION_GRANTED}.
     *
     * @see Activity#onRequestPermissionsResult(int, String[], int[])
     */
    public static boolean verifyPermissions(int[] grantResults) {
        // At least one result must be checked.
        if (grantResults.length < 1) {
            return false;
        }

        // Verify that each required permission has been granted, otherwise return false.
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

}
