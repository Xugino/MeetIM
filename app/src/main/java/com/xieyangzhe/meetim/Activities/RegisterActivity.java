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

import com.xieyangzhe.meetim.R;
import com.xieyangzhe.meetim.Utils.XMPPTool;

public class RegisterActivity extends AppCompatActivity {

    private EditText textRegisterUsername;
    private EditText textRegisterPassword;
    private Button buttonRegister;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    Toast.makeText(RegisterActivity.this, "Register failed", Toast.LENGTH_LONG).show();
                    break;
                case 1:
                    AlertDialog dialog = new AlertDialog.Builder(RegisterActivity.this)
                            .setMessage("Register success")
                            .setPositiveButton("Back to login", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
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
        setContentView(R.layout.activity_register);
        textRegisterUsername = findViewById(R.id.register_username);
        textRegisterPassword = findViewById(R.id.register_password);
        buttonRegister = findViewById(R.id.register_button);

        buttonRegister.setOnClickListener( view -> {
            new Thread(() -> {
                if (doRegister()) {
                    handler.sendEmptyMessage(1);
                } else {
                    handler.sendEmptyMessage(0);
                }
            }).start();
        });
    }

    private boolean doRegister() {
        return XMPPTool.getXmppTool().register(textRegisterUsername.getText().toString(),
                textRegisterPassword.getText().toString());
    }
}
