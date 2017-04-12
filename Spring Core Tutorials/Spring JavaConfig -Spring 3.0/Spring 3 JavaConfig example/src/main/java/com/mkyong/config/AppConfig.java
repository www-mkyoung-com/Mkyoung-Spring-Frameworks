package com.mkyong.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.mkyong.hello.HelloWorld;
import com.mkyong.hello.impl.HelloWorldImpl;

@Configuration
public class AppConfig {
	
	@Bean(name="helloBean")
    public HelloWorld helloWorld() {
        return new HelloWorldImpl();
    }
	
}