package com.tdpark.utils;

public class StringUtils {

	public static boolean isBlank(String value){
		return value == null || value.trim().length() <= 0;
	}
	
	public static boolean isNotBlank(String value){
		return !isBlank(value);
	}
}
