package com.tdpark.execute;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.tdpark.domain.Entity;
import com.tdpark.domain.EntityBridge;
import com.tdpark.lock.LockContainer;
import com.tdpark.lock.SimpleLock;
import com.tdpark.utils.HttpClient;
import com.tdpark.utils.StringUtils;



public class Executor implements Runnable {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(Executor.class);
	private EntityBridge entityFactory;
	private SimpleLock simpleLock;
	public Executor(int threadNo,EntityBridge entityFactory) {
		this.entityFactory = entityFactory;
		simpleLock = LockContainer.instance(threadNo);
	}

	public void run() {
		while(true){
			process();
		}
	}
	
	private void process(){
		try {
			Entity entity = entityFactory.pop(simpleLock);//获取消息实体，并且重置锁的执行时间
			if(entity == null){//没有可执行的消息或者线程需要等待时则wait
				synchronized (simpleLock) {
					simpleLock.wait();
				}
				return;
			}
			long now = System.currentTimeMillis();
			if(now < entity.getNext_time()){
				synchronized (simpleLock) {
					simpleLock.wait(entity.getNext_time() - now + 5L);//等待至实体的计划执行时间
				}
			}
			if(System.currentTimeMillis() < entity.getNext_time()){//被唤醒的视为实体还没有到执行时间
				return;
			}
			
			if(todo(entity)){
				entityFactory.clean(entity);
				return;
			}
			again(entity);
		} catch (Exception e) {
			LOGGER.error("===>",e);
		}
	}
	
	private boolean todo(Entity entity){
		Map<String, String> map = new HashMap<String, String>();
		if(StringUtils.isNotBlank(entity.getParams())){
			map.put("params", entity.getParams());
		}
		String s = null;
		try {
			s = new HttpClient().post(entity.getUrl(), map , String.class);
			if(s.contains(entity.getMatch_value())){
				return true;
			}else{
				LOGGER.info("===>Fail! request:{}\n			response:{}",new Gson().toJson(entity),s);
			}
		} catch (Exception e) {
			LOGGER.error("===>",e);
		}
		return false;
	}
	
	private void again(Entity entity){
		if(entity.reset()){
			entityFactory.again(entity);
		}else{
			entityFactory.clean(entity);
		}
	}
}
