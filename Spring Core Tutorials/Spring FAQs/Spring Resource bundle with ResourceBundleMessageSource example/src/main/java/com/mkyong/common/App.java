package com.mkyong.common;

import java.util.Locale;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.mkyong.customer.services.CustomerService;

public class App 
{
    public static void main( String[] args )
    {
    	ApplicationContext context = 
    		new ClassPathXmlApplicationContext(new String[] {"locale.xml","Spring-Customer.xml"});
	
    	//get the message resource inside context
    	String name = context.getMessage("customer.name", 
    			new Object[] { 28, "http://www.mkyong.com" }, Locale.US);
    	System.out.println("Customer name (English) : " + name);
    	
    	String namechinese = context.getMessage("customer.name", 
    			new Object[] { 28, "http://www.mkyong.com" }, Locale.SIMPLIFIED_CHINESE);
    	System.out.println("Customer name (Chinese) : " + namechinese);
    	
    	//get the message resource inside the bean
    	CustomerService cust = (CustomerService)context.getBean("customerService");
    	cust.printMessage();
    }
}
