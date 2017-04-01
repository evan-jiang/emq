package com.tdpark.run.lock;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.tdpark.common.cache.StatusCache;

public class LockContainer {

    private static final Map<Integer, SimpleLock> LOCKES = new HashMap<Integer, SimpleLock>();

    /**
     * 创建锁
     * @param threadNo
     * @return
     */
    public static SimpleLock instance(int threadNo) {
        if (!LOCKES.containsKey(threadNo)) {
            SimpleLock simpleLock = new SimpleLock(threadNo);
            simpleLock.setExecuteTime(Long.MAX_VALUE);
            LOCKES.put(threadNo, simpleLock);
            return simpleLock;
        }
        return LOCKES.get(threadNo);
    }
    /**
     * 尝试唤醒该节点下的某个线程
     * @param threadNo
     * @param newExecuteTime
     */
    public static void tryNotify(int threadNo, long newExecuteTime) {
        if (LOCKES.containsKey(threadNo)) {
            SimpleLock simpleLock = LOCKES.get(threadNo);
            if (simpleLock.getExecuteTime() > newExecuteTime
                    && !StatusCache.pausing()) {
                synchronized (simpleLock) {
                    simpleLock.notify();
                }
            }
        }
    }
    /**
     * 唤醒该节点下的所有线程
     */
    public static void notifyALL() {
        for (Entry<Integer, SimpleLock> e : LOCKES.entrySet()) {
            SimpleLock simpleLock = e.getValue();
            synchronized (simpleLock) {
                simpleLock.notify();
            }
        }
    }
}
