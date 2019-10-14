package com.atisz.appface.utils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.lang.reflect.Type;

/**
 * Created by LaoWu on 2018/6/19 0019.
 * json解析的工具类
 */

public class JsonHelper {
    private static final Gson gson = new Gson();

    /**
     * 对象转变成Json字符串
     * @param object
     * @return
     */
    public static String toJsonString(Object object) {
        String jsonString = "";
        try {
            jsonString = gson.toJson(object);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonString;
    }

    /**
     * 字符串根据类型转变成类
     * @param json
     * @param type
     * @param <T>
     * @return
     */
    public static <T>T fromJSONObject(String json, Type type) {
        T t = null;
        try {
            t = gson.fromJson(json, type);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return t;
    }

    /**
     * 字符串根据类转变成类
     * @param json
     * @param tClass
     * @param <T>
     * @return
     */
    public static <T>T fromJSONObject(String json, Class<T> tClass) {
        T t = null;
        try {
            t = gson.fromJson(json, tClass);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return t;
    }
}
