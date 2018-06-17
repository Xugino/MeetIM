package com.xieyangzhe.meetim.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.xieyangzhe.meetim.R;
import com.xieyangzhe.meetim.Utils.XMPPTool;

public class AddContactActivity extends AppCompatActivity {

    private EditText textAddContact;
    private Button buttonAddContact;
    private Toolbar toolbar;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    Toast.makeText(AddContactActivity.this, "Failed", Toast.LENGTH_SHORT);
                    break;
                case 1:
                    AlertDialog dialog = new AlertDialog.Builder(AddContactActivity.this)
                            .setMessage("Add contact success")
                            .setPositiveButton("Back", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivity(new Intent(AddContactActivity.this, MainActivity.class));
                                    finish();
                                }
                            }).create();
                    dialog.show();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        toolbar = findViewById(R.id.toolbar_chat);
        toolbar.setTitle("Add Contact");

        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(view -> {
            finish();
        });

        textAddContact = findViewById(R.id.text_add_contact);
        buttonAddContact = findViewById(R.id.button_add_contact);

        buttonAddContact.setOnClickListener(view -> {
            doAddContact();
        });
    }

    private void doAddContact() {
        String username = textAddContact.getText().toString();
        new Thread(() -> {
            handler.sendEmptyMessage(XMPPTool.getXmppTool().addContact(username) ? 1 : 0);
        }).start();
    }
}
