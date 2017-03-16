package com.lrchao.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Target;

import java.io.File;

/**
 * Description: 显示图片的工具类
 * http://square.github.io/picasso/
 * https://futurestud.io/tutorials/picasso-getting-started-simple-loading
 * <p>
 * fit(): 将resize 图片的大小，来适配image view的大小， 会降低内存，延迟显示时间
 * centerCrop(): 按照填满image view的大小进行填充， 图片会被裁剪。配合resize使用，不可单独使用
 * centerInside(): 缩小图片全部显示在image view中，会无法填满image view。配合resize使用，不可单独使用
 * scaleDown(): 当使用resize()方法Picasso将调整图片大小。
 * 因为使一个小图像更大并没有提高图像的质量会浪费计算时间,调用scaleDown(true)只当原始图像尺寸超过目标的大小才会调用resize()方法
 * <p>
 *
 * @author lrc19860926@gmail.com
 * @date 2016/10/17 上午9:40
 */

public final class ImageUtils {

    private ImageUtils() {
    }

    /**
     * 显示URL图片
     */
    public static void display(String url, ImageView imageView,
                               @DrawableRes int placeHolder,
                               final Callback callback,
                               boolean isFit,
                               int targetW,
                               int targetH,
                               boolean isCenterCrop,
                               boolean isCenterInside) {
        try {
            RequestCreator creator = Picasso.with(LrchaoUtils.getInstance().getContext())
                    .load(url);
            create(creator, imageView, placeHolder, callback, isFit, targetW, targetH, isCenterCrop, isCenterInside);

        } catch (Throwable e) {
            e.printStackTrace();
        }

    }

    /**
     * 显示URI图片
     */
    public static void display(Uri uri, ImageView imageView,
                               @DrawableRes int placeHolder,
                               final Callback callback,
                               boolean isFit,
                               int targetW,
                               int targetH,
                               boolean isCenterCrop,
                               boolean isCenterInside) {
        try {
            RequestCreator creator = Picasso.with(LrchaoUtils.getInstance().getContext())
                    .load(uri);
            create(creator, imageView, placeHolder, callback, isFit, targetW, targetH, isCenterCrop, isCenterInside);

        } catch (Throwable e) {
            e.printStackTrace();
        }

    }

    /**
     * 显示资源图片
     */
    public static void display(@DrawableRes int resId,
                               ImageView imageView,
                               @DrawableRes int placeHolder,
                               final Callback callback,
                               boolean isFit,
                               int targetW,
                               int targetH,
                               boolean isCenterCrop,
                               boolean isCenterInside) {
        try {
            RequestCreator creator = Picasso.with(LrchaoUtils.getInstance().getContext())
                    .load(resId);
            create(creator, imageView, placeHolder, callback, isFit, targetW, targetH, isCenterCrop, isCenterInside);

        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示File图片
     */
    public static void display(File file, ImageView imageView,
                               @DrawableRes int placeHolder,
                               final Callback callback,
                               boolean isFit,
                               int targetW,
                               int targetH,
                               boolean isCenterCrop,
                               boolean isCenterInside) {

        try {
            RequestCreator creator = Picasso.with(LrchaoUtils.getInstance().getContext())
                    .load(file);
            create(creator, imageView, placeHolder, callback, isFit, targetW, targetH, isCenterCrop, isCenterInside);

        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    /**
     * 取消显示
     */
    public static void cancel(Context context, ImageView imageView) {
        Picasso.with(context).cancelRequest(imageView);
    }

    /**
     * 加载bitmap
     *
     * @param url      network url
     * @param callback BitmapCallback
     */
    public static void loadBitmap(String url, final BitmapCallback callback) {
        Picasso.with(LrchaoUtils.getInstance().getContext()).load(url).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                if (callback != null) {
                    callback.onBitmapLoaded(bitmap);
                }
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                if (callback != null) {
                    callback.onBitmapFailed();
                }
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });
    }


    //============================================
    // private
    //============================================

    /**
     * @param requestCreator RequestCreator
     * @param imageView      ImageView
     * @param placeHolder    placeHolder()
     * @param callback       Callback
     * @param isFit          fit()
     * @param targetW        resize()
     * @param targetH        resize()
     * @param isCenterCrop   centerCrop()
     * @param isCenterInside centerInside()()
     * @throws Throwable
     */
    private static void create(RequestCreator requestCreator,
                               ImageView imageView,
                               @DrawableRes int placeHolder,
                               final Callback callback,
                               boolean isFit,
                               int targetW,
                               int targetH,
                               boolean isCenterCrop,
                               boolean isCenterInside
    ) throws Throwable {

        if (isFit) {
            requestCreator.fit();
        }

        if (targetW > 0 && targetH > 0) {
            requestCreator.resize(targetW, targetH);
        }

        if (isCenterCrop) {
            requestCreator.centerCrop();
        }

        if (isCenterInside) {
            requestCreator.centerInside();
        }

        if (placeHolder > 0) {
            requestCreator.placeholder(placeHolder);
        }

        requestCreator
                .into(imageView, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        if (callback != null) {
                            callback.onSuccess();
                        }
                    }

                    @Override
                    public void onError() {
                        if (callback != null) {
                            callback.onError();
                        }
                    }
                });
    }


    /**
     * 加载图片 结果接口
     */
    public interface Callback {
        void onSuccess();

        void onError();
    }

    public interface BitmapCallback {
        void onBitmapLoaded(Bitmap bitmap);

        void onBitmapFailed();
    }


}
