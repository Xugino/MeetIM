package com.xieyangzhe.meetim.Services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import com.xieyangzhe.meetim.R;
import com.xieyangzhe.meetim.Utils.IMApplication;
import com.xieyangzhe.meetim.Utils.PreferencesUtils;
import com.xieyangzhe.meetim.Utils.XMPPTool;

public class XMPPService extends Service {

    private XMPPTool xmppTool;
    private boolean threadStatus;
    private Thread xmppThread;
    private Handler xmppHandler;

    public static final String LOGIN_RECEIVER = "com.xieyangzhe.meetim.RECEIVER";
    public static final String LOGIN_STATUS = "com.xieyangzhe.meetim.LOGIN_STATUS";

    private Intent intent = new Intent(LOGIN_RECEIVER);

    public XMPPService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        start();
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stop();
    }

    public void start() {
        if (!threadStatus) {
            threadStatus = true;
            if (xmppThread == null || !xmppThread.isAlive()) {
                xmppThread = new Thread(() -> {
                    Looper.prepare();
                    xmppHandler = new Handler();
                    initXMPPTool();

                    intent.putExtra(LOGIN_STATUS, XMPPTool.getLoginStatus());
                    sendBroadcast(intent);
                    Looper.loop();
                });
                xmppThread.start();
            }
        }
    }

    public void stop() {
        threadStatus = false;
        xmppHandler.post(() -> {
            xmppTool.cutConnection();
        });
    }

    private void initXMPPTool() {
        try {
            xmppTool = XMPPTool.getXmppTool();
            String username = (String) PreferencesUtils.getInstance().getData("username", "null");
            String password = (String) PreferencesUtils.getInstance().getData("password", "null");
            xmppTool.doLogin(username, password);
        } catch (Exception e) {
            Log.d("ERROR", "initXMPPTool: " + e.getMessage());
        }
    }
}
