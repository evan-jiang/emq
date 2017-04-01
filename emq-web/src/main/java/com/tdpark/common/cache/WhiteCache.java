package com.tdpark.common.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tdpark.common.channel.BrotherChannel;
import com.tdpark.eutils.StringUtils;
import com.tdpark.vo.Result;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

@Component
public class WhiteCache {

    @Autowired
    private ShardedJedisPool shardedJedisPool;
    @Autowired
    private BrotherChannel brotherChannel;
    private final Map<String, Boolean> WHITE_LIST_BY_HOST = new HashMap<String, Boolean>();
    private final String MQ_WHITE_KEY = "MQ_WHITE_KEY";

    /**
     * 初始化白名单
     */
    public void init() {
        ShardedJedis shardedJedis = shardedJedisPool.getResource();
        try {
            Set<String> set = shardedJedis.smembers(MQ_WHITE_KEY);
            WHITE_LIST_BY_HOST.clear();
            if (set != null) {
                for (String host : set) {
                    WHITE_LIST_BY_HOST.put(host, true);
                }
            }
        } finally {
            shardedJedisPool.returnResource(shardedJedis);
        }
    }
    /**
     * 加入白名单
     * @param host
     */
    public Result addHost(String host) {
        Result result = new Result();
        if(!StringUtils.isHost(host)){
            result.setCode(Result.FAILED_CODE);
            result.setMsg("Host illegal format![www.test.com] or [www.test.com.cn] or [10.244.12.52]");
            return result;
        }
        ShardedJedis shardedJedis = shardedJedisPool.getResource();
        try {
            shardedJedis.sadd(MQ_WHITE_KEY, host);
        } finally {
            shardedJedisPool.returnResource(shardedJedis);
        }
        init();
        brotherChannel.initWhite();
        return result;
    }
    /**
     * 移除白名单
     * @param host
     * @return
     */
    public Result removeHost(String host) {
        Result result = new Result();
        if(!StringUtils.isHost(host)){
            return result;
        }
        ShardedJedis shardedJedis = shardedJedisPool.getResource();
        try {
            shardedJedis.srem(MQ_WHITE_KEY, host);
        } finally {
            shardedJedisPool.returnResource(shardedJedis);
        }
        init();
        brotherChannel.initWhite();
        return result;
    }
    /**
     * 验证url是否在白名单内
     * @param url
     * @return
     */
    public boolean checkUrl(String url) {
        return WHITE_LIST_BY_HOST.containsKey(urlToHost(url));
    }
    /**
     * 从url中提取host
     * @param url
     * @return
     */
    private String urlToHost(String url) {
        String host = url.replace("http://", "").replace("https://", "");
        int idx = host.indexOf("/");
        if (idx > 0) {
            host = host.substring(0, idx);
        }
        idx = host.indexOf(":");
        if (idx > 0) {
            host = host.substring(0, idx);
        }
        return host;
    }
    public Set<String> whiteList(){
        Set<String> set = WHITE_LIST_BY_HOST.keySet();
        return set;
    }
}
