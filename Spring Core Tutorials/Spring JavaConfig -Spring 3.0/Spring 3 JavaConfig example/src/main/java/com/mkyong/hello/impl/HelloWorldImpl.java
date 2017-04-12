package com.mkyong.hello.impl;

import com.mkyong.hello.HelloWorld;

public class HelloWorldImpl implements HelloWorld {

	@Override
	public void printHelloWorld(String msg) {

		System.out.println("Hello : " + msg);
	}

}