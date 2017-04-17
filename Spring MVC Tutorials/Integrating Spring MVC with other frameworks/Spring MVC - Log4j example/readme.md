![spring-log4j](http://www.mkyong.com/wp-content/uploads/2010/07/spring-log4j.jpg)

In this tutorial, we will show you how to use the log4j framework to do the logging in a Spring MVC web application.

Technologies and tools used :

1.  Log4j 1.2.17
2.  Spring 4.1.6.RELEASE
3.  Maven 3
4.  Tomcat 6
5.  Eclipse Kepler 4.3

**Note**  
By default, Spring (spring-core) is using the JCL ([commons-logging](http://commons.apache.org/proper/commons-logging/)) for logging, and the JCL has a runtime discovery algorithm to find out for other logging frameworks in well known places on the project classpath.

To integrate log4j, all you need to do is :

1.  Puts the `log4j.jar` in the project classpath.
2.  Create a `log4j.properties` or `log4j.xml` file in the project root classpath (if you follow the Maven standard directory structure, this should be the `resources` folder).

## 1\. Project Directory

Review the final project structure.

![spring-mvc-log4j](http://www.mkyong.com/wp-content/uploads/2010/07/spring-mvc-log4j.png)

## 2\. Project Dependencies

Declares the following dependencies :

pom.xml

    <properties>
    	<spring.version>4.1.6.RELEASE</spring.version>
    	<log4j.version>1.2.17</log4j.version>
    </properties>

    <dependencies>

    	<!-- Spring -->
    	<dependency>
    		<groupId>org.springframework</groupId>
    		<artifactId>spring-webmvc</artifactId>
    		<version>${spring.version}</version>
    	</dependency>

    	<!-- Log4j -->
    	<dependency>
    		<groupId>log4j</groupId>
    		<artifactId>log4j</artifactId>
    		<version>${log4j.version}</version>
    	</dependency>

    </dependencies>

## 3\. log4j.properties

Create a `log4j.properties` file, and put it in the `resources`. folder, refer to the above project directory structure.

log4j.properties

    # Root logger option
    log4j.rootLogger=DEBUG, stdout, file

    # Redirect log messages to console
    log4j.appender.stdout=org.apache.log4j.ConsoleAppender
    log4j.appender.stdout.Target=System.out
    log4j.appender.stdout.layout=org.apache.log4j.PatternLayout
    log4j.appender.stdout.layout.ConversionPattern=%d{yyyy-MM-dd HH:mm:ss} %-5p %c{1}:%L - %m%n

    # Redirect log messages to a log file
    log4j.appender.file=org.apache.log4j.RollingFileAppender
    #outputs to Tomcat home
    log4j.appender.file.File=${catalina.home}/logs/myapp.log
    log4j.appender.file.MaxFileSize=5MB
    log4j.appender.file.MaxBackupIndex=10
    log4j.appender.file.layout=org.apache.log4j.PatternLayout
    log4j.appender.file.layout.ConversionPattern=%d{yyyy-MM-dd HH:mm:ss} %-5p %c{1}:%L - %m%n

## 4\. Spring MVC Controller + Message Logging

A simple controller to return a welcome page. Furthermore, it shows you how to use log4j to do the logging.

WelcomeController.java

    package com.mkyong.common.controller;

    import org.apache.log4j.Logger;
    import org.springframework.stereotype.Controller;
    import org.springframework.web.bind.annotation.RequestMapping;
    import org.springframework.web.bind.annotation.RequestMethod;
    import org.springframework.web.servlet.ModelAndView;

    @Controller
    public class WelcomeController {

    	private static final Logger logger = Logger.getLogger(WelcomeController.class);

    	@RequestMapping(value = "/", method = RequestMethod.GET)
    	public ModelAndView getWelcome() {

    		//logs debug message
    		if(logger.isDebugEnabled()){
    			logger.debug("getWelcome is executed!");
    		}

    		//logs exception
    		logger.error("This is Error message", new Exception("Testing"));

    		ModelAndView model = new ModelAndView("welcome");
    		model.addObject("msg", "Hello Spring MVC + Log4j");
    		return model;

    	}

    }

## 5\. Demo

5.1 Download the [source code](http://www.mkyong.com/spring-mvc/spring-mvc-log4j-integration-example/#download), and run the web app with the embedded Jetty container.

    $ mvn jetty:run

Access URL :_ http://localhost:8080/spring-mvc-log4j/_

5.2 All logging messages will be displayed in the console.

log4j.properties

    # Redirect log messages to console
    log4j.appender.stdout=org.apache.log4j.ConsoleAppender
    log4j.appender.stdout.Target=System.out
    log4j.appender.stdout.layout=org.apache.log4j.PatternLayout
    log4j.appender.stdout.layout.ConversionPattern=%d{yyyy-MM-dd HH:mm:ss} %-5p %c{1}:%L - %m%n

Jetty console

    2015-06-19 14:10:35 DEBUG WelcomeController:19 - getWelcome is executed!
    2015-06-19 14:10:35 ERROR WelcomeController:23 - This is Error message
    java.lang.Exception: Testing
            at com.mkyong.common.controller.WelcomeController.getWelcome(WelcomeController.java:23)
            at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
            at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:57)
            at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
            at java.lang.reflect.Method.invoke(Method.java:606)

[http://www.mkyong.com/spring-mvc/spring-mvc-log4j-integration-example/](http://www.mkyong.com/spring-mvc/spring-mvc-log4j-integration-example/)
