package com.tdpark.common.config;

import java.util.Map;

public class Config {
	
    /** 每个节点的默认线程数  **/
    public static final int DEF_THREAD_NUM = 8;
	private int nodeIdx;
	private int threadNum = DEF_THREAD_NUM;
	private Map<Integer, String> nodes;
	private boolean restrictHost = Boolean.FALSE;
	
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
