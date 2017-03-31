package com.tdpark.run.init;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;
import com.tdpark.common.cache.StatusCache;
import com.tdpark.common.cache.WhiteCache;
import com.tdpark.common.config.Config;
import com.tdpark.common.dal.EntityDAL;
import com.tdpark.eutils.HttpUtils;
import com.tdpark.run.execute.Executor;
import com.tdpark.vo.Result;

public class EmqInit implements InitializingBean {

    @Autowired
    private EntityDAL entityDAL;
    @Autowired
    private Config config;
    @Autowired
    private WhiteCache whiteCache;
    private static final String APPLY_JOIN_URI = "/emq/apply_join";
    private static final String APPLY_QUIT_URI = "/emq/apply_quit";

    public static final String NODE_IDX_KEY = "node_idx";
    public static final String NODE_HOST_KEY = "host";
    public static final String NODE_TOTAL_KEY = "node_total";
    public static final String THREAD_NUM_KEY = "thread_num";
    private static final Logger LOGGER = LoggerFactory.getLogger(EmqInit.class);

    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            checkConfig();
            loadWhite();
            applyJoin();
            applyQuit();
        } catch (RuntimeException e) {
            LOGGER.error("===>",e);
            System.exit(0);
        }
        for (int idx = 0; idx < config.getThreadNum(); idx++) {
            new Thread(new Executor(idx + ((config.getNodeIdx() - 1) * config.getThreadNum()), entityDAL)).start();
        }
    }

    private void checkConfig() {
        int nodeIdx = config.getNodeIdx();
        if (nodeIdx < 1) {
            throw new RuntimeException("Node index[" + nodeIdx
                    + "] illegality!");
        }
        Map<Integer, String> nodes = config.getNodes();
        if (nodes == null || nodes.isEmpty()) {
            throw new RuntimeException("Nodes illegality!");
        }
        int threadNum = config.getThreadNum();
        if (threadNum < 1) {
            throw new RuntimeException("Thread num[" + threadNum
                    + "] illegality!");
        }
    }

    private void loadWhite() {
        whiteCache.reload();
    }

    private void applyJoin() {
        int nodeIdx = config.getNodeIdx();
        StatusCache.connectionStatus(nodeIdx, true);
        Map<Integer, String> nodes = config.getNodes();
        for (Entry<Integer, String> e : nodes.entrySet()) {
            Integer idx = e.getKey();
            if (nodeIdx == idx) {
                continue;
            }
            String url = e.getValue() + APPLY_JOIN_URI;
            Map<String, Object> params = new HashMap<String, Object>();
            params.put(NODE_IDX_KEY, nodeIdx);
            params.put(NODE_HOST_KEY, nodes.get(nodeIdx));
            params.put(NODE_TOTAL_KEY, config.getNodeTotal());
            params.put(THREAD_NUM_KEY, config.getThreadNum());
            try {
                String doPost = HttpUtils.doPost(url, params);
                if (doPost.contains(Result.SUCCESS_CODE)) {
                    StatusCache.connectionStatus(idx, true);
                } else {
                    LOGGER.error("===>url : {},params : {}", url,
                            new Gson().toJson(params));
                    throw new RuntimeException(doPost);
                }
            } catch (IOException e1) {
                LOGGER.error("===>", e1);
            }
        }
    }
    
    private void applyQuit(){
        //该节点shutdown时通知其他节点“我已经停止运行，你们要是接到需要我执行的消息时只需要持久化消息，就不要再提醒我了”
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                int nodeIdx = config.getNodeIdx();
                Map<Integer, String> nodes = config.getNodes();
                StatusCache.connectionStatus(nodeIdx, false);
                for(Entry<Integer, String> e : nodes.entrySet()){
                    if(e.getKey() == nodeIdx){
                        continue;
                    }
                    String url = e.getValue() + APPLY_QUIT_URI;
                    Map<String, Object> params = new HashMap<String, Object>();
                    params.put(NODE_IDX_KEY, nodeIdx);
                    try {
                        HttpUtils.doPost(url, params);
                    } catch (IOException e1) {
                        LOGGER.error("===>", e1);
                    }
                }
            }
        }));
    }
}