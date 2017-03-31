package com.tdpark.common.channel;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tdpark.common.cache.StatusCache;
import com.tdpark.common.config.Config;
import com.tdpark.eutils.HttpUtils;
import com.tdpark.run.lock.LockContainer;
import com.tdpark.vo.Result;

@Component
public class BrotherChannel {

    @Autowired
    private Config config;
    private static final String SINGLE_PAUSE_URI = "/emq/pause";
    private static final String SINGLE_RESUME_URI = "/emq/resume";
    private static final String THREAD_NOTIFY_URI = "/emq/notify";
    public static final String THREAD_KEY = "thread_no";
    public static final String EXECUTE_TIME_KEY = "new_execute_time";
    private static final Logger LOGGER = LoggerFactory
            .getLogger(BrotherChannel.class);
    public void fullPause() {
        fullPauseOrResume(true);
    }
    public void fullResume() {
        fullPauseOrResume(false);
    }
    private void fullPauseOrResume(boolean pause){
        int nodeIdx = config.getNodeIdx();
        StatusCache.slefPauseOrResume(pause);
        Map<Integer, String> nodes = config.getNodes();
        for (Entry<Integer, String> e : nodes.entrySet()) {
            Integer idx = e.getKey();
            if (nodeIdx == idx) {
                continue;
            }
            if (!StatusCache.connectionStatus(idx)) {
                continue;
            }
            String url = e.getValue() + (pause ? SINGLE_PAUSE_URI : SINGLE_RESUME_URI);
            try {
                HttpUtils.doPost(url, null);
            } catch (IOException e1) {
                LOGGER.error("===>",e1);
            }
        }
    }

    public void notifyThread(int threadNo,long newExecuteTime){
        int idx = (threadNo / config.getThreadNum()) + 1;//根据线程编号计算节点编号
        int nodeIdx = config.getNodeIdx();
        if(idx == nodeIdx){
            LockContainer.notify(threadNo, newExecuteTime);
            return;
        }
        Map<Integer, String> nodes = config.getNodes();
        if(!nodes.containsKey(idx)){
            LOGGER.info("Node[{}] does not exist!",idx);
            return;
        }
        if (!StatusCache.connectionStatus(idx)) {
            LOGGER.info("Node[{}] connection exception!",idx);
            return;
        }
        String url = nodes.get(idx) + THREAD_NOTIFY_URI;
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(THREAD_KEY, threadNo);
        params.put(EXECUTE_TIME_KEY, newExecuteTime);
        try {
            HttpUtils.doPost(url, params);
        } catch (IOException e1) {
            LOGGER.error("===>",e1);
        }
    }
    
    public Result applyJoin(int nodeIdx,String nodeHost,int nodeTotal,int threadNum){
        Result result = new Result();
        Map<Integer, String> nodes = config.getNodes();
        if(nodes == null || !nodes.containsKey(nodeIdx)){
            result.setCode(Result.FAILED_CODE);
            result.setMsg("Node index["+config.getNodeIdx()+"] error!");
            return result;
        }
        if(!nodes.get(nodeIdx).equals(nodeHost)){
            result.setCode(Result.FAILED_CODE);
            result.setMsg("Node host["+nodes.get(nodeIdx)+"] error!");
            return result;
        }
        if(config.getNodeTotal() != nodeTotal){
            result.setCode(Result.FAILED_CODE);
            result.setMsg("Node total["+config.getNodeTotal()+"] error!");
            return result;
        }
        if(config.getThreadNum() != threadNum){
            result.setCode(Result.FAILED_CODE);
            result.setMsg("Thread num["+config.getThreadNum()+"] error!");
            return result;
        }
        StatusCache.connectionStatus(nodeIdx, true);
        return result;
    }
    
    public Result applyQuit(int nodeIdx){
        Result result = new Result();
        StatusCache.connectionStatus(nodeIdx, false);
        return result;
    }
}
