package com.example.native_jvmti;

import android.content.Context;
import android.os.Build;
import android.os.Debug;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.File;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MTJvmTi {

    private final String TAG = "JvmTi";
    private final String JVMTI_AGENT = "jvmti";
    private HandlerThread monitorThread;
    private NativeCommunityHandler nativeCommunityHandler;

    public void start(Context context) {
        monitorThread = new HandlerThread("thread_monitor_thread");
        monitorThread.start();
        nativeCommunityHandler = new NativeCommunityHandler(monitorThread.getLooper());
        init(context, nativeCommunityHandler);
    }


    private void init(Context context, NativeCommunityHandler communityHandler) {
        attachJvmTiAgent(context);
        registerCommunityHandler(communityHandler);
    }

    private void attachJvmTiAgent(Context context) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                ClassLoader classLoader = context.getClassLoader();
                Method findLibrary = ClassLoader.class.getDeclaredMethod("findLibrary", String.class);
                String jvmtiAgentLibPath = (String) findLibrary.invoke(classLoader, JVMTI_AGENT);
                Log.d(TAG, "jvmtiagentlibpath " + jvmtiAgentLibPath);

                File filesDir = context.getFilesDir();
                File jvmtiLibDir = new File(filesDir, "jvmti");
                if (!jvmtiLibDir.exists()) {
                    jvmtiLibDir.mkdirs();

                }
                File agentLibSo = new File(jvmtiLibDir, "agent.so");
                if (agentLibSo.exists()) {
                    agentLibSo.delete();
                }
                Files.copy(Paths.get(new File(jvmtiAgentLibPath).getAbsolutePath()), Paths.get((agentLibSo).getAbsolutePath()));

                Log.d(TAG, agentLibSo.getAbsolutePath() + "," + context.getPackageCodePath());

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    Debug.attachJvmtiAgent(agentLibSo.getAbsolutePath(), null, classLoader);
                    Log.d(TAG, "Debug.attachJvmTiAgent ----> ");
                } else {
                    try {
                        Class vmDebugClazz = Class.forName("dalvik.system.VMDebug");
                        Method attachAgentMethod = vmDebugClazz.getMethod("attachAgent", String.class);
                        attachAgentMethod.setAccessible(true);
                        attachAgentMethod.invoke(null, agentLibSo.getAbsolutePath());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                System.loadLibrary(JVMTI_AGENT);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public native static void registerCommunityHandler(NativeCommunityHandler handler);

}
