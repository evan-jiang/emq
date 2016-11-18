package com.tdpark.lock;

import java.util.concurrent.atomic.AtomicBoolean;

import com.tdpark.hold.Hold;

public class SimpleLock {

	private final int threadNo;
	private long executeTime;
	private AtomicBoolean waiting = Hold.WAITING;
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
	public AtomicBoolean getWaiting() {
		return waiting;
	}
}
