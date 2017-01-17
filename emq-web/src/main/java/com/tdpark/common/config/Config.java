package com.tdpark.common.config;

import java.util.Map;

public class Config {
	
	private int nodeIdx;
	private int nodeTotal;
	private int threadNum;
	private Map<Integer, String> nodes;
	private boolean restrictHost = Boolean.FALSE;
	
	public int getNodeIdx() {
		return nodeIdx;
	}
	public void setNodeIdx(int nodeIdx) {
		this.nodeIdx = nodeIdx;
	}
	public int getNodeTotal() {
		return nodeTotal;
	}
	public void setNodeTotal(int nodeTotal) {
		this.nodeTotal = nodeTotal;
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
