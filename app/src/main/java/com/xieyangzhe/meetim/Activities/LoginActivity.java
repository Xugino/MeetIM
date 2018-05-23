package com.xieyangzhe.meetim.Activities;

import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.xieyangzhe.meetim.R;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    // UI references.
    private AutoCompleteTextView textUsername;
    private EditText textPassword;
    private Button buttonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        textUsername = findViewById(R.id.username);
        buttonLogin = findViewById(R.id.sign_in_button);
        textPassword = findViewById(R.id.password);

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


        buttonLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

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

    }

    private boolean isUsernameValid(String username) {
        return true;
    }

    private boolean isPasswordValid(String password) {
        return true;
    }
}

