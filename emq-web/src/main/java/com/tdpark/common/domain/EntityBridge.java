package com.tdpark.common.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tdpark.dao.MsgDao;
import com.tdpark.run.lock.LockContainer;
import com.tdpark.run.lock.SimpleLock;

@Component
public class EntityBridge {

	@Autowired
	private MsgDao msgDao;
	
	public void push(Entity entity){
		msgDao.insert(entity);
		LockContainer.notify(entity.getThread_no(), entity.getNext_time());
	}
	public Entity pop(SimpleLock simpleLock){
		Entity entity = null;
		if(!simpleLock.getWaiting().get()){
			entity = msgDao.pop(simpleLock.getThreadNo());
		}
		simpleLock.setExecuteTime(entity == null ? Long.MAX_VALUE : entity.getNext_time());
		return entity;
	}
	
	public void again(Entity entity){
		msgDao.again(entity);
	}
	
	public void clean(Entity entity){
		msgDao.clean(entity);
	}
}
