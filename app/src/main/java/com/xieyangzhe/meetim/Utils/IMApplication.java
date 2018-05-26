package com.xieyangzhe.meetim.Utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.text.TextUtils;

import java.util.List;

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

    public static boolean isForeground(Activity activity) {
        return isForeground(activity, activity.getClass().getName());
    }

    public static boolean isForeground(Context context, String className) {
        if (context == null || TextUtils.isEmpty(className))
            return false;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(1);
        if (list != null && list.size() > 0) {
            ComponentName cpn = list.get(0).topActivity;
            if (className.equals(cpn.getClassName()))
                return true;
        }
        return false;
    }
}
