package com.example.performdemo.core;

import android.os.Looper;
import android.util.Printer;

import androidx.annotation.UiThread;

import com.example.performdemo.utils.ReflectHelperUtil;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class UIThreadLooperMonitor {
    private ArrayList<LooperHandleEventListener> mHandleEventListeners = new ArrayList();
    private Printer mHookedPrinter;
    private Printer mOriginPrinter;
    private boolean enable;

    public UIThreadLooperMonitor() {
        mOriginPrinter = (Printer) ReflectHelperUtil.getInstance().reflectField(Looper.getMainLooper(), "mLogging");
        mHookedPrinter = new Printer() {
            public void println(String x) {
                Printer printer = mOriginPrinter;
                if (printer != null) {
                    printer.println(x);
                }

                if (getEnable()) {
                    boolean dispatch = x.charAt(0) == '>' || x.charAt(0) == '<';
                    if (dispatch) {
                        notifyListenerLooperProcessMsg(x.charAt(0) == '>');
                    }

                }
            }
        };
        Looper.getMainLooper().setMessageLogging(mHookedPrinter);
    }


    public boolean getEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public void addLooperHandleEventListener(@NotNull LooperHandleEventListener listener) {
        mHandleEventListeners.add(listener);
    }

    public void removeLooperHandleEventListener(@NotNull LooperHandleEventListener listener) {
        mHandleEventListeners.remove(listener);
    }

    @UiThread
    private void notifyListenerLooperProcessMsg(boolean start) {
        for (LooperHandleEventListener listener : mHandleEventListeners) {
            if (start) {
                listener.onMessageLooperStartHandleMessage();
            } else {
                listener.onMessageLooperStopHandleMessage();
            }
        }
    }

    public interface LooperHandleEventListener {
        void onMessageLooperStartHandleMessage();

        void onMessageLooperStopHandleMessage();
    }
}
