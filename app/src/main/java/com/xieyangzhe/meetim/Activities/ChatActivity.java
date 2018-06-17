package com.xieyangzhe.meetim.Activities;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.github.bassaer.chatmessageview.model.Message;
import com.github.bassaer.chatmessageview.view.ChatView;
import com.github.bassaer.chatmessageview.view.MessageView;
import com.xieyangzhe.meetim.Models.ChatMessage;
import com.xieyangzhe.meetim.Models.ChatMessageList;
import com.xieyangzhe.meetim.Models.Contact;
import com.xieyangzhe.meetim.R;
import com.xieyangzhe.meetim.Services.XMPPService;
import com.xieyangzhe.meetim.Utils.BitmapAndStringUtils;
import com.xieyangzhe.meetim.Utils.DBTool;
import com.xieyangzhe.meetim.Utils.OkHttp3Util;
import com.xieyangzhe.meetim.Utils.PictureTool;
import com.xieyangzhe.meetim.Utils.TimeFormatter;
import com.xieyangzhe.meetim.Utils.XMPPTool;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private ChatView chatView;
    private BroadcastReceiver broadcastReceiver;
    private BroadcastReceiver uploadReceiver;
    private Contact you;
    private Contact me;
    private ChatMessageList messageList;
    private DBTool dbTool;

    private Toolbar toolbar;

    protected static final int BACKGROUND_COLOR = R.color.gray200;
    protected static final int SEND_BUTTON_COLOR = R.color.blueGray500;
    protected static final int OPTION_BUTTON_COLOR = R.color.teal500;
    protected static final String INPUT_TEXT_HINT = "New message..";
    protected static final int MESSAGE_MARGIN = 5;
    private static final int PHOTO_REQUEST_GALLERY = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        dbTool = new DBTool();
        chatView = findViewById(R.id.chat_view);

        Bundle bundle = this.getIntent().getExtras();
        you = new Contact (
                bundle.getString("JID_TO"),
                bundle.getString("USERNAME_TO"),
                bundle.getString("HEAD_TO")
        );

        toolbar = findViewById(R.id.toolbar_chat);
        toolbar.setTitle(you.getName());

        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(view -> {
            finish();
        });

        me = new Contact ("", XMPPTool.getCurrentUserName(), "");

        loadMessages();

        chatView.setBackgroundColor(ContextCompat.getColor(this, BACKGROUND_COLOR));
        chatView.setSendButtonColor(ContextCompat.getColor(this, SEND_BUTTON_COLOR));
        chatView.setOptionButtonColor(OPTION_BUTTON_COLOR);
        chatView.setInputTextHint(INPUT_TEXT_HINT);
        chatView.setMessageMarginTop(MESSAGE_MARGIN);
        chatView.setMessageMarginBottom(MESSAGE_MARGIN);
        chatView.setMaxInputLine(5);
        chatView.setUsernameFontSize(getResources().getDimension(R.dimen.font_small));
        chatView.setAutoHidingKeyboard(false);


        uploadReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String path = "http://img.xieyangzhe.com/img/" + intent.getStringExtra("filename");
                doSendMessage(path, you);
            }
        };
        IntentFilter intentFilter1 = new IntentFilter();
        intentFilter1.addAction(PictureTool.UPLOAD_SUCCESS);
        registerReceiver(uploadReceiver, intentFilter1);

        chatView.setOnClickSendButtonListener(view -> {


            String msgText = chatView.getInputText();
            if (msgText.equals("")) {
                return;
            }

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
            Intent intent1 = new Intent(Intent.ACTION_PICK);
            intent1.setType("image/*");
            startActivityForResult(intent1, PHOTO_REQUEST_GALLERY);
        });

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String fromUsername = intent.getStringExtra("FROM_USERNAME");
                String fromMsgBody = intent.getStringExtra("FROM_MSG_BODY");


                if (fromUsername.equals(you.getJid())) {
                    Message messageYou;
                    if (fromMsgBody.contains("http://") && fromMsgBody.contains(".png")) {
                        String path =  PictureTool.DIR + OkHttp3Util.getNameFromUrl(fromMsgBody);
                        Log.d("asdasd", path);
                        messageYou = new Message.Builder()
                                .setUser(you)
                                .setRight(false)
                                .setText(fromMsgBody)
                                .setType(Message.Type.PICTURE)
                                .setPicture(PictureTool.getPic(path))
                                .build();
                    } else {
                        messageYou = new Message.Builder()
                                .setUser(you)
                                .setRight(false)
                                .setText(fromMsgBody)
                                .build();
                    }
                    chatView.receive(messageYou);
                }
            }
        };

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(XMPPService.MESSAGE_RECEIVER);
        registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PHOTO_REQUEST_GALLERY) {

            Handler handler = new Handler() {
                @Override
                public void handleMessage(android.os.Message msg) {
                    switch (msg.what) {
                        case 0:
                            Bitmap bitmap = (Bitmap) msg.obj;
                            String filename = msg.getData().getString("filename");
                            Message message = new Message.Builder()
                                    .setUser(me)
                                    .setRight(true)
                                    .setType(Message.Type.PICTURE)
                                    .setPicture(bitmap)
                                    .setText(filename)
                                    .build();
                            chatView.send(message);
                            messageList.add(message);
                            saveMessage(message);
                            break;
                    }
                }
            };
            new Thread(() -> {
                try {
                    Uri uri = data.getData();
                    ContentResolver contentResolver = getContentResolver();
                    Bitmap tmpbitmap = PictureTool.getCompresedImage(uri.getPath().replace("/raw/", ""));
                    //Bitmap tmpbitmap = PictureTool.compressImage(BitmapFactory.decodeStream(contentResolver.openInputStream(uri)));
                    String tmpfilename = PictureTool.savePic(tmpbitmap);
                    PictureTool.uploadPic(tmpbitmap, tmpfilename);
                    android.os.Message msg = new android.os.Message();
                    msg.what = 0;
                    Bundle bundle = new Bundle();
                    bundle.putString("filename", tmpfilename);
                    msg.setData(bundle);
                    msg.obj = tmpbitmap;

                    handler.sendMessage(msg);
                } catch (Exception e) {
                    Log.d("Error", "onActivityResult: " + e.getMessage());
                }
            }).start();

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void doSendMessage(String msgText, Contact you) {
        new Thread(() -> {
            XMPPTool.getXmppTool().sendMessage(msgText, you);
        }).start();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(uploadReceiver);
        unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }

    private void loadMessages() {

        List<Message> messages;
        List<Message> savedMessages = new ArrayList<>();
        messageList = dbTool.get(you.getName());
        if (messageList == null) {
            messageList = new ChatMessageList();
            return;
        }
        messages = messageList.getMessages();

        for (Message message: messages) {
            Message newMessage;
            if (message.getType() == Message.Type.PICTURE) {
                newMessage = new Message.Builder()
                        .setUser(message.isRight() ? me : you)
                        .setRight(message.isRight())
                        .setType(Message.Type.PICTURE)
                        .setPicture(message.getPicture())
                        .setSendTime(message.getSendTime())
                        .build();
            } else {
                newMessage = new Message.Builder()
                        .setUser(message.isRight() ? me : you)
                        .setRight(message.isRight())
                        .setText(message.getText())
                        .setSendTime(message.getSendTime())
                        .build();
            }

            savedMessages.add(newMessage);
        }
        MessageView messageView = chatView.getMessageView();

        messageView.init(savedMessages);
        messageView.setSelection(messageView.getCount() - 1);
    }

    private void saveMessage(Message message) {
        dbTool.addSingleMessage(new ChatMessageList().convertToChatMessage(message), you.getName());
    }
}
