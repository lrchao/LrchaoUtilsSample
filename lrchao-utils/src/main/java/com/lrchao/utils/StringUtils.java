package com.lrchao.utils;

import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;

/**
 * Description: 操作字符工具类
 *
 * @author lrc19860926@gmail.com
 * @date 2016/10/14 下午2:52
 */

public final class StringUtils {

    private StringUtils() {
    }

    /**
     * 设置TextView显示html
     *
     * @param htmlText HTML格式的文本
     */
    @SuppressWarnings("deprecation")
    @Nullable
    public static Spanned getHtmlText(String htmlText) {
        if (TextUtils.isEmpty(htmlText)) {
            return null;
        }

        if (OSUtils.hasN()) {
            return Html.fromHtml(htmlText, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(htmlText);
        }
    }

    /**
     * 验证手机格式
     * <p/>
     * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
     * 联通：130、131、132、152、155、156、185、186
     * 电信：133、153、180、189、（1349卫通）
     * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
     */
    public static boolean isMobileNumber(String mobiles) {
        //"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        String telRegex = "[1][358]\\d{9}";
        if (!TextUtils.isEmpty(mobiles)) {
            return mobiles.matches(telRegex);
        } else {
            return false;
        }
    }

    /**
     * 获取html文本变色样式
     * #999999
     *
     * @param content 文本内容
     */
    public static String getHtmlStrWithColor(String colorStr, String content) {
        return "<font color='" + colorStr + "'>" + content + "</font>";
    }

    /**
     * 获取TextView不同颜色的文本
     *
     * @param sourceStr   整个文本
     * @param keyword     要匹配的string
     * @param behindColor 背景色
     * @param frontColor  前景色
     */
    @Nullable
    public static SpannableStringBuilder getMatchKeywordColorStr(String sourceStr,
                                                                 String keyword,
                                                                 @ColorRes int behindColor,
                                                                 @ColorRes int frontColor) {
        if (TextUtils.isEmpty(sourceStr) || TextUtils.isEmpty(keyword)) {
            return null;
        }

        // 背景
        int behindStart = sourceStr.indexOf(keyword);
        int behindEnd = behindStart + keyword.length();
        // 前景
        int frontStart = sourceStr.indexOf(keyword);
        int frontEnd = frontStart + keyword.length();
        SpannableStringBuilder style = new SpannableStringBuilder(sourceStr);

        if (behindStart >= 0 && behindColor > 0) {
            style.setSpan(new BackgroundColorSpan(ResourceUtils.getColor(behindColor)),
                    behindStart, behindEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        if (frontStart >= 0 && frontColor > 0) {
            style.setSpan(new ForegroundColorSpan(ResourceUtils.getColor(frontColor)),
                    frontStart, frontEnd, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        }
        return style;
    }

    /**
     * 转换成小写
     */
    public static char toLower(char ch) {
        if (ch <= 'Z' && ch >= 'A') {
            return (char) (ch - 'A' + 'a');
        }
        return ch;
    }

    /**
     * 转换成大写
     */
    public static char toUpper(char ch) {
        if (ch <= 'z' && ch >= 'a') {
            return (char) (ch - 32);
        }
        return ch;
    }

    /**
     * 去除对应的参数，然后返回去除后的URL
     *
     * @param url
     * @param params
     * @return
     */
    public static String removeParams(String url, String[] params) {
        String reg = null;
        String mUrl = url;
        for (int i = 0; i < params.length; i++) {
            reg = "(?<=[\\?&])" + params[i] + "=[^&]*&?";
            mUrl = mUrl.replaceAll(reg, "");
        }
        mUrl = mUrl.replaceAll("&+$", "");
        return mUrl;
    }

    public static boolean isURL(String url) {
        if (TextUtils.isEmpty(url)) {
            return false;
        }
        return url.startsWith("http://");
    }
}
