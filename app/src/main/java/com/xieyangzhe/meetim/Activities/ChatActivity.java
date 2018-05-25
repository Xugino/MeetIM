package com.xieyangzhe.meetim.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.github.bassaer.chatmessageview.model.Message;
import com.github.bassaer.chatmessageview.view.ChatView;
import com.xieyangzhe.meetim.Models.Contact;
import com.xieyangzhe.meetim.R;
import com.xieyangzhe.meetim.Utils.XMPPTool;

public class ChatActivity extends AppCompatActivity {

    private ChatView chatView;
    private BroadcastReceiver broadcastReceiver;
    private Contact you;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Bundle bundle = this.getIntent().getExtras();
        you = new Contact (
                bundle.getString("JID_TO"),
                bundle.getString("USERNAME_TO"),
                bundle.getString("HEAD_TO")
        );

        chatView = findViewById(R.id.chat_view);
        chatView.setOnClickSendButtonListener(view -> {

            Contact me = new Contact ("", XMPPTool.getCurrentUserName(), "");
            String msgText = chatView.getInputText();

            doSendMessage(msgText, you);

            Message message1 = new Message.Builder()
                    .setUser(me)
                    .setRight(true)
                    .setText(msgText)
                    .build();

            chatView.send(message1);
            chatView.setInputText("");


            /*
            Message message2 = new Message.Builder()
                    .setUser(you)
                    .setRight(false)
                    .setText(you.getName())
                    .build();

            chatView.receive(message2);
            */
        });

        chatView.setOnClickOptionButtonListener(view -> {
        });

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String fromUsername = intent.getStringExtra("FROM_USERNAME");
                String fromMsgBody = intent.getStringExtra("FROM_MSG_BODY");

                if (fromUsername.equals(you.getJid())) {
                    Message messageYou = new Message.Builder()
                            .setUser(you)
                            .setRight(false)
                            .setText(fromMsgBody)
                            .build();
                    chatView.receive(messageYou);
                }
            }
        };

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(XMPPTool.MESSAGE_RECEIVER);
        registerReceiver(broadcastReceiver, intentFilter);
    }

    private void doSendMessage(String msgText, Contact you) {
        new Thread(() -> {
            XMPPTool.getXmppTool().sendMessage(msgText, you);
        }).start();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }
}
