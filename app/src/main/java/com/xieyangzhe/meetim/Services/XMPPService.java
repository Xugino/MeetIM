package com.xieyangzhe.meetim.Services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import com.github.bassaer.chatmessageview.model.Message;
import com.xieyangzhe.meetim.Activities.ChatActivity;
import com.xieyangzhe.meetim.Models.ChatMessage;
import com.xieyangzhe.meetim.R;
import com.xieyangzhe.meetim.Utils.ActivityLifecycleListener;
import com.xieyangzhe.meetim.Utils.DBTool;
import com.xieyangzhe.meetim.Utils.IMApplication;
import com.xieyangzhe.meetim.Utils.NotificationTool;
import com.xieyangzhe.meetim.Utils.OkHttp3Util;
import com.xieyangzhe.meetim.Utils.PictureTool;
import com.xieyangzhe.meetim.Utils.PreferencesUtils;
import com.xieyangzhe.meetim.Utils.XMPPTool;

import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;

import java.util.Calendar;

public class XMPPService extends Service {

    private XMPPTool xmppTool;
    private boolean threadStatus;
    private Thread xmppThread;
    private Handler xmppHandler;

    public static final String LOGIN_RECEIVER = "com.xieyangzhe.meetim.RECEIVER";
    public static final String LOGIN_STATUS = "com.xieyangzhe.meetim.LOGIN_STATUS";
    public final static String MESSAGE_RECEIVER = "com.xieyangzhe.meetim.MESSAGE";

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
            if (xmppTool.doLogin(username, password)) {
                XMPPTool.chatManager.addChatListener(new ChatManagerListener() {
                    @Override
                    public void chatCreated(Chat chat, boolean createdLocally) {
                        if (!createdLocally) {
                            chat.addMessageListener(new ChatMessageListener() {
                                @Override
                                public void processMessage(Chat chat, org.jivesoftware.smack.packet.Message message) {
                                    //TODO: Process message
                                    Log.d("CHAT", message.toString());
                                    DBTool dbTool = new DBTool();

                                    ChatMessage chatMessage;
                                    if (message.getBody().contains("http://") && message.getBody().contains(".png")) {
                                        PictureTool.downloadPic(message.getBody());
                                        chatMessage = new ChatMessage(OkHttp3Util.getNameFromUrl(message.getBody()), message.getFrom(), Calendar.getInstance(), false, true);
                                    } else {
                                        chatMessage = new ChatMessage(message.getBody(), message.getFrom(), Calendar.getInstance(), false, false);
                                    }

                                    dbTool.addSingleMessage(chatMessage, message.getFrom().replaceAll("@39.105.73.30/Android", ""));

                                    String username = message.getFrom().replaceAll("@39.105.73.30/Android", "");
                                    String jid = username + "@39.105.73.30";

                                    Intent tmpIntent = new Intent(XMPPService.this, ChatActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putString("JID_TO", jid);
                                    bundle.putString("USERNAME_TO", username);
                                    bundle.putString("HEAD_TO", "");
                                    tmpIntent.putExtras(bundle);
                                    PendingIntent pendingIntent = PendingIntent.getActivity(XMPPService.this, 0, tmpIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                                    Intent intent = new Intent(MESSAGE_RECEIVER);
                                    intent.putExtra("FROM_USERNAME", message.getFrom().replaceAll("/Android", ""));
                                    intent.putExtra("FROM_MSG_BODY", message.getBody());
                                    IMApplication.getAppContext().sendBroadcast(intent);

                                    if (ActivityLifecycleListener.isBackGround()) {
                                        NotificationTool notificationUtils = new NotificationTool(IMApplication.getAppContext());
                                        notificationUtils.sendNotification(username, message.getBody(), pendingIntent);
                                    }
                                }
                            });
                        }
                    }
                });
            }
        } catch (Exception e) {
            Log.d("ERROR", "initXMPPTool: " + e.getMessage());
        }
    }
}
