package com.tdpark.utils;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {
	private String inStr;
	private MessageDigest md5;

	/* 下面是构造函数 */
	public MD5(String inStr) {
		this.inStr = inStr;
		try {
			this.md5 = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			System.out.println(e.toString());
			e.printStackTrace();
		}
	}

	/* 下面是关键的md5算法 */
	public String compute() {

		char[] charArray = this.inStr.toCharArray();

		byte[] byteArray = new byte[charArray.length];

		for (int i = 0; i < charArray.length; i++)
			byteArray[i] = (byte) charArray[i];

		byte[] md5Bytes = this.md5.digest(byteArray);

		StringBuffer hexValue = new StringBuffer();

		for (int i = 0; i < md5Bytes.length; i++) {
			int val = ((int) md5Bytes[i]) & 0xff;
			if (val < 16)
				hexValue.append("0");
			hexValue.append(Integer.toHexString(val));
		}

		return hexValue.toString();
	}
	
	public static String toMD5(String s){
		MD5 md5 = new MD5(s);
		return md5.compute();
	}

	/* 下面是主函数调用 */
	public static void main(String[] args) {
		
		System.out.println("加密后的数据:" + MD5.toMD5("238889230"+"12345678"+"100.00"+"kksd%sj*77"));
	}
}
