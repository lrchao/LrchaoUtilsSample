package com.lrchao.utils;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Context;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;

import java.util.List;

/**
 * Description: 辅助功能的工具
 *
 * @author lrc19860926@gmail.com
 * @date 2017/2/7 下午4:42
 */

public final class AccessibleUtils {

    private AccessibleUtils() {
    }

    /**
     * 检查是否开启辅助功能
     *
     * @param accessibilityServiceClass 继承 AccessibilityService的类
     */
    public static boolean isAccessibleEnabled(Class accessibilityServiceClass) {

        AccessibilityManager manager = (AccessibilityManager) LrchaoUtils.getInstance().getContext().
                getSystemService(Context.ACCESSIBILITY_SERVICE);

        List<AccessibilityServiceInfo> runningServices = manager.getEnabledAccessibilityServiceList(AccessibilityEvent.TYPES_ALL_MASK);
        for (AccessibilityServiceInfo info : runningServices) {
            if (info.getId().equals(LrchaoUtils.getInstance().getContext().getPackageName()
                    + "/.service." + accessibilityServiceClass.getSimpleName())) {
                return true;
            }
        }
        return false;
    }

}
