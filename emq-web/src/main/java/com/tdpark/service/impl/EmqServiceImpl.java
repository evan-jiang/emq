package com.tdpark.service.impl;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tdpark.common.cache.WhiteCache;
import com.tdpark.common.config.Config;
import com.tdpark.common.dal.EntityDAL;
import com.tdpark.common.domain.Entity;
import com.tdpark.eutils.StringUtils;
import com.tdpark.params.EmqParams;
import com.tdpark.service.EmqService;
import com.tdpark.vo.Result;

@Service("emqService")
public class EmqServiceImpl implements EmqService{

	@Autowired
	private Config emqConfig;
	@Autowired
	private EntityDAL entityDAL;
	@Autowired
	private WhiteCache whiteCache;
	@Override
	public Result make(EmqParams emqParams) {
		Result result = new Result();
		result.setCode("FAILED");
		String url = emqParams.getUrl();
		if(StringUtils.isBlank(url)){
			result.setMsg("[url] cant be empty!");
			return result;
		}
		if (emqConfig.isRestrictHost() && !whiteCache.checkUrl(url)) {
			result.setMsg("[url] Illegal format!");
			return result;
		}
		int plan_times = emqParams.getPlan_times();
		long interval = emqParams.getInterval();
		String match_value = emqParams.getMatch_value();
		if (plan_times <= 0) {
			plan_times = 1;
			interval = 5000;
		}
		if(plan_times > 1 && interval < 5000){
			result
			.setMsg("[interval] Must be greater than 5000!");
			return result;
		}
		if(StringUtils.isBlank(match_value)){
			result
			.setMsg("[match_value] cant be empty!");
			return result;
		}
		
		String title = emqParams.getTitle();
		if(StringUtils.isBlank(title)){
			result
			.setMsg("[title] cant be empty!");
			return result;
		}
		long delay = emqParams.getDelay();
		if(delay <= 0){
			delay = 0;
		}
		String params = emqParams.getParams();
		boolean block = emqParams.isBlock();
		int hash = 0;
		if(block){
			if(StringUtils.isNotBlank(emqParams.getGroup())){
				hash = emqParams.getGroup().hashCode();
			}else if(StringUtils.isNotBlank(params)){
				hash = params.hashCode();
			}else{
				hash = url.hashCode();
			}
		}else{
			hash = new Random().nextInt(Integer.MAX_VALUE);
		}
		int thread_no = Math.abs(hash % (emqConfig.getNodeTotal() * emqConfig.getThreadNum()));
		Entity entity = new Entity();
		entity.setInterval(interval);
		entity.setMatch_value(match_value);
		entity.setNext_time(System.currentTimeMillis() + delay);
		entity.setParams(params);
		entity.setPlan_times(plan_times);
		entity.setThread_no(thread_no);
		entity.setTitle(title);
		entity.setUrl(url);
		entityDAL._push(entity);
		result.setCode(Result.SUCCESS_CODE);
		return result;
	}

}
