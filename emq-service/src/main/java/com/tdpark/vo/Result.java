package com.tdpark.vo;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Result implements Serializable{
	public static final String SUCCESS_CODE = "SUCCESS";
	public static final String FAILED_CODE = "FAILED";
	private String code = SUCCESS_CODE;
	private String msg;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public boolean success(){
		return SUCCESS_CODE.equals(this.code);
	}
}
