package com.mkyong.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.Resource;

import com.mkyong.customer.services.CustomerService;
public class App 
{
    public static void main( String[] args )
    {
    	ApplicationContext appContext = 
    		new ClassPathXmlApplicationContext(new String[] {"Spring-Customer.xml"});
	
    	CustomerService cust = (CustomerService)appContext.getBean("customerService");
    	
    	//Resource resource = cust.getResource("file:c:\\testing.txt");
    	//Resource resource = cust.getResource("url:http://www.google.com");
    	Resource resource = cust.getResource("classpath:com/mkyong/common/testing.txt");
    	
    	try{
    		InputStream is = resource.getInputStream();
        	BufferedReader br = new BufferedReader(new InputStreamReader(is));
        	
        	String line;
        	while ((line = br.readLine()) != null) {
     	       System.out.println(line);
        	} 
        	br.close();
        	
    	}catch(IOException e){
    		e.printStackTrace();
    	}
    	
    }
}
