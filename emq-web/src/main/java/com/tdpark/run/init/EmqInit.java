package com.tdpark.run.init;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.tdpark.common.cache.WhiteCache;
import com.tdpark.common.config.Config;
import com.tdpark.common.dal.EntityDAL;
import com.tdpark.run.execute.Executor;

public class EmqInit implements InitializingBean{

	@Autowired
	private EntityDAL entityDAL;
	@Autowired
	private Config config;
	@Autowired
	private WhiteCache whiteCache;
	@Override
	public void afterPropertiesSet() throws Exception {
		whiteCache.reload();
		for(int idx=0;idx<config.getThreadNum();idx++){
			new Thread(new Executor(idx, entityDAL)).start();
		}
	}

}