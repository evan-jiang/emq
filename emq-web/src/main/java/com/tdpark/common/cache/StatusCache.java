package com.tdpark.common.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import com.tdpark.run.lock.LockContainer;

public class StatusCache {

    private static volatile AtomicBoolean PAUSE = new AtomicBoolean(
            Boolean.FALSE);
    private static final Map<Integer, Boolean> CONNECTION_STATUS = new ConcurrentHashMap<Integer, Boolean>();

    public static boolean pausing() {
        return PAUSE.get();
    }

    public static void slefPauseOrResume(boolean pause) {
        PAUSE.set(pause);
        LockContainer.notifyALL();
    }

    public static void connectionStatus(int nodeIdx, boolean status) {
        if(status || CONNECTION_STATUS.containsKey(nodeIdx)){
            CONNECTION_STATUS.put(nodeIdx, status);
        }
    }

    public static boolean connectionStatus(int nodeIdx) {
        return CONNECTION_STATUS.containsKey(nodeIdx)
                && CONNECTION_STATUS.get(nodeIdx);
    }

}
