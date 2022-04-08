package com.example.performdemo;

import android.app.Application;

import com.demo.native_crash.MTNativeCrashCaptor;
import com.example.performdemo.anr.MTANRHighVersionMonitor;
import com.example.performdemo.memory.MTMemoryMonitor;
import com.example.performdemo.traffic.MTTrafficMonitor;

public class MyApplication extends Application {
    private MTANRHighVersionMonitor mANRHighVersionMonitor;
    private MTMemoryMonitor mMTMemoryMonitor;
    private MTTrafficMonitor mMTTrafficMonitor;
    private MTNativeCrashCaptor mtNativeCrashCaptor;

    @Override
    public void onCreate() {
        super.onCreate();
        //检测ANR
        mANRHighVersionMonitor = new MTANRHighVersionMonitor();
        mANRHighVersionMonitor.startANRMonitor();
        //检测内存使用
        mMTMemoryMonitor = new MTMemoryMonitor();
        mMTMemoryMonitor.startMemoryMonitor();
        //检测流量使用
        mMTTrafficMonitor = new MTTrafficMonitor();
        mMTTrafficMonitor.startTrafficMonitor(getApplicationContext());
        //检测native crash使用
        mtNativeCrashCaptor = new MTNativeCrashCaptor();
        mtNativeCrashCaptor.init();


    }
}
