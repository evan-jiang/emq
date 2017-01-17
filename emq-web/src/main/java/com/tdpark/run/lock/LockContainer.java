package com.tdpark.run.lock;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class LockContainer {

	private static final Map<Integer, SimpleLock> LOCKES = new HashMap<Integer, SimpleLock>();
	
	public static SimpleLock instance(int threadNo){
		if(!LOCKES.containsKey(threadNo)){
			SimpleLock simpleLock = new SimpleLock(threadNo);
			simpleLock.setExecuteTime(Long.MAX_VALUE);
			LOCKES.put(threadNo, simpleLock);
			return simpleLock;
		}
		return LOCKES.get(threadNo);
	}
	
	public static void notify(int threadNo,long newExecuteTime){
		if(LOCKES.containsKey(threadNo)){
			SimpleLock simpleLock = LOCKES.get(threadNo);
			if(simpleLock.getExecuteTime() > newExecuteTime && !simpleLock.getWaiting().get()){
				synchronized(simpleLock){
					simpleLock.notify();
				}
			}
		}
	}
	
	public static void notifyALL(){
		for(Entry<Integer, SimpleLock> e : LOCKES.entrySet()){
			SimpleLock simpleLock = e.getValue();
			synchronized (simpleLock) {
				simpleLock.notify();
			}
		}
	}
}
