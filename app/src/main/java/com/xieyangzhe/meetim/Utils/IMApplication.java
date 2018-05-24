package com.xieyangzhe.meetim.Utils;

import android.app.Application;
import android.content.Context;

/**
 * Created by joseph on 5/23/18.
 */

public class IMApplication extends Application {
    private static Context AppContext = null;
    private static String AppPackegname;

    public void onCreate() {
        super.onCreate();
        AppContext = getApplicationContext();
        AppPackegname = getPackageName();
    }

    public static Context getAppContext() {
        return AppContext;
    }

    public static String getAppPackegname() {
        return AppPackegname;
    }
}
