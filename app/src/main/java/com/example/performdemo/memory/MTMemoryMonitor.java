package com.example.performdemo.memory;

import android.os.Debug;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import java.util.Formatter;


public class MTMemoryMonitor {
    private static final long MEMORY_COLLECT_PERIOD = 2000L;
    private Handler memoryRefreshHandler;
    private HandlerThread mMonitorThread;
    private Runnable memoryCollectRunnable = new Runnable() {
        @Override
        public void run() {
            MTMemoryInfo memInfo = getMemoryInfo();
            String size = formatFileSize(memInfo.totalSize);
            Log.d("zsw11", "totalSize: " + size);
            Log.d("zsw11", "nativeSize:" + formatFileSize(memInfo.nativeSize));
            Log.d("zsw11", "vmSize:" + formatFileSize(memInfo.vmSize));
            memoryRefreshHandler.postDelayed(memoryCollectRunnable, MEMORY_COLLECT_PERIOD);
        }
    };

    private String formatFileSize(int size) {
        if (size <= 0) return "";
        Formatter formater = new Formatter();
        String formatSize = "";
        if (size <= 1024) {
            formatSize = size + "B";
        } else if (size > 1024 && size <= 1024 * 1024) {
            formatSize = formater.format("%.2f KB", size / 1024f).toString();
        } else if (size > 1024 * 1024 && size <= 1024 * 1024 * 1024) {
            formatSize = formater.format("%.2f MB", size / 1024f / 1024f).toString();
        } else {
            formatSize = formater.format("%.2f GB", size / 1024f / 1024f / 1024f).toString();
        }
        return formatSize;
    }

    private MTMemoryInfo getMemoryInfo() {
        return getMemoryInfoInDebug();
    }

    public void startMemoryMonitor() {
        mMonitorThread = new HandlerThread("rabbit_memory_monitor_thread");
        mMonitorThread.start();
        memoryRefreshHandler = new Handler(mMonitorThread.getLooper());
        memoryRefreshHandler.postDelayed(memoryCollectRunnable, MEMORY_COLLECT_PERIOD);

    }

    public void stopMonitor() {

    }

    private MTMemoryInfo getMemoryInfoInDebug() {
        Debug.MemoryInfo info = new Debug.MemoryInfo();
        Debug.getMemoryInfo(info);
        MTMemoryInfo memInfo = new MTMemoryInfo();
        memInfo.totalSize = (info.getTotalPss()) * 1024;
        memInfo.vmSize = (info.dalvikPss) * 1024;
        memInfo.nativeSize = info.nativePss * 1024;
        memInfo.othersSize = info.otherPss * 1024 + info.getTotalSwappablePss() * 1024;
        memInfo.time = System.currentTimeMillis();
        return memInfo;

    }
}
