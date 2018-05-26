package com.xieyangzhe.meetim.Utils;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

/**
 * Created by joseph on 5/26/18.
 */

public class ActivityLifecycleListener implements Application.ActivityLifecycleCallbacks {
    private int refCount = 0;
    private static boolean isBackGround;

    public static boolean isBackGround() {
        return isBackGround;
    }

    public void setBackGround(boolean backGround) {
        isBackGround = backGround;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        refCount++;
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {
        refCount--;
        if(refCount == 0){
            setBackGround(true);
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }
}
