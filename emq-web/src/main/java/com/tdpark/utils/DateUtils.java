package com.tdpark.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class DateUtils {

	private static final String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	@SuppressWarnings("unchecked")
	private static <T> T assign(Date date,Class<T> clazz){
		if(clazz != String.class && clazz != long.class && clazz != Long.class && clazz != Date.class){
			throw new RuntimeException("Only support java.lang.Long or java.lang.String or java.util.Date");
		}
		if(clazz == String.class){
			return (T) new SimpleDateFormat(DEFAULT_FORMAT).format(date);
		}else if(clazz == long.class || clazz == Long.class){
			return (T) Long.valueOf(date.getTime());
		}else{
			return (T) date;
		}
	}
	@SuppressWarnings("unchecked")
	private static <T> T assign(String format,Date date,Class<T> clazz){
		if(clazz != String.class && clazz != long.class && clazz != Long.class && clazz != Date.class){
			throw new RuntimeException("Only support java.lang.Long or java.lang.String or java.util.Date");
		}
		if(clazz == String.class){
			return (T) new SimpleDateFormat(format).format(date);
		}else if(clazz == long.class || clazz == Long.class){
			return (T) Long.valueOf(date.getTime());
		}else{
			return (T) date;
		}
	}
	public static <T> T assign(int day,Class<T> clazz){
		Date date = new Date(System.currentTimeMillis() + (day * 24 * 3600) * 1000L);
		return assign(date, clazz);
	}
	public static <T> T assign(int day,int hour,Class<T> clazz){
		Date date = new Date(System.currentTimeMillis() + (day * 24 * 3600 + hour * 3600) * 1000L);
		return assign(date, clazz);
	}
	public static <T> T assign(int day,int hour,int minute,Class<T> clazz){
		Date date = new Date(System.currentTimeMillis() + (day * 24 * 3600 + hour * 3600 + minute * 60) * 1000L);
		return assign(date, clazz);
	}
	public static <T> T assign(int day,int hour,int minute,int second,Class<T> clazz){
		Date date = new Date(System.currentTimeMillis() + (day * 24 * 3600 + hour * 3600 + minute * 60 + second) * 1000L);
		return assign(date, clazz);
	}
	
	public static <T> T assign(String format,int day,Class<T> clazz){
		Date date = new Date(System.currentTimeMillis() + (day * 24 * 3600) * 1000L);
		return assign(format,date, clazz);
	}
	public static <T> T assign(String format,int day,int hour,Class<T> clazz){
		Date date = new Date(System.currentTimeMillis() + (day * 24 * 3600 + hour * 3600) * 1000L);
		return assign(format,date, clazz);
	}
	public static <T> T assign(String format,int day,int hour,int minute,Class<T> clazz){
		Date date = new Date(System.currentTimeMillis() + (day * 24 * 3600 + hour * 3600 + minute * 60) * 1000L);
		return assign(format,date, clazz);
	}
	public static <T> T assign(String format,int day,int hour,int minute,int second,Class<T> clazz){
		Date date = new Date(System.currentTimeMillis() + (day * 24 * 3600 + hour * 3600 + minute * 60 + second) * 1000L);
		return assign(format,date, clazz);
	}
	public static <T> T now(Class<T> clazz){
		Date date = new Date();
		return assign(date, clazz);
	}
	public static <T> T now(String format,Class<T> clazz){
		Date date = new Date();
		return assign(format,date, clazz);
	}
	public static <T> T any(Date date,Class<T> clazz){
		return assign(date, clazz);
	}
	public static <T> T any(String format,Date date,Class<T> clazz){
		return assign(format,date, clazz);
	}
	
	public static void main(String[] args) throws ParseException {
		//System.out.println(now(String.class));
		//System.out.println(now(long.class));
		//System.out.println(assign("yyyy-MM-dd HH:mm:ss",1, String.class));
		
		
		/*System.out.println(new SimpleDateFormat(DEFAULT_FORMAT).parse("2016-09-29 00:00:00").getTime() + "L");
		System.out.println(new SimpleDateFormat(DEFAULT_FORMAT).parse("2016-09-30 00:00:00").getTime() + "L");
		System.out.println(new SimpleDateFormat(DEFAULT_FORMAT).parse("2016-09-29 00:00:00").getTime() + "L");
		System.out.println(new SimpleDateFormat(DEFAULT_FORMAT).parse("2016-10-01 00:00:00").getTime() + "L");
		System.out.println(new SimpleDateFormat(DEFAULT_FORMAT).parse("2016-10-02 00:00:00").getTime() + "L");
		System.out.println(new SimpleDateFormat(DEFAULT_FORMAT).parse("2016-10-03 00:00:00").getTime() + "L");
		System.out.println(new SimpleDateFormat(DEFAULT_FORMAT).parse("2016-10-04 00:00:00").getTime() + "L");
		System.out.println(new SimpleDateFormat(DEFAULT_FORMAT).parse("2016-10-05 00:00:00").getTime() + "L");
		System.out.println(new SimpleDateFormat(DEFAULT_FORMAT).parse("2016-10-06 00:00:00").getTime() + "L");
		System.out.println(new SimpleDateFormat(DEFAULT_FORMAT).parse("2016-10-07 00:00:00").getTime() + "L");
		System.out.println(new SimpleDateFormat(DEFAULT_FORMAT).parse("2016-10-08 00:00:00").getTime() + "L");*/
		
		Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("09-29", 60);
        map.put("09-30", 90);
        map.put("10-01", 90);
        map.put("10-02", 90);
        map.put("10-03", 90);
        map.put("10-04", 90);
        map.put("10-05", 90);
        map.put("10-06", 120);
        map.put("10-07", 120);
        map.put("10-08", 120);
        int time = 60;
        long value = 0;
        boolean execute = false;
        String day = new SimpleDateFormat("MM-dd").format(assign(1, Date.class));
        if(execute = map.containsKey(day)){
        	time = map.get(day);
        }
        if(execute){
        	Random random = new Random();
        	int probability = random.nextInt(100);
        	if(probability <= 0){
                probability = 0 - 100;
            }
            if(probability < 30){
                value = 30000;//30%
            }else if(probability < 50){
                value = 60000;//20%
            }else if(probability < 57){
                value = 90000;//7%
            }else if(probability < 72){
                value = 100000;//15%
            }else if(probability < 77){
                value = 130000;//5%
            }else if(probability < 82){
                value = 160000;//5%
            }else if(probability < 87){
                value = 190000;//5%
            }else if(probability < 97){
                value = 200000;//10%
            }else if(probability < 99){
                value = 230000;//2%
            }else{
                value = 260000;//1%
            }
            time = random.nextInt(time + (time - 30));
            while(time <= 30){
            	time = random.nextInt(time + (time - 30));
            }
        }
        if(value > 0){
        	System.out.println(value);
        }
        
	}
}
