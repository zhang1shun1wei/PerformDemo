package com.example.performdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.demo.native_crash.MTNativeCrashCaptor;

public class MainActivity extends AppCompatActivity {
    private Button mARNButton;
    private Button mNativeCrashButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mARNButton = findViewById(R.id.btn_anr);
        mNativeCrashButton = findViewById(R.id.btn_native_crash);
        mARNButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Thread.sleep(15000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        mNativeCrashButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MTNativeCrashCaptor mtNativeCrashCaptor = new MTNativeCrashCaptor();
                mtNativeCrashCaptor.nativeCrash();
            }
        });
    }
}
