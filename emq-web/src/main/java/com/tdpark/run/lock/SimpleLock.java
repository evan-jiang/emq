package com.tdpark.run.lock;


public class SimpleLock {

    private final int threadNo;
    private long executeTime;

    public SimpleLock(int threadNo) {
        this.threadNo = threadNo;
        this.executeTime = System.currentTimeMillis();
    }

    public int getThreadNo() {
        return threadNo;
    }

    public long getExecuteTime() {
        return executeTime;
    }

    public void setExecuteTime(long executeTime) {
        this.executeTime = executeTime;
    }

}
