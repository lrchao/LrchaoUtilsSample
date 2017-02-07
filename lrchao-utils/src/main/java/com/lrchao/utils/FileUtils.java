package com.lrchao.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.ExifInterface;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;

/**
 * Description: 文件操作的工具类
 *
 * @author lrc19860926@gmail.com
 * @date 2016/10/14 下午3:13
 */

public final class FileUtils {

    private static final String TAG = "FileUtils";

    //=======================================================
    // Public
    //=======================================================

    /**
     * 文件是否存在
     */
    public static boolean isExist(File file) {
        return file != null && file.exists();
    }

    /**
     * 删除文件
     *
     * @param fileName 文件的绝对路径
     * @return 是否删除成功
     * @author KevinLiu
     */

    public static boolean delete(String fileName) {
        return new File(fileName).delete();
    }

    /**
     * 删除一个文件，或者一个目录下的所有文件
     *
     * @param file 文件或者目录
     */
    public static boolean deleteDir(File file) {
        try {
            if (file == null || !file.exists()) {
                return true;
            }
            if (file.exists() && file.isDirectory()) {
                File[] files = file.listFiles();
                if (files.length > 0) {
                    for (File child : files) {
                        deleteDir(child);
                    }
                }
                if (file.listFiles().length == 0) {
                    return file.delete();
                }
            } else if (file.isFile()) {
                return file.delete();
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * /data/data/com.jia.zxpt.user/cache/fileName
     *
     * @param context  Context
     * @param fileName 文件名称
     * @author KevinLiu
     */
    public static File getCacheFile(Context context, String fileName) {
        File cacheDir = context.getCacheDir();
        return new File(cacheDir.getAbsolutePath() + File.separator + fileName);
    }

    /**
     * 读取文本文件的内容
     *
     * @param inputStream InputStream
     * @return string 文本
     */
    @Nullable
    public static String readTextFile(InputStream inputStream) {
        if (inputStream == null) {
            return "";
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        int len;
        try {
            while ((len = inputStream.read(buf)) != -1) {
                outputStream.write(buf, 0, len);
            }
            outputStream.close();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String s = "";
        try {
            s = outputStream.toString("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return s;
    }

    /**
     * 判断SDCard是否可用
     */
    public static boolean isSDCardAvailable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * @param path    文件路径
     * @param content 写入的文本内容
     */
    public static void writeDataToSdCard(String path, String content) {
        // 要写入的文本文件
        File file = new File(path);

        File dirFile = new File(file.getParent());
        if (!new File(file.getParent()).exists()) {
            //创建目录
            dirFile.mkdirs();
        }

        RandomAccessFile raf = null;
        // 如果文件不存在，则创建该文件
        if (!file.exists()) {
            try {
                file.createNewFile();
                raf = new RandomAccessFile(file, "rw");
                raf.write(content.getBytes("UTF-8"));
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (raf != null) {
                    try {
                        // 关闭输出流，施放资源
                        raf.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 设置文件权限
     *
     * @param filePath 文件路径
     * @return true 设置成功 false 设置失败
     */
    public static boolean setFileAuthority(String filePath) {
        try {
            // 必须给权限 才能安装
            //全部权限
            String command = "chmod 777 " + filePath;
            Runtime runtime = Runtime.getRuntime();
            runtime.exec(command);
            return true;
        } catch (IOException e) {
            Log.e(TAG, "chmod fail!!!!:" + e.getMessage());
            return false;
        }
    }

    /**
     * 读取assets中的文件string
     *
     * @param fileName 文件名
     */
    public static String readAssetsFile(String fileName) {
        String result = "";
        try {
            //得到资源中的asset数据流
            InputStream in = LrchaoUtils.getInstance().getContext().
                    getResources().getAssets().open(fileName);

            int length = in.available();
            byte[] buffer = new byte[length];

            in.read(buffer);
            in.close();
            result = new String(buffer, "UTF-8");

        } catch (Exception e) {

            e.printStackTrace();

        }
        return result;
    }

    /**
     * 获取图片文件的方向
     *
     * @param imagePath 图片文件的绝对路径
     * @return 方向, 正常竖着照的，是0
     */
    public static int getImageFileOrientation(String imagePath) {
        int rotate = 0;
        try {
            File imageFile = new File(imagePath);

            ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
                default:
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return rotate;
    }

    /**
     * 保存图片到指定目录
     *
     * @param bitmap Bitmap
     * @param file   File
     * @param format Bitmap.CompressFormat.JPEG
     */
    public static boolean writeBitmap(Bitmap bitmap, File file, Bitmap.CompressFormat format) {
        if (!BitmapUtils.isAvailable(bitmap)) {
            return false;
        }

        if (file == null) {
            return false;
        }

        if (file.exists()) {
            FileUtils.deleteDir(file);
        }
        FileOutputStream os = null;
        try {
            os = new FileOutputStream(file);
            Bitmap.CompressFormat mFormat = format == null ? Bitmap.CompressFormat.JPEG : format;
            bitmap.compress(mFormat, 100, os);
            os.flush();
        } catch (Exception e) {
            Log.e(TAG, "Error occured on save image", e);
            return false;
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }


    /**
     * 创建子目录
     *
     * @param file File
     */
    public static void mkDirs(File file) {
        if (!file.exists()) {
            file.mkdirs();
        }
    }


}
