package com.xieyangzhe.meetim.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.github.bassaer.chatmessageview.model.Message;
import com.github.bassaer.chatmessageview.view.ChatView;
import com.github.bassaer.chatmessageview.view.MessageView;
import com.xieyangzhe.meetim.Models.ChatMessage;
import com.xieyangzhe.meetim.Models.ChatMessageList;
import com.xieyangzhe.meetim.Models.Contact;
import com.xieyangzhe.meetim.R;
import com.xieyangzhe.meetim.Utils.DBTool;
import com.xieyangzhe.meetim.Utils.TimeFormatter;
import com.xieyangzhe.meetim.Utils.XMPPTool;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private ChatView chatView;
    private BroadcastReceiver broadcastReceiver;
    private Contact you;
    private ChatMessageList messageList;
    private DBTool dbTool;

    protected static final int RIGHT_BUBBLE_COLOR = R.color.colorPrimaryDark;
    protected static final int LEFT_BUBBLE_COLOR = R.color.gray300;
    protected static final int BACKGROUND_COLOR = R.color.gray200;
    protected static final int SEND_BUTTON_COLOR = R.color.blueGray500;
    protected static final int OPTION_BUTTON_COLOR = R.color.teal500;
    protected static final int RIGHT_MESSAGE_TEXT_COLOR = Color.WHITE;
    protected static final int LEFT_MESSAGE_TEXT_COLOR = Color.BLACK;
    protected static final String INPUT_TEXT_HINT = "New message..";
    protected static final int MESSAGE_MARGIN = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        dbTool = new DBTool();
        chatView = findViewById(R.id.chat_view);

        chatView.setRightBubbleColor(ContextCompat.getColor(this,RIGHT_BUBBLE_COLOR));
        chatView.setLeftBubbleColor(ContextCompat.getColor(this, LEFT_BUBBLE_COLOR));
        chatView.setBackgroundColor(ContextCompat.getColor(this, BACKGROUND_COLOR));
        chatView.setSendButtonColor(ContextCompat.getColor(this, SEND_BUTTON_COLOR));
        chatView.setOptionIcon(R.drawable.ic_account_circle);
        chatView.setOptionButtonColor(OPTION_BUTTON_COLOR);
        chatView.setRightMessageTextColor(RIGHT_MESSAGE_TEXT_COLOR);
        chatView.setLeftMessageTextColor(LEFT_MESSAGE_TEXT_COLOR);
        chatView.setInputTextHint(INPUT_TEXT_HINT);
        chatView.setMessageMarginTop(MESSAGE_MARGIN);
        chatView.setMessageMarginBottom(MESSAGE_MARGIN);
        chatView.setMaxInputLine(5);
        chatView.setUsernameFontSize(getResources().getDimension(R.dimen.font_small));

        Bundle bundle = this.getIntent().getExtras();
        you = new Contact (
                bundle.getString("JID_TO"),
                bundle.getString("USERNAME_TO"),
                bundle.getString("HEAD_TO")
        );

        loadMessages();

        chatView.setOnClickSendButtonListener(view -> {

            Contact me = new Contact ("", XMPPTool.getCurrentUserName(), "");
            String msgText = chatView.getInputText();

            doSendMessage(msgText, you);

            Message messageMe = new Message.Builder()
                    .setUser(me)
                    .setRight(true)
                    .setText(msgText)
                    .build();

            chatView.send(messageMe);
            chatView.setInputText("");

            messageList.add(messageMe);
            saveMessage(messageMe);
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
                    saveMessage(messageYou);
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

    private void loadMessages() {

        List<Message> messages;
        messageList = dbTool.get();
        messages = messageList.getMessages();

        MessageView messageView = chatView.getMessageView();

        messageView.init(messages);
        messageView.setSelection(messageView.getCount() - 1);
    }

    private void saveMessage(Message message) {
        dbTool.addSingleMessage(new ChatMessageList().convertToChatMessage(message));
    }
}
