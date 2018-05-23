package com.xieyangzhe.meetim.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.xieyangzhe.meetim.R;
import com.xieyangzhe.meetim.Utils.PreferencesUtils;

public class MainActivity extends AppCompatActivity {
    Button buttonTest;
    Button buttonSend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonTest = findViewById(R.id.button_test);
        buttonSend = findViewById(R.id.button_send);
        buttonTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferencesUtils.getInstance().saveData("Hello", "TTTTTTTTT");
                String tmp = (String) PreferencesUtils.getInstance().getData("Hello", "");
                Log.d("TEST", tmp);
            }
        });

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });
    }
}
