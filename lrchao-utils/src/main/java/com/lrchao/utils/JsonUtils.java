package com.lrchao.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Description: JSON解析的工具类
 * 所有统一的JSON
 * 目前采用Google Gson
 * 更多用法 #Link{https://github.com/google/gson}
 *
 * @author lrc19860926@gmail.com
 * @date 2016/10/14 下午3:27
 */

public final class JsonUtils {

    private static Gson gson;

    static {
        gson = new GsonBuilder()
                //.registerTypeAdapter(Id.class, new IdTypeAdapter())
                //.enableComplexMapKeySerialization()
                //.serializeNulls()
                //.setDateFormat(DateFormat.LONG)
                //.setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
                //.setPrettyPrinting()
                //.setVersion(1.0)
                //.excludeFieldsWithoutExposeAnnotation()
                .create();
    }


    private JsonUtils() {
    }

    /**
     * 根据class将json字符串转换成对应的对象
     * 要转换的对象可以用public字段，也可以用getter
     * 抽象类 无法 转换
     *
     * @param jsonStr json str
     * @param clazz   解析对象的class
     * @param <T>     具体类型
     * @return 解析的对象， 如果发生数据格式不同等异常，则导致return null
     */
    public static <T> T read(String jsonStr, Class<T> clazz) {

        T t = null;
        try {
            t = gson.fromJson(jsonStr, clazz);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return t;
    }

    /**
     * 将json string转换成 List<T>
     *
     * @param jsonStr json string
     * @return 如果发生数据格式不同等异常，则导致return null
     */
    public static <T> List<T> readList(String jsonStr) {

        List<T> t = null;
        try {
            Type listType = new TypeToken<List<T>>() {
            }.getType();
            t = gson.fromJson(jsonStr, listType);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return t;
    }

    /**
     * 将json string转换成 List<T>
     *
     * @param jsonStr json string
     * @param clazz   model array' class
     * @return 如果发生数据格式不同等异常，则导致return null
     */
    public static <T> List<T> readList(String jsonStr, Class<T[]> clazz) {
        List<T> t = null;
        try {
            T[] arr = gson.fromJson(jsonStr, clazz);
            t = Arrays.asList(arr);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return t;
    }

    /**
     * 将对象转换成json string
     *
     * @param obj 要转换成json string 的 对象，
     */
    public static String write(Object obj) {
        String jsonStr = "";
        try {
            jsonStr = gson.toJson(obj);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return jsonStr;
    }

    public static <T> List<T> readListFromClass(String jsonStr, Class<T> clazz) {
        List<T> lst = new ArrayList<>();
        JsonArray array = new JsonParser().parse(jsonStr).getAsJsonArray();
        for (final JsonElement elem : array) {
            lst.add(new Gson().fromJson(elem, clazz));
        }
        return lst;
    }

    //====================================
    // 普通的JSON 解析
    //====================================

    /**
     * 解析int
     *
     * @param jsonObject 父JSON Oject
     * @param key        节点key
     * @return String value
     */
    public static int getJSONInt(JSONObject jsonObject, String key) {
        int value = 0;
        try {
            value = jsonObject.getInt(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * 解析String
     *
     * @param jsonObject 父JSON Oject
     * @param key        节点key
     * @return String value
     */
    public static String getJSONString(JSONObject jsonObject, String key) {
        String value = "";
        try {
            value = jsonObject.getString(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return value;
    }


    /**
     * 获取JSONObject
     *
     * @param jsonStr json 字符串
     * @return JSONObject 可能为null
     */
    public static JSONObject getJSONObject(String jsonStr) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(jsonStr);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    /**
     * 解析JSON中的一个Object节点
     *
     * @param jsonObject 父JSON Object
     * @param key        节点名称
     * @return 节点对象
     */
    public static Object getObject(JSONObject jsonObject, String key) {
        Object obj = null;
        try {
            obj = jsonObject.get(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }
}
