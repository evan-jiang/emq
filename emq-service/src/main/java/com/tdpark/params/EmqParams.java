package com.tdpark.params;

import java.io.Serializable;

@SuppressWarnings("serial")
public class EmqParams implements Serializable {

	/*** 标题 ***/
	private String title;
	/*** 执行url ***/
	private String url;
	/*** 执行参数 ***/
	private String params;
	/*** 执行结果匹配值 ***/
	private String match_value;
	/*** 允许执行次数 ***/
	private int plan_times;
	/*** 重复执行的时间间隔(毫秒) ***/
	private long interval;
	/*** 下次执行时间(毫秒) ***/
	private long delay;
	/*** 是否阻塞的(线程安全)  ***/
	private boolean block;
	private String group;


	/*** 标题 ***/
	public String getTitle() {
		return this.title;
	}

	/*** 标题 ***/
	public void setTitle(String title) {
		this.title = title;
	}

	/*** 执行url ***/
	public String getUrl() {
		return this.url;
	}

	/*** 执行url ***/
	public void setUrl(String url) {
		this.url = url;
	}

	/*** 执行参数 ***/
	public String getParams() {
		return this.params;
	}

	/*** 执行参数 ***/
	public void setParams(String params) {
		this.params = params;
	}

	/*** 执行结果匹配值 ***/
	public String getMatch_value() {
		return this.match_value;
	}

	/*** 执行结果匹配值 ***/
	public void setMatch_value(String match_value) {
		this.match_value = match_value;
	}

	/*** 允许执行次数 ***/
	public int getPlan_times() {
		return this.plan_times;
	}

	/*** 允许执行次数 ***/
	public void setPlan_times(int plan_times) {
		this.plan_times = plan_times;
	}

	/*** 重复执行的时间间隔(毫秒) ***/
	public long getInterval() {
		return this.interval;
	}

	/*** 重复执行的时间间隔(毫秒) ***/
	public void setInterval(long interval) {
		this.interval = interval;
	}

	public long getDelay() {
		return delay;
	}

	public void setDelay(long delay) {
		this.delay = delay;
	}

	public boolean isBlock() {
		return block;
	}

	public void setBlock(boolean block) {
		this.block = block;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}
	
}
