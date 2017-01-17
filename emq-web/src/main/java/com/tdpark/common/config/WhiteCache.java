package com.tdpark.common.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tdpark.common.domain.White;
import com.tdpark.dao.WhiteDao;

@Component
public class WhiteCache {

	@Autowired
	private WhiteDao whiteDao;
	private final Map<String, White> WHITE_LIST_BY_HOST = new HashMap<String, White>();
	private final Map<String, White> WHITE_LIST_BY_APP  = new HashMap<String, White>();
	
	public void reload(){
		List<White> list = whiteDao.select();
		WHITE_LIST_BY_HOST.clear();
		WHITE_LIST_BY_APP.clear();
		if(list != null){
			for(White w : list){
				String host = urlToHost(w.getDomain());
				WHITE_LIST_BY_HOST.put(host, w);
				WHITE_LIST_BY_APP.put(w.getApp_id(), w);
			}
		}
	}
	
	public White getByUrl(String url){
		return WHITE_LIST_BY_HOST.get(urlToHost(url));
	}
	
	public White getByApp(String appId){
		return WHITE_LIST_BY_APP.get(appId);
	}
	
	public boolean checkUrl(String url){
		return WHITE_LIST_BY_HOST.containsKey(urlToHost(url));
	}
	private String urlToHost(String url){
		String host = url.replace("http://", "").replace("https://", "");
		int idx = host.indexOf("/");
		if(idx > 0){
			return host.substring(0,idx);
		}
		return host;
	}
}
