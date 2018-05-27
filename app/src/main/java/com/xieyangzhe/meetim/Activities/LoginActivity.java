package com.xieyangzhe.meetim.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.xieyangzhe.meetim.R;
import com.xieyangzhe.meetim.Services.XMPPService;
import com.xieyangzhe.meetim.Utils.PreferencesUtils;
import com.xieyangzhe.meetim.Utils.XMPPTool;

import org.jivesoftware.smackx.iqregister.packet.Registration;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    // UI references.
    private AutoCompleteTextView textUsername;
    private EditText textPassword;
    private Button buttonLogin;
    private TextView textRegister;

    private BroadcastReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        textUsername = findViewById(R.id.username);
        buttonLogin = findViewById(R.id.sign_in_button);
        textPassword = findViewById(R.id.password);
        textRegister = findViewById(R.id.text_register);

        textPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        buttonLogin.setOnClickListener(view -> {
                attemptLogin();
        });

        textRegister.setOnClickListener(view -> {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                finish();
        });

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean loginStatus = intent.getBooleanExtra(XMPPService.LOGIN_STATUS, false);
                if (loginStatus) {
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Log in failed, please try again.", Toast.LENGTH_LONG).show();
                    stopService(new Intent(LoginActivity.this, XMPPService.class));
                }
            }
        };

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(XMPPService.LOGIN_RECEIVER);
        registerReceiver(receiver, intentFilter);
    }

    private void attemptLogin() {

        textUsername.setError(null);
        textPassword.setError(null);

        String username = textUsername.getText().toString();
        String password = textPassword.getText().toString();

        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            textPassword.setError("Error");
        }

        if (TextUtils.isEmpty(username)) {
            textUsername.setError("Error");
        } else if (!isUsernameValid(username)) {
            textUsername.setError("Error");
        }

        PreferencesUtils.getInstance().saveData("username", username);
        PreferencesUtils.getInstance().saveData("password", password);

        if (XMPPTool.getLoginStatus()) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        } else {
            startService(new Intent(this, XMPPService.class));
        }
    }

    private boolean isUsernameValid(String username) {
        return true;
    }

    private boolean isPasswordValid(String password) {
        return true;
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }
}

