package com.example.performdemo.core;

import com.example.performdemo.anr.FrameUpdateLister;

public class ChoreographerMonitorCenter {
    private static MTChoreographerFrameMonitor mMTChoreographerFrameMonitor = new MTChoreographerFrameMonitor();
    private static LazyChoreographerFrameUpdateMonitor detailedFrameUpdateMonitor = new LazyChoreographerFrameUpdateMonitor();



    public static void addSimpleFrameUpdateListener(FrameUpdateLister listener) {
        if (mMTChoreographerFrameMonitor.getCurrentListenerSize() == 0) {
            mMTChoreographerFrameMonitor.startUpdateFrameMonitor();
        }
        mMTChoreographerFrameMonitor.addFrameUpdateListener(listener);
    }

    public static void removeSimpleFrameUpdateListener(FrameUpdateLister listener) {
        mMTChoreographerFrameMonitor.removeFrameUpdateListener(listener);
        if (mMTChoreographerFrameMonitor.getCurrentListenerSize() == 0) {
            mMTChoreographerFrameMonitor.stopUpdateFrameMonitor();
        }
    }

    public static void addDetailedFrameUpdateListener(LazyChoreographerFrameUpdateMonitor.FrameUpdateListener listener) {
        if (detailedFrameUpdateMonitor.getCurrentListenerSize() == 0) {
            detailedFrameUpdateMonitor.startMonitor();
        }
        detailedFrameUpdateMonitor.addFrameUpdateListener(listener);
    }

    public static void removeDetailedFrameUpdateListener(LazyChoreographerFrameUpdateMonitor.FrameUpdateListener listener) {
        detailedFrameUpdateMonitor.removeFrameUpdateListener(listener);
        if (detailedFrameUpdateMonitor.getCurrentListenerSize() == 0) {
            detailedFrameUpdateMonitor.stopMonitor();
        }
    }

}
