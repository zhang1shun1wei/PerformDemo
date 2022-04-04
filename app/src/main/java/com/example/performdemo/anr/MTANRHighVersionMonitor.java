package com.example.performdemo.anr;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.performdemo.core.ChoreographerMonitorCenter;
import com.example.performdemo.core.MTChoreographerFrameMonitor;
import com.google.gson.Gson;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class MTANRHighVersionMonitor implements FrameUpdateLister {
    private static final String TAG = "MTANRHighVersionMonitor";
    private HandlerThread monitorThread;
    private Handler stackShowHandler;
    private Map<String, MTBlockStackTraceInfo> mBlockStackTraces = new HashMap<String, MTBlockStackTraceInfo>();
    private long mStackCollectPeriod = 16666666L;
    private long mStackPostDelayTime = TimeUnit.MICROSECONDS.convert(mStackCollectPeriod, TimeUnit.NANOSECONDS);
    private long mAnrCheckPeriodtime = TimeUnit.NANOSECONDS.convert(5, TimeUnit.SECONDS);
    private int mCollectCount = 0;
    private boolean mIsStartMonitor = false;
    private Runnable blockStackCollectTask = new Runnable() {
        @Override
        public void run() {
            mCollectCount++;
            if (mCollectCount * mStackCollectPeriod >= mAnrCheckPeriodtime) {
                String stackStr = traceToString(Looper.getMainLooper().getThread().getStackTrace());
                Log.d(TAG, "监测到ANR: run: ---->stackStr" + stackStr);
                stackShowHandler.removeCallbacks(this);
                mBlockStackTraces.clear();
            } else {
                stackShowHandler.postDelayed(this, 16);
            }
        }
    };

    public final String traceToString(@NonNull StackTraceElement[] stackArray) {
        if (stackArray.length == 0) {
            return "[]";
        } else {
            StringBuilder b = new StringBuilder();
            for (int i = 0; i < stackArray.length; i++) {
                if (i <= 0) {
                    continue;
                }
                b.append(stackArray[i]);
                b.append("\n");
                if (i > 20) {
                    break;
                }
            }
            return b.toString();
        }
    }

    public void startANRMonitor() {
        monitorThread = new HandlerThread("mt_anr_monitor");
        monitorThread.start();
        stackShowHandler = new Handler(monitorThread.getLooper());
        ChoreographerMonitorCenter.addSimpleFrameUpdateListener(this);
        mIsStartMonitor = true;
    }

    public void stopANRMonitor() {
        if (monitorThread == null) {
            return;
        }
        monitorThread.quitSafely();
        ChoreographerMonitorCenter.removeSimpleFrameUpdateListener(this);
        mIsStartMonitor = false;
    }

    @Override
    public void doFrame(long frameCostTime) {
        stackShowHandler.removeCallbacks(blockStackCollectTask);
        mCollectCount = 0;
        stackShowHandler.postDelayed(blockStackCollectTask, 16);
    }
}
