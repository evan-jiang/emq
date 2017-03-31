package com.tdpark.common.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

@Component
public class WhiteCache {

    @Autowired
    private ShardedJedisPool shardedJedisPool;
	private final Map<String, Boolean> WHITE_LIST_BY_HOST = new HashMap<String, Boolean>();
	private final String MQ_WHITE_KEY = "MQ_WHITE_KEY";
	
	public void reload(){
		ShardedJedis shardedJedis = shardedJedisPool.getResource();
		try {
		    Set<String> set = shardedJedis.smembers(MQ_WHITE_KEY);
		    WHITE_LIST_BY_HOST.clear();
	        if(set != null){
                for(String host : set){
	                WHITE_LIST_BY_HOST.put(host, true);
	            }
	        }
        } finally {
            shardedJedisPool.returnResource(shardedJedis);
        }
	}
	
	public void addHost(String host){
	    ShardedJedis shardedJedis = shardedJedisPool.getResource();
        try {
            host = urlToHost(host);
            shardedJedis.sadd(MQ_WHITE_KEY, host);
            WHITE_LIST_BY_HOST.put(host, true);
        } finally {
            shardedJedisPool.returnResource(shardedJedis);
        }
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
