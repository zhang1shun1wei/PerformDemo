package com.demo.native_crash;

import android.util.Log;

public class MTNativeCrashCaptor {

    static {
        System.loadLibrary("mt-crash");
    }

    public void init() {
        try {
            String nativeString = nativeInitCaptor("1.0");
            Log.d("zsw11", "init: ->nativeString=" + nativeString);
        } catch (Exception e) {
        }
    }

    public static void onCaptureNativeCrash() {
        Log.d("zsw11", "receive native crash callback in java thread ! -> current thread : "
                + Thread.currentThread().getName());
    }

    native String nativeInitCaptor(String version);

    public native void nativeCrash();

}
