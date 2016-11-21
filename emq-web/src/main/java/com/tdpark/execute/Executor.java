package com.tdpark.execute;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.tdpark.domain.Entity;
import com.tdpark.domain.EntityBridge;
import com.tdpark.lock.LockContainer;
import com.tdpark.lock.SimpleLock;



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
			Entity entity = entityFactory.pop(simpleLock);//��ȡ��Ϣʵ�壬�����������ִ��ʱ��
			if(entity == null){//û�п�ִ�е���Ϣ�����߳���Ҫ�ȴ�ʱ��wait
				synchronized (simpleLock) {
					simpleLock.wait();
				}
				return;
			}
			long now = System.currentTimeMillis();
			if(now < entity.getNext_time()){
				synchronized (simpleLock) {
					simpleLock.wait(entity.getNext_time() - now + 5L);//�ȴ���ʵ��ļƻ�ִ��ʱ��
				}
			}
			if(System.currentTimeMillis() < entity.getNext_time()){//�����ѵ���Ϊʵ�廹û�е�ִ��ʱ��
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
		LOGGER.info("===>{}",new Gson().toJson(entity));
		return true;
	}
	
	private void again(Entity entity){
		if(entity.reset()){
			entityFactory.again(entity);
		}else{
			entityFactory.clean(entity);
		}
	}
}
