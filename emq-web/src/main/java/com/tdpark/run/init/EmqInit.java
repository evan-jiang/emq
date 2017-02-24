package com.tdpark.run.init;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.tdpark.common.config.Config;
import com.tdpark.common.config.WhiteCache;
import com.tdpark.common.domain.EntityBridge;
import com.tdpark.run.execute.Executor;

public class EmqInit implements InitializingBean{

	@Autowired
	private EntityBridge entityBridge;
	@Autowired
	private Config config;
	@Autowired
	private WhiteCache whiteCache;
	@Override
	public void afterPropertiesSet() throws Exception {
		whiteCache.reload();
		for(int idx=0;idx<config.getThreadNum();idx++){
			new Thread(new Executor(idx, entityBridge)).start();
		}
	}

}