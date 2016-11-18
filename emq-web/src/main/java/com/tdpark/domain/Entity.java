package com.tdpark.domain;
public class Entity {
    /*** 主键 ***/
    private long id;
    /*** 标题 ***/
    private String title;
    /*** 执行url ***/
    private String url;
    /*** 执行参数 ***/
    private String params;
    /*** 线程编号 ***/
    private int thread_no;
    /*** 执行结果匹配值 ***/
    private String match_value;
    /*** 允许执行次数 ***/
    private int plan_times;
    /*** 已经执行次数 ***/
    private int yet_times;
    /*** 重复执行的时间间隔(毫秒) ***/
    private long interval;
    /*** 下次执行时间(毫秒) ***/
    private long next_time;
    /*** 主键 ***/
    public long getId(){
        return this.id;
    }
    /*** 主键 ***/
    public void setId(long id){
        this.id = id;
    }
    /*** 标题 ***/
    public String getTitle(){
        return this.title;
    }
    /*** 标题 ***/
    public void setTitle(String title){
        this.title = title;
    }
    /*** 执行url ***/
    public String getUrl(){
        return this.url;
    }
    /*** 执行url ***/
    public void setUrl(String url){
        this.url = url;
    }
    /*** 执行参数 ***/
    public String getParams(){
        return this.params;
    }
    /*** 执行参数 ***/
    public void setParams(String params){
        this.params = params;
    }
    /*** 线程编号 ***/
    public int getThread_no(){
        return this.thread_no;
    }
    /*** 线程编号 ***/
    public void setThread_no(int thread_no){
        this.thread_no = thread_no;
    }
    /*** 执行结果匹配值 ***/
    public String getMatch_value(){
        return this.match_value;
    }
    /*** 执行结果匹配值 ***/
    public void setMatch_value(String match_value){
        this.match_value = match_value;
    }
    /*** 允许执行次数 ***/
    public int getPlan_times(){
        return this.plan_times;
    }
    /*** 允许执行次数 ***/
    public void setPlan_times(int plan_times){
        this.plan_times = plan_times;
    }
    /*** 已经执行次数 ***/
    public int getYet_times(){
        return this.yet_times;
    }
    /*** 已经执行次数 ***/
    public void setYet_times(int yet_times){
        this.yet_times = yet_times;
    }
    /*** 重复执行的时间间隔(毫秒) ***/
    public long getInterval(){
        return this.interval;
    }
    /*** 重复执行的时间间隔(毫秒) ***/
    public void setInterval(long interval){
        this.interval = interval;
    }
    /*** 下次执行时间(毫秒) ***/
    public long getNext_time(){
        return this.next_time;
    }
    /*** 下次执行时间(毫秒) ***/
    public void setNext_time(long next_time){
        this.next_time = next_time;
    }
    public boolean reset(){
		if(this.plan_times > this.yet_times){
			this.yet_times += 1;
			this.next_time = System.currentTimeMillis() + this.interval;
			return true;
		}
		return false;
	}
}