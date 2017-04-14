package com.mkyong.customer.services;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;

public class CustomerService implements MessageSourceAware
{
	private MessageSource messageSource;
	
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
	
	public void printMessage(){
		String name = messageSource.getMessage("customer.name", 
    			new Object[] { 28, "http://www.mkyong.com" }, Locale.US);
    	System.out.println("Customer name (English) : " + name);
    	
    	String namechinese = messageSource.getMessage("customer.name", 
    			new Object[] { 28, "http://www.mkyong.com" }, Locale.SIMPLIFIED_CHINESE);
    	System.out.println("Customer name (Chinese) : " + namechinese);
	}
	
}
