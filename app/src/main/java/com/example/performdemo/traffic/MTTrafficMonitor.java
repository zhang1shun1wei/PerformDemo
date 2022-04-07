package com.example.performdemo.traffic;

import android.app.usage.NetworkStats;
import android.app.usage.NetworkStatsManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.RemoteException;
import android.util.Log;

public class MTTrafficMonitor {
    private boolean isOpen;

    public void startTrafficMonitor(Context context) {
        NetworkStatsManager networkService;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            networkService = (NetworkStatsManager) context.getSystemService(Context.NETWORK_STATS_SERVICE);
            if (networkService == null) {
            } else {
                NetworkStats trafficStatus = null;
                try {
                    trafficStatus = networkService.querySummary(ConnectivityManager.TYPE_WIFI, getAppUid(context),
                            System.currentTimeMillis() - 100000000L, System.currentTimeMillis());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                NetworkStats.Bucket trafficBucket = new NetworkStats.Bucket();
                if (trafficStatus.getNextBucket(trafficBucket)) {
                    long txBytes = trafficBucket.getTxBytes();
                    long txPackets = trafficBucket.getTxPackets();
                    Log.d("zsw11", "open:-->txBytes:" + txBytes);
                    Log.d("zsw11", "open:-->txPackets:" + txPackets);
                    //参考链接：https://www.cnblogs.com/DASOU/p/4205314.html
                }
                setOpen(true);
            }
        }
    }

    public void stopTrafficMonitor() {
        setOpen(false);
    }

    private String getAppUid(Context context) {
        String uid = "";
        PackageManager packageManager = context.getPackageManager();

        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            uid = String.valueOf(packageInfo.applicationInfo.uid);
        } catch (PackageManager.NameNotFoundException e) {
        }

        return uid;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean isOpen) {
        this.isOpen = isOpen;
    }
}
