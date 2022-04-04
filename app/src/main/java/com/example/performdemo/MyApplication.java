package com.example.performdemo;

import android.app.Application;

import com.example.performdemo.anr.MTANRHighVersionMonitor;

public class MyApplication extends Application {
    private MTANRHighVersionMonitor mANRHighVersionMonitor;

    @Override
    public void onCreate() {
        super.onCreate();
        mANRHighVersionMonitor = new MTANRHighVersionMonitor();
        mANRHighVersionMonitor.startANRMonitor();
    }
}
