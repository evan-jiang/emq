package com.tdpark.common.dal;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.jedis.Tuple;

import com.google.gson.Gson;
import com.tdpark.common.cache.StatusCache;
import com.tdpark.common.domain.Entity;
import com.tdpark.eutils.StringUtils;
import com.tdpark.run.lock.SimpleLock;
@Component
public class EntityDAL {
    
    @Autowired
    private ShardedJedisPool shardedJedisPool;
    
    private static final String MQ_THREAD_NO_KEY = "MQ_THREAD_%s";
    private static final String MQ_ID_KEY = "MQ_ID_%s";
    private static final String MQ_SEQUENCE_KEY = "MQ_SEQUENCE";
    
    /**
     * 获取自增ID
     * @param shardedJedis
     * @return
     */
    private long sequence(ShardedJedis shardedJedis){
        return shardedJedis.incr(MQ_SEQUENCE_KEY);
    }
    /**
     * 消息首次被保存
     * @param entity
     */
    public void _push(Entity entity){
        ShardedJedis shardedJedis = shardedJedisPool.getResource();
        try {
            long id = sequence(shardedJedis);
            entity.setId(id);
            String mqId = String.format(MQ_ID_KEY, id);
            String threadKey = String.format(MQ_THREAD_NO_KEY, entity.getThread_no());
            shardedJedis.set(mqId, new Gson().toJson(entity));
            shardedJedis.zadd(threadKey, entity.getNext_time(), mqId);
        } finally {
            shardedJedisPool.returnResource(shardedJedis);
        }
        //LockContainer.notify(entity.getThread_no(), entity.getNext_time());
    }
    /**
     * 获取消息
     * @param simpleLock
     * @return
     */
    public Entity _pop(SimpleLock simpleLock){
        Entity entity = null;
        if (!StatusCache.pausing()) {
            ShardedJedis shardedJedis = shardedJedisPool.getResource();
            try {
                String threadKey = String.format(MQ_THREAD_NO_KEY, simpleLock.getThreadNo());
                Set<Tuple> set = shardedJedis.zrangeWithScores(threadKey, 0, 0);//获取最近消息的ID
                if(set != null && set.size() > 0){
                    String mqId = set.iterator().next().getElement();
                    String json = shardedJedis.get(mqId);
                    if(StringUtils.isNotBlank(json)){
                        entity = new Gson().fromJson(json, Entity.class);
                    }
                }
            } finally {
                shardedJedisPool.returnResource(shardedJedis);
            }
        }
        simpleLock.setExecuteTime(entity == null ? Long.MAX_VALUE : entity.getNext_time());
        return entity;
    }
    /**
     * 消息再次被保存
     * @param entity
     */
    public void _again(Entity entity) {
        ShardedJedis shardedJedis = shardedJedisPool.getResource();
        try {
            String threadKey = String.format(MQ_THREAD_NO_KEY, entity.getThread_no());
            String mqId = String.format(MQ_ID_KEY, entity.getId());
            shardedJedis.set(mqId, new Gson().toJson(entity));
            shardedJedis.zadd(threadKey, entity.getNext_time(), mqId);
        } finally {
            shardedJedisPool.returnResource(shardedJedis);
        }
    }
    /**
     * 清除消息
     * @param entity
     */
    public void _clean(Entity entity) {
        ShardedJedis shardedJedis = shardedJedisPool.getResource();
        try {
            String threadKey = String.format(MQ_THREAD_NO_KEY, entity.getThread_no());
            String mqId = String.format(MQ_ID_KEY, entity.getId());
            shardedJedis.zrem(threadKey, mqId);
            shardedJedis.del(mqId);
        } finally {
            shardedJedisPool.returnResource(shardedJedis);
        }
    }
}
