package com.example.native_jvmti;


import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

public class NativeCommunityHandler extends Handler {

    public NativeCommunityHandler(Looper looper) {
        super(looper);
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        super.handleMessage(msg);
        Log.d("zsw11", "receive message !!");
    }

    public void sendStrMsg(String msgStr) {
        Message msg = Message.obtain();
        msg.obj = msgStr;
        sendMessage(msg);
    }

}
