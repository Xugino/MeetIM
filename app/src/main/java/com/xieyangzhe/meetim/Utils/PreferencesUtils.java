package com.xieyangzhe.meetim.Utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by joseph on 5/22/18.
 */

public class PreferencesUtils {

    private static final String FILE_NAME = "APP_DATA";
    private static SharedPreferences sharedPreferences;
    private static PreferencesUtils preferencesUtils;

    private PreferencesUtils() {
        sharedPreferences = IMApplication.getAppContext().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized PreferencesUtils getInstance() {
        if (preferencesUtils == null) {
            preferencesUtils = new PreferencesUtils();
        }
        return preferencesUtils;
    }

    public void saveData(String key, Object data) {
        String type = data.getClass().getSimpleName();

        SharedPreferences.Editor editor = sharedPreferences.edit();

        if ("Integer".equals(type)) {
            editor.putInt(key, (Integer) data);
        } else if ("Boolean".equals(type)) {
            editor.putBoolean(key, (Boolean) data);
        } else if ("String".equals(type)) {
            editor.putString(key, (String) data);
        } else if ("Float".equals(type)) {
            editor.putFloat(key, (Float) data);
        } else if ("Long".equals(type)) {
            editor.putLong(key, (Long) data);
        }

        editor.commit();
    }

    public Object getData(String key, Object defValue) {

        String type = defValue.getClass().getSimpleName();
        if ("Integer".equals(type)) {
            return sharedPreferences.getInt(key, (Integer) defValue);
        } else if ("Boolean".equals(type)) {
            return sharedPreferences.getBoolean(key, (Boolean) defValue);
        } else if ("String".equals(type)) {
            return sharedPreferences.getString(key, (String) defValue);
        } else if ("Float".equals(type)) {
            return sharedPreferences.getFloat(key, (Float) defValue);
        } else if ("Long".equals(type)) {
            return sharedPreferences.getLong(key, (Long) defValue);
        }

        return null;
    }
}
