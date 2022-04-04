package com.example.performdemo.core;

import android.view.Choreographer;

import androidx.annotation.NonNull;

import com.example.performdemo.anr.FrameUpdateLister;

import java.util.ArrayList;

public class MTChoreographerFrameMonitor {
    private boolean isStart;
    private long mLastFrameTimeNanos = 0L;
    private final ArrayList<FrameUpdateLister> mFrameListeners = new ArrayList();
    //Choreographer是监听刷新机制
    private Choreographer.FrameCallback mFrameCallback = new Choreographer.FrameCallback() {
        @Override
        public void doFrame(long frameTimeNanos) {
            if (!isStart) {
                return;
            }
            if (mLastFrameTimeNanos != 0) {
                long diffFrameTime = frameTimeNanos - mLastFrameTimeNanos;
                for (FrameUpdateLister listener : mFrameListeners) {
                    listener.doFrame(diffFrameTime);
                }
            }
            mLastFrameTimeNanos = frameTimeNanos;
            Choreographer.getInstance().postFrameCallback(this);
        }
    };

    public void startUpdateFrameMonitor() {
        isStart = true;
        Choreographer.getInstance().postFrameCallback(mFrameCallback);
    }

    public void stopUpdateFrameMonitor() {
        isStart = false;
        Choreographer.getInstance().removeFrameCallback(mFrameCallback);
    }

    public void addFrameUpdateListener(@NonNull FrameUpdateLister listener) {
        mFrameListeners.add(listener);
    }

    public void removeFrameUpdateListener(@NonNull FrameUpdateLister listener) {
        this.mFrameListeners.remove(listener);
    }

    public int getCurrentListenerSize() {
        return mFrameListeners.size();
    }

}
