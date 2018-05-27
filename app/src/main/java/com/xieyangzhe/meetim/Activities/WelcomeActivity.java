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
import android.widget.Button;

import com.xieyangzhe.meetim.R;
import com.xieyangzhe.meetim.Services.XMPPService;
import com.xieyangzhe.meetim.Utils.IMApplication;
import com.xieyangzhe.meetim.Utils.NotificationTool;
import com.xieyangzhe.meetim.Utils.PreferencesUtils;
import com.xieyangzhe.meetim.Utils.XMPPTool;

public class WelcomeActivity extends AppCompatActivity {
    Button buttonTest;
    Button buttonSend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        /*
        if (((String) PreferencesUtils.getInstance().getData("username", "null")).length() >= 4
                && ((String) PreferencesUtils.getInstance().getData("username", "null")).length() >= 4) {
            startService(new Intent(WelcomeActivity.this, XMPPService.class));
            if (!XMPPTool.getLoginStatus()) {
                stopService(new Intent(WelcomeActivity.this, XMPPService.class));
                startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
            } else {
                startActivity(new Intent(this, MainActivity.class));
            }
        }
        */

        buttonTest = findViewById(R.id.button_test);
        buttonSend = findViewById(R.id.button_send);
        buttonTest.setOnClickListener(view -> {
            NotificationTool notificationUtils = new NotificationTool(IMApplication.getAppContext());
            notificationUtils.sendNotification("测试标题", "测试内容", null);
        });

        buttonSend.setOnClickListener(view -> {
                startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
        });
    }
}
