package com.example.performdemo.core;

import android.view.Choreographer;

import com.example.performdemo.utils.ReflectHelperUtil;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class LazyChoreographerFrameUpdateMonitor {
    private final int CALLBACK_INPUT = 0;
    private final int CALLBACK_ANIMATION = 1;
    private final int CALLBACK_TRAVERSAL = 2;
    private final int CALLBACK_COMMIT = 3;
    private Method addTraversalQueue;
    private Method addInputQueue;
    private Method addAnimationQueue;
    private Method commitQueque;
    private Choreographer choreographer;
    private Object[] callbackQueues;
    private Object callbackQueueLock;
    private final String METHOD_ADD_CALLBACK = "addCallbackLocked";
    private final ArrayList<FrameUpdateListener> frameListeners = new ArrayList<>();
    private long inputEventCostTimeNs;
    private long animationEventCostTimeNs;
    private long traversalEventCostTimeNs;
    private boolean actualExecuteDoFrame;
    private boolean insertMonitorRunnable;
    private long oneFrameStartTime;
    private long oneFrameEndTime;
    private boolean startMonitorDoFrame;
    private UIThreadLooperMonitor mainThreadLooperMonitor = new UIThreadLooperMonitor();
    private UIThreadLooperMonitor.LooperHandleEventListener mLoopListener = new UIThreadLooperMonitor.LooperHandleEventListener() {
        @Override
        public void onMessageLooperStartHandleMessage() {
            startMonitorChoreographerDoFrame();
        }

        @Override
        public void onMessageLooperStopHandleMessage() {
            endMonitorChoreographerDoFrame();
        }
    };

    public LazyChoreographerFrameUpdateMonitor() {
        init();
    }


    public void init() {
        choreographer = Choreographer.getInstance();
        callbackQueueLock = ReflectHelperUtil.getInstance().reflectField(choreographer, "mLock");
        callbackQueues = (Object[]) ReflectHelperUtil.getInstance().reflectField(choreographer, "mCallbackQueues");
        if (callbackQueues != null) {
            addInputQueue = ReflectHelperUtil.getInstance().reflectMethod(callbackQueues[CALLBACK_INPUT],
                    METHOD_ADD_CALLBACK, new Class[]{Long.TYPE, Object.class, Object.class});


            addAnimationQueue = ReflectHelperUtil.getInstance().reflectMethod(callbackQueues[CALLBACK_ANIMATION],
                    METHOD_ADD_CALLBACK, new Class[]{Long.TYPE, Object.class, Object.class});


            addTraversalQueue = ReflectHelperUtil.getInstance().reflectMethod(callbackQueues[CALLBACK_TRAVERSAL],
                    METHOD_ADD_CALLBACK, new Class[]{Long.TYPE, Object.class, Object.class});


            commitQueque = ReflectHelperUtil.getInstance().reflectMethod(callbackQueues[CALLBACK_TRAVERSAL],
                    METHOD_ADD_CALLBACK, new Class[]{Long.TYPE, Object.class, Object.class});
        }

    }

    public void startMonitor() {
        mainThreadLooperMonitor.setEnable(true);
        mainThreadLooperMonitor.addLooperHandleEventListener(mLoopListener);
    }

    public void stopMonitor() {
        mainThreadLooperMonitor.setEnable(false);
        mainThreadLooperMonitor.removeLooperHandleEventListener(mLoopListener);
    }

    public void startMonitorChoreographerDoFrame() {
        startMonitorDoFrame = true;
        actualExecuteDoFrame = false;
        inputEventCostTimeNs = 0L;
        animationEventCostTimeNs = 0L;
        traversalEventCostTimeNs = 0L;
        oneFrameStartTime = System.nanoTime();
        insertCallbackToInputQueue();
    }

    public final void endMonitorChoreographerDoFrame() {
        if (startMonitorDoFrame && actualExecuteDoFrame) {
            startMonitorDoFrame = false;
            oneFrameEndTime = System.nanoTime();
            traversalEventCostTimeNs = System.nanoTime() - traversalEventCostTimeNs;
            long oneFrameCostNs = oneFrameEndTime - oneFrameStartTime;
            long inputCost = inputEventCostTimeNs;
            long animationCost = animationEventCostTimeNs;
            long traversalCost = traversalEventCostTimeNs;
            for (FrameUpdateListener listener : frameListeners) {
                listener.doFrame(oneFrameCostNs, inputCost, animationCost, traversalCost);
            }

        }
    }

    private void insertCallbackToInputQueue() {
        if (!insertMonitorRunnable) {
            insertMonitorRunnable = true;
            addCallbackToQueue(CALLBACK_INPUT, new Runnable() {
                public final void run() {
                    insertMonitorRunnable = false;
                    actualExecuteDoFrame = true;
                    inputEventCostTimeNs = System.nanoTime();
                }
            }, false);
            addCallbackToQueue(CALLBACK_ANIMATION, new Runnable() {
                public final void run() {
                    inputEventCostTimeNs = System.nanoTime() - inputEventCostTimeNs;
                    animationEventCostTimeNs = System.nanoTime();
                }
            }, false);
            addCallbackToQueue(CALLBACK_TRAVERSAL, new Runnable() {
                public final void run() {
                    animationEventCostTimeNs = System.nanoTime() - animationEventCostTimeNs;
                    traversalEventCostTimeNs = System.nanoTime();
                }
            }, false);
        }
    }

    private void addCallbackToQueue(int type, Runnable callback, boolean isHead) {
        if (callbackQueueLock != null && callbackQueues != null) {
            try {
                synchronized (callbackQueueLock) {
                    Method method = null;
                    if (type == CALLBACK_INPUT) {
                        method = addInputQueue;
                    } else if (type == CALLBACK_ANIMATION) {
                        method = addAnimationQueue;
                    } else if (type == CALLBACK_TRAVERSAL) {
                        method = addTraversalQueue;
                    } else if (type == CALLBACK_COMMIT) {
                        method = commitQueque;
                    }
                    if (method != null) {
                        method.invoke(callbackQueues[type], -1, callback, null);
                    }
                }
            } catch (Exception e) {

            }

        }
    }


    public void addCallbackToCommitQueue(@NotNull Runnable callback) {
        addCallbackToQueue(CALLBACK_COMMIT, callback, false);
    }

    public void addFrameUpdateListener(@NotNull FrameUpdateListener listener) {
        frameListeners.add(listener);
    }

    public void removeFrameUpdateListener(@NotNull FrameUpdateListener listener) {
        frameListeners.remove(listener);
    }

    public final int getCurrentListenerSize() {
        return frameListeners.size();
    }

    public interface FrameUpdateListener {
        void doFrame(long frameCostNs, long inputCostNs, long animationCostNs, long traversalCostNs);
    }
}
