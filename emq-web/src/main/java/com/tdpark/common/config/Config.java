package com.tdpark.common.config;

import java.util.Map;

public class Config {

    /** 线程基数，每个节点的线程数必须是这个基数的正整数倍 **/
    public static final int THREAD_BASE_NUM = 8;
    /** 节点编号 **/
    private int nodeIdx;
    /** 每个节点的线程数量 **/
    private int threadNum = THREAD_BASE_NUM;
    /** 所有节点的访问等地址 **/
    private Map<Integer, String> nodes;
    /** 是否支持url白名单 **/
    private boolean restrictHost = Boolean.TRUE;

    public int getNodeIdx() {
        return nodeIdx;
    }

    public void setNodeIdx(int nodeIdx) {
        this.nodeIdx = nodeIdx;
    }

    public int getNodeTotal() {
        return nodes.size();
    }

    public int getThreadNum() {
        return threadNum;
    }

    public void setThreadNum(int threadNum) {
        this.threadNum = threadNum;
    }

    public Map<Integer, String> getNodes() {
        return nodes;
    }

    public void setNodes(Map<Integer, String> nodes) {
        this.nodes = nodes;
    }

    public boolean isRestrictHost() {
        return restrictHost;
    }

    public void setRestrictHost(boolean restrictHost) {
        this.restrictHost = restrictHost;
    }

}
