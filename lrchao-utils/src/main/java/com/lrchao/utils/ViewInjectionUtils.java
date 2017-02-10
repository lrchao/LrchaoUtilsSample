package com.lrchao.utils;

import android.app.Activity;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Description: View注入的帮助类
 * 使用Butter Knife
 * http://jakewharton.github.io/butterknife/
 * http://blog.csdn.net/ytzys/article/details/53243438
 *
 * @author liuranchao
 * @date 16/7/28 上午9:44
 */
public final class ViewInjectionUtils {


    private ViewInjectionUtils() {
    }

    /**
     * 绑定注入
     *
     * @param target Object
     * @param source View
     */
    public static Unbinder bind(@NonNull Object target, @NonNull View source) {
        return ButterKnife.bind(target, source);
    }

    public static void bind(Activity activity) {
        ButterKnife.bind(activity);
    }

    /**
     * 绑定注入
     *
     * @param view View
     */
    public static void bind(View view) {
        ButterKnife.bind(view);
    }

    /**
     * 适用于LayoutInflater.from(getActivity()).inflate(R.layout.xxx, null);
     */
    public static <T extends View> T findById(@NonNull View view, @IdRes int id) {
        return ButterKnife.findById(view, id);
    }

    /**
     * 解绑依赖
     *
     * @param unbinder Unbinder
     */
    public static void unbind(Unbinder unbinder) {
        try {
            if (unbinder != null) {
                unbinder.unbind();
            }

        } catch (Exception e) {
            LogUtils.wtf(e);
        } catch (Error error) {
            LogUtils.wtf(error);
        }

    }


}
