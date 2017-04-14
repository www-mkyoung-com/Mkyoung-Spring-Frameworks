package com.mkyong.customer.services;

import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

public class CustomerService implements ResourceLoaderAware
{
	private ResourceLoader resourceLoader;
	
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}
		
	public Resource getResource(String location){
		return resourceLoader.getResource(location);
	}
}
