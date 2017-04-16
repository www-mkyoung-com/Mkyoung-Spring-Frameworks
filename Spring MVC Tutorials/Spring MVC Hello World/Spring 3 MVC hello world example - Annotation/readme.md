In this tutorial, we will take the previous [Maven + Spring MVC XML example](http://www.mkyong.com/spring3/spring-3-mvc-hello-world-example/), rewrite it to support `@JavaConfig` configuration, no more XML files, and deploy it into a Servlet 3.0+ container, like Tomcat 7 or Jetty 9.

Technologies used :

1.  Spring 3.2.13.RELEASE
2.  Maven 3
3.  JDK 1.6
4.  Tomcat 7 or Jetty 9
5.  Eclipse 4.4
6.  Boostrap 3

**Spring 4 MVC Annotation**  
Try this [Spring 4 MVC hello world example – Annotation](http://www.mkyong.com/spring-mvc/gradle-spring-4-mvc-hello-world-example-annotation/).

## 1\. Project Structure

Download the project [source code](http://www.mkyong.com/spring3/spring-3-mvc-hello-world-example-annotation/#download) and review the project folder structure :

![spring3-mvc-hello-world-annotation](http://www.mkyong.com/wp-content/uploads/2015/06/spring3-mvc-hello-world-annotation.png)

_P.S No more XML files like `web.xml` or any other Spring XML configuration files._

## 2\. Maven

2.1 A `pom.xml` template to quick start a Spring MVC project. To compile this project, we need to add a `servlet-api`dependency.

pom.xml

    <project xmlns="http://maven.apache.org/POM/4.0.0"
      xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
      xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
      http://maven.apache.org/maven-v4_0_0.xsd">
      <modelVersion>4.0.0</modelVersion>
      <groupId>com.mkyong</groupId>
      <artifactId>spring3-mvc-maven-annotation-hello-world</artifactId>
      <packaging>war</packaging>
      <version>1.0-SNAPSHOT</version>
      <name>spring mvc</name>

      <properties>
    	<jdk.version>1.6</jdk.version>
    	<spring.version>3.2.13.RELEASE</spring.version>
    	<jstl.version>1.2</jstl.version>
    	<servletapi.version>3.1.0</servletapi.version>
      </properties>

      <dependencies>

    	<dependency>
    		<groupId>org.springframework</groupId>
    		<artifactId>spring-webmvc</artifactId>
    		<version>${spring.version}</version>
    	</dependency>

    	<dependency>
    		<groupId>javax.servlet</groupId>
    		<artifactId>jstl</artifactId>
    		<version>${jstl.version}</version>
    	</dependency>

    	<!-- compile only, deployed container will provide this -->
    	<dependency>
    		<groupId>javax.servlet</groupId>
    		<artifactId>javax.servlet-api</artifactId>
    		<version>${servletapi.version}</version>
    		<scope>provided</scope>
    	</dependency>

      </dependencies>

      <build>
        <plugins>

    	<plugin>
    		<groupId>org.apache.maven.plugins</groupId>
    		<artifactId>maven-compiler-plugin</artifactId>
    		<version>3.3</version>
    		<configuration>
    			<source>${jdk.version}</source>
    			<target>${jdk.version}</target>
    		</configuration>
    	</plugin>

    	<!-- embedded Jetty server, for testing -->
    	<plugin>
    		<groupId>org.eclipse.jetty</groupId>
    		<artifactId>jetty-maven-plugin</artifactId>
    		<version>9.2.11.v20150529</version>
    		<configuration>
    			<scanIntervalSeconds>10</scanIntervalSeconds>
    			<webApp>
    				<contextPath>/spring3</contextPath>
    			</webApp>
    		</configuration>
    	</plugin>

    	<!-- configure Eclipse workspace -->
    	<plugin>
    		<groupId>org.apache.maven.plugins</groupId>
    		<artifactId>maven-eclipse-plugin</artifactId>
    		<version>2.9</version>
    		<configuration>
    			<downloadSources>true</downloadSources>
    			<downloadJavadocs>true</downloadJavadocs>
    			<wtpversion>2.0</wtpversion>
    			<wtpContextName>spring3</wtpContextName>
    		</configuration>
    	</plugin>

        </plugins>
      </build>

      </project>

2.2 To compile this project and make it supports Eclipse IDE.

Terminal

    $ mvn eclipse:eclipse

## 3\. Spring Controller

HelloController.java

    package com.mkyong.web.controller;

    import org.springframework.stereotype.Controller;
    import org.springframework.ui.ModelMap;
    import org.springframework.web.bind.annotation.PathVariable;
    import org.springframework.web.bind.annotation.RequestMapping;
    import org.springframework.web.bind.annotation.RequestMethod;
    import org.springframework.web.servlet.ModelAndView;

    @Controller
    public class HelloController {

    	@RequestMapping(value = "/", method = RequestMethod.GET)
    	public String printWelcome(ModelMap model) {

    		model.addAttribute("message", "Spring 3 MVC Hello World");
    		return "hello";

    	}

    	@RequestMapping(value = "/hello/{name:.+}", method = RequestMethod.GET)
    	public ModelAndView hello(@PathVariable("name") String name) {

    		ModelAndView model = new ModelAndView();
    		model.setViewName("hello");
    		model.addObject("msg", name);

    		return model;

    	}

    }

## 4\. JSP Views

A JSP page to display the value, and include bootstrap css and js.

html4strict

    <%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
    <!DOCTYPE html>
    <html lang="en">
    <head>
    <title>Maven + Spring MVC + @JavaConfig</title>

    <spring:url value="/resources/core/css/hello.css" var="coreCss" />
    <spring:url value="/resources/core/css/bootstrap.min.css" var="bootstrapCss" />
    <link href="${bootstrapCss}" rel="stylesheet" />
    <link href="${coreCss}" rel="stylesheet" />
    </head>

    <nav class="navbar navbar-inverse navbar-fixed-top">
      <div class="container">
    	<div class="navbar-header">
    		<a class="navbar-brand" href="#">Spring 3 MVC Project @JavaConfig</a>
    	</div>
      </div>
    </nav>

    <div class="jumbotron">
      <div class="container">
    	<h1>${title}</h1>
    	<p>
    		<c:if test="${not empty name}">
    			Hello ${name}
    		</c:if>

    		<c:if test="${empty name}">
    			Welcome Welcome!
    		</c:if>
        </p>
        <p>
    		<a class="btn btn-primary btn-lg" href="#" role="button">Learn more</a>
    	</p>
    	</div>
    </div>

    <div class="container">

      <div class="row">
    	<div class="col-md-4">
    		<h2>Heading</h2>
    		<p>ABC</p>
    		<p>
    			<a class="btn btn-default" href="#" role="button">View details</a>
    		</p>
    	</div>
    	<div class="col-md-4">
    		<h2>Heading</h2>
    		<p>ABC</p>
    		<p>
    			<a class="btn btn-default" href="#" role="button">View details</a>
    		</p>
    	</div>
    	<div class="col-md-4">
    		<h2>Heading</h2>
    		<p>ABC</p>
    		<p>
    			<a class="btn btn-default" href="#" role="button">View details</a>
    		</p>
    	</div>
      </div>

      <hr>
      <footer>
    	<p>© Mkyong.com 2015</p>
      </footer>
    </div>

    <spring:url value="/resources/core/css/hello.js" var="coreJs" />
    <spring:url value="/resources/core/css/bootstrap.min.js" var="bootstrapJs" />

    <script src="${coreJs}"></script>
    <script src="${bootstrapJs}"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>

    </body>
    </html>

## 5\. Spring @JavaConfig

SpringWebConfig.java

    package com.mkyong.config;

    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.ComponentScan;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.web.servlet.config.annotation.EnableWebMvc;
    import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
    import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
    import org.springframework.web.servlet.view.InternalResourceViewResolver;
    import org.springframework.web.servlet.view.JstlView;

    @EnableWebMvc //mvc:annotation-driven
    @Configuration
    @ComponentScan({ "com.mkyong.web" })
    public class SpringWebConfig extends WebMvcConfigurerAdapter {

    	@Override
    	public void addResourceHandlers(ResourceHandlerRegistry registry) {
    		registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
    	}

    	@Bean
    	public InternalResourceViewResolver viewResolver() {
    		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
    		viewResolver.setViewClass(JstlView.class);
    		viewResolver.setPrefix("/WEB-INF/views/jsp/");
    		viewResolver.setSuffix(".jsp");
    		return viewResolver;
    	}

    }

XML equivalent.

    <beans xmlns="http://www.springframework.org/schema/beans"
    	xmlns:context="http://www.springframework.org/schema/context"
    	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    	xmlns:mvc="http://www.springframework.org/schema/mvc"
    	xsi:schemaLocation="
            http://www.springframework.org/schema/beans
            http://www.springframework.org/schema/beans/spring-beans-3.2.xsd
            http://www.springframework.org/schema/mvc
            http://www.springframework.org/schema/mvc/spring-mvc-3.2.xsd
            http://www.springframework.org/schema/context
            http://www.springframework.org/schema/context/spring-context-3.2.xsd">

    	<context:component-scan base-package="com.mkyong.web" />

    	<bean
    		class="org.springframework.web.servlet.view.InternalResourceViewResolver">
    		<property name="prefix">
    			<value>/WEB-INF/views/jsp/</value>
    		</property>
    		<property name="suffix">
    			<value>.jsp</value>
    		</property>
    	</bean>

    	<mvc:resources mapping="/resources/**" location="/resources/" />

    	<mvc:annotation-driven />

    </beans>

## 6\. Servlet 3.0+ Container

Create a ServletInitializer class by extending `AbstractAnnotationConfigDispatcherServletInitializer`, the Servlet 3.0+ container will pick up this class and run it automatically. This is the replacement for `web.xml`.

MyWebInitializer.java

    package com.mkyong.servlet3;

    import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

    import com.mkyong.config.SpringWebConfig;

    public class MyWebInitializer extends
    		AbstractAnnotationConfigDispatcherServletInitializer {

    	@Override
    	protected Class<?>[] getServletConfigClasses() {
    		return new Class[] { SpringWebConfig.class };
    	}

    	@Override
    	protected String[] getServletMappings() {
    		return new String[] { "/" };
    	}

    	@Override
    	protected Class<?>[] getRootConfigClasses() {
    		return null;
    	}

    }

## 7\. Demo

Download the project and run it with the embedded Jetty container.

Terminal

    $ mvn jetty:run

URL : _http://localhost:8080/spring3_

![spring3-mvc-maven-xml-demo](http://www.mkyong.com/wp-content/uploads/2011/08/spring3-mvc-maven-xml-demo.png)

URL : _http://localhost:8080/spring3/hello/mkyong_

![spring3-mvc-maven-xml-demo2](http://www.mkyong.com/wp-content/uploads/2011/08/spring3-mvc-maven-xml-demo2.png)

[http://www.mkyong.com/spring3/spring-3-mvc-hello-world-example-annotation/](http://www.mkyong.com/spring3/spring-3-mvc-hello-world-example-annotation/)
