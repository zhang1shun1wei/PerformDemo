package com.example.performdemo;

import android.app.Application;

import com.example.performdemo.anr.MTANRHighVersionMonitor;
import com.example.performdemo.memory.MTMemoryMonitor;

public class MyApplication extends Application {
    private MTANRHighVersionMonitor mANRHighVersionMonitor;
    private MTMemoryMonitor mMTMemoryMonitor;

    @Override
    public void onCreate() {
        super.onCreate();
        mANRHighVersionMonitor = new MTANRHighVersionMonitor();
        mANRHighVersionMonitor.startANRMonitor();
        mMTMemoryMonitor = new MTMemoryMonitor();
        mMTMemoryMonitor.open();
    }
}
