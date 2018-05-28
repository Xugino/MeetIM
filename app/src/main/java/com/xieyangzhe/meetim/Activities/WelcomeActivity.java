package com.xieyangzhe.meetim.Activities;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationBuilderWithBuilderAccessor;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.xieyangzhe.meetim.R;
import com.xieyangzhe.meetim.Services.XMPPService;
import com.xieyangzhe.meetim.Utils.IMApplication;
import com.xieyangzhe.meetim.Utils.NotificationTool;
import com.xieyangzhe.meetim.Utils.PreferencesUtils;
import com.xieyangzhe.meetim.Utils.XMPPTool;

public class WelcomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.activity_welcome);

        if (((String) PreferencesUtils.getInstance().getData("username", "")).length() >= 4
                && ((String) PreferencesUtils.getInstance().getData("username", "")).length() >= 4) {
            startService(new Intent(this, XMPPService.class));
            new Thread(() -> {
                try {
                    Thread.sleep(1000);
                    if (!XMPPTool.getLoginStatus()) {
                        stopService(new Intent(this, XMPPService.class));
                        startActivity(new Intent(this, LoginActivity.class));
                    } else {
                        startActivity(new Intent(this, MainActivity.class));
                    }
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();


        }
    }
}
