package com.mkyong;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {
	
	public static void main(String[] args) {

		String springConfig = "spring/batch/jobs/job-quartz.xml";

		ApplicationContext context = new ClassPathXmlApplicationContext(springConfig);

	}
}
