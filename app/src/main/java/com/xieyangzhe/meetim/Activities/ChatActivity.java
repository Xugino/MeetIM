package com.xieyangzhe.meetim.Activities;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Bundle bundle = this.getIntent().getExtras();
        final Contact you = new Contact (
                bundle.getString("JID_TO"),
                bundle.getString("USERNAME_TO"),
                bundle.getString("HEAD_TO")
        );

        final Contact me = new Contact (
                "",
                XMPPTool.getCurrentUserName(),
                ""
        );

        chatView = findViewById(R.id.chat_view);

        chatView.setOnClickSendButtonListener(view -> {


            String msgText = chatView.getInputText();
            Message message1 = new Message.Builder()
                    .setUser(me)
                    .setRight(true)
                    .setText(msgText)
                    .build();

            Message message2 = new Message.Builder()
                    .setUser(you)
                    .setRight(false)
                    .setText(you.getName())
                    .build();

            chatView.send(message1);
            chatView.receive(message2);
            chatView.setInputText("");
        });

        chatView.setOnClickOptionButtonListener(view -> {
        });
    }
}
