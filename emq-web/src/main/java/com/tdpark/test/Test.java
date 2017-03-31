package com.tdpark.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.google.gson.Gson;
import com.tdpark.common.dal.EntityDAL;
import com.tdpark.common.domain.Entity;
import com.tdpark.run.lock.SimpleLock;

public class Test {

	public static void main(String[] args) {
		ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:spring/application-context.xml");
		EntityDAL factory = ac.getBean(EntityDAL.class);
		Entity entity = factory._pop(new SimpleLock(1));
		System.out.println(new Gson().toJson(entity));
	}
}
