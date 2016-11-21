package com.tdpark.init;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.tdpark.config.Config;
import com.tdpark.config.WhiteCache;
import com.tdpark.domain.EntityBridge;
import com.tdpark.execute.Executor;

public class EmqInit implements InitializingBean{

	@Autowired
	private EntityBridge entityFactory;
	@Autowired
	private Config config;
	@Autowired
	private WhiteCache whiteCache;
	@Override
	public void afterPropertiesSet() throws Exception {
		whiteCache.reload();
		for(int idx=0;idx<config.getThreadNum();idx++){
			new Thread(new Executor(idx, entityFactory)).start();
		}
	}

}