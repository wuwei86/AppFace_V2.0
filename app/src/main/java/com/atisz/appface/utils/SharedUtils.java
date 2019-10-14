package com.atisz.appface.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by LaoWu on 2018/4/28.
 */

public class SharedUtils {
    public static void commit(Context context, String shareName, String key, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(shareName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static void clear(Context context, String shareName) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(shareName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear().commit();
    }


    public static String getStringValue(Context context, String shareName, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(shareName, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, null);
    }
}
