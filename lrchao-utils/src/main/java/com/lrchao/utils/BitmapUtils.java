package com.lrchao.utils;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.widget.ImageView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * Description: Bitmap工具类
 *
 * @author lrc19860926@gmail.com
 * @date 2016/10/14 上午11:19
 */

public final class BitmapUtils {

    private static final String TAG = "BitmapUtils";

    private BitmapUtils() {
    }

    /**
     * bitmap 是否可用
     */
    public static boolean isAvailable(Bitmap b) {
        return (b != null && !b.isRecycled());
    }

    /**
     * 从资源文件中获取bitmap
     *
     * @param drawableId 资源图片文件
     * @return Bitmap
     */
    public static Bitmap decode(@DrawableRes int drawableId) {
        return BitmapFactory.decodeResource(LrchaoUtils.getInstance().getContext().getResources(),
                drawableId);
    }

    /**
     * 获得图片文件的bitmap, 根据设备
     *
     * @param filePath  图片文件路径
     * @param reqWidth  期望的宽度
     * @param reqHeight 期望的高度
     */
    public static Bitmap decodeBitmapBySampleSizeFromFile(String filePath, int reqWidth, int reqHeight) {
        Bitmap bitmap = null;
        try {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(filePath, options);
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
            options.inJustDecodeBounds = false;
            bitmap = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError error) {
            error.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 计算Sample size
     *
     * @param options   BitmapFactory.Options
     * @param reqWidth  期望的宽度
     * @param reqHeight 期望的高度
     */
    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) reqHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }
            final float totalPixels = width * height;
            final float totalReqPixelsCap = reqWidth * reqHeight * 2;
            while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
                inSampleSize++;
            }
        }
        return inSampleSize;
    }


    /**
     * 压缩bitmap到预期的大小
     *
     * @param sourceBmp 要压缩的bitmap
     * @param size      最大的大小 byte
     */
    public static Bitmap compressBitmap(Bitmap sourceBmp, long size) {
        int offset = 5;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int options = 80;
        sourceBmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
        while (baos.toByteArray().length > size) {
            baos.reset();
            options -= offset;
            if (options > 0) {
                sourceBmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
            } else {
                options += offset;
                sourceBmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
                break;
            }
        }
        //把压缩后的数据baos存放到ByteArrayInputStream中
        ByteArrayInputStream is = new ByteArrayInputStream(baos.toByteArray());
        //把ByteArrayInputStream数据生成图片
        Bitmap resultBitmap = BitmapFactory.decodeStream(is, null, null);
        recycle(sourceBmp);
        return resultBitmap;
    }

    /**
     * 释放图片
     *
     * @param bitmap Bitmap
     */
    public static void recycle(Bitmap bitmap) {
        if (isAvailable(bitmap)) {
            bitmap.recycle();
        }
    }

    /**
     * 模糊图片
     *
     * @param bitmap    Bitmap
     * @param imageView ImageView
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static void doBlur(Bitmap bitmap, ImageView imageView) {
        float scaleFactor = 8;
        float radius = 2;

        try {
            Bitmap overlay = Bitmap.createBitmap((int) (imageView.getMeasuredWidth() / scaleFactor),
                    (int) (imageView.getMeasuredHeight() / scaleFactor), Bitmap.Config.ARGB_8888);

            Canvas canvas = new Canvas(overlay);
            canvas.translate(-imageView.getLeft() / scaleFactor, -imageView.getTop() / scaleFactor);
            canvas.scale(1 / scaleFactor, 1 / scaleFactor);
            Paint paint = new Paint();
            paint.setFlags(Paint.FILTER_BITMAP_FLAG);
            RectF rectF = new RectF(0, 0, imageView.getMeasuredWidth(), imageView.getMeasuredHeight());
            canvas.drawBitmap(bitmap, null, rectF, paint);
            overlay = doBlur(overlay, (int) radius, true);
            imageView.setImageDrawable(new BitmapDrawable(overlay));
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
    }

    /**
     * 模糊图片
     */
    @SuppressWarnings({"checkstyle:methodlength", "checkstyle:arraytypestyle",
            "checkstyle:multiplevariabledeclarations", "checkstyle:innerassignment"})
    public static Bitmap doBlur(Bitmap sentBitmap, int radius, boolean canReuseInBitmap) {

        // Stack Blur v1.0 from
        // http://www.quasimondo.com/StackBlurForCanvas/StackBlurDemo.html
        //
        // Java Author: Mario Klingemann <mario at quasimondo.com>
        // http://incubator.quasimondo.com
        // created Feburary 29, 2004
        // Android port : Yahel Bouaziz <yahel at kayenko.com>
        // http://www.kayenko.com
        // ported april 5th, 2012

        // This is a compromise between Gaussian Blur and Box blur
        // It creates much better looking blurs than Box Blur, but is
        // 7x faster than my Gaussian Blur implementation.
        //
        // I called it Stack Blur because this describes best how this
        // filter works internally: it creates a kind of moving stack
        // of colors whilst scanning through the image. Thereby it
        // just has to add one new block of color to the right side
        // of the stack and remove the leftmost color. The remaining
        // colors on the topmost layer of the stack are either added on
        // or reduced by one, depending on if they are on the right or
        // on the left side of the stack.
        //
        // If you are using this algorithm in your code please add
        // the following line:
        //
        // Stack Blur Algorithm by Mario Klingemann <mario@quasimondo.com>

        Bitmap bitmap;
        if (canReuseInBitmap) {
            bitmap = sentBitmap;
        } else {
            bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);
        }

        if (radius < 1) {
            return (null);
        }

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int[] pix = new int[w * h];
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);

        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int dv[] = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {

                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16) | (dv[gsum] << 8) | dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += w;
            }
        }

        bitmap.setPixels(pix, 0, w, 0, 0, w, h);

        return (bitmap);
    }
}
