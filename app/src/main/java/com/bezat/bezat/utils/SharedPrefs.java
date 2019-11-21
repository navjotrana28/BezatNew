package com.bezat.bezat.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefs {
    public static SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor editor;

    public static void setKey(Context context, String key, String value) {
        sharedPreferences = context.getSharedPreferences("prefsData", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getKey(Context context, String key) {
        sharedPreferences = context.getSharedPreferences("prefsData", Context.MODE_PRIVATE);
        String value = sharedPreferences.getString(key, "");
        return value;
    }

    public static void deleteSharedPrefs(Context context) {
        sharedPreferences = context.getSharedPreferences("prefsData", Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().commit();
    }
}
