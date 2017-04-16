In this tutorial, we show you a Spring 3 MVC hello world example, using Maven build tool.

Technologies used :

1.  Spring 3.2.13.RELEASE
2.  Maven 3
3.  JDK 1.6
4.  Eclipse 4.4
5.  Boostrap 3

**Spring 4 MVC XML**  
Try this [Spring 4 MVC hello world example](http://www.mkyong.com/spring-mvc/gradle-spring-mvc-web-project-example/).

**Spring 3 MVC Annotation**  
Try this [Spring 3 MVC hello world annotation example](http://www.mkyong.com/spring3/spring-3-mvc-hello-world-example-annotation/).

## 1\. Project Structure

Download the project [source code](http://www.mkyong.com/spring3/spring-3-mvc-hello-world-example/#download) and review the project folder structure :

![spring3-mvc-maven-project](http://www.mkyong.com/wp-content/uploads/2011/08/spring3-mvc-maven-project.png)

## 2\. Maven

A `pom.xml` template to quick start a Spring MVC project, it defines Spring 3 dependencies, an embedded Jetty container and Eclipse workspace configuration.

pom.xml

    <project xmlns="http://maven.apache.org/POM/4.0.0"
            xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
    	http://maven.apache.org/maven-v4_0_0.xsd">
    	<modelVersion>4.0.0</modelVersion>
    	<groupId>com.mkyong</groupId>
    	<artifactId>spring3-web</artifactId>
    	<packaging>war</packaging>
    	<version>1.0-SNAPSHOT</version>
    	<name>spring css</name>

    	<properties>
    		<jdk.version>1.6</jdk.version>
    		<spring.version>3.2.13.RELEASE</spring.version>
    		<jstl.version>1.2</jstl.version>
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

## 3\. Controller & Mapping

The `@RequestMapping` has been available since 2.5, but now enhanced to support REST style URLs.

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
    <title>Maven + Spring MVC</title>

    <spring:url value="/resources/core/css/hello.css" var="coreCss" />
    <spring:url value="/resources/core/css/bootstrap.min.css" var="bootstrapCss" />
    <link href="${bootstrapCss}" rel="stylesheet" />
    <link href="${coreCss}" rel="stylesheet" />
    </head>

    <nav class="navbar navbar-inverse navbar-fixed-top">
      <div class="container">
    	<div class="navbar-header">
    		<a class="navbar-brand" href="#">Spring 3 MVC Project</a>
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

## 5\. Spring XML Configuration

5.1 Enable component scanning, view resolver and resource mapping.

spring-web-servlet.xml

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

5.2 Declares a `DispatcherServlet` in `web.xml`. If the Spring XML configuration file is NOT specified, Spring will look for the `{servlet-name}-servlet.xml`.

In this example, Spring will look for the `spring-web-servlet.xml` file.

web.xml

    <web-app xmlns="http://java.sun.com/xml/ns/javaee"
    	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    	xsi:schemaLocation="http://java.sun.com/xml/ns/javaee
    	http://java.sun.com/xml/ns/javaee/web-app_2_5.xsd"
    	version="2.5">

    	<display-name>Spring3 MVC Application</display-name>

    	<servlet>
    		<servlet-name>spring-web</servlet-name>
    		<servlet-class>
                        org.springframework.web.servlet.DispatcherServlet
                    </servlet-class>
    		<load-on-startup>1</load-on-startup>
    	</servlet>

    	<servlet-mapping>
    		<servlet-name>spring-web</servlet-name>
    		<url-pattern>/</url-pattern>
    	</servlet-mapping>

    </web-app>

You can define a Spring XML file via `contextConfigLocation`.

web.xml

    <servlet>
    	<servlet-name>spring-web</servlet-name>
    	<servlet-class>
                         org.springframework.web.servlet.DispatcherServlet
                    </servlet-class>
    	<load-on-startup>1</load-on-startup>
    	<init-param>
    		<param-name>contextConfigLocation</param-name>
    		<param-value>/WEB-INF/spring-mvc-config.xml</param-value>
    	</init-param>
    </servlet>

            <servlet-mapping>
    	<servlet-name>spring-web</servlet-name>
    	<url-pattern>/</url-pattern>
    </servlet-mapping>

## 6\. Demo

The `pom.xml` file defines an embedded Jetty container. Issues `mvn jetty:run` to start the project.

Terminal

    $ mvn jetty:run
    ...
    [INFO] Started Jetty Server
    [INFO] Starting scanner at interval of 10 seconds.

URL : _http://localhost:8080/spring3_

![spring3-mvc-maven-xml-demo](http://www.mkyong.com/wp-content/uploads/2011/08/spring3-mvc-maven-xml-demo.png)

URL : _http://localhost:8080/spring3/hello/mkyong_

![spring3-mvc-maven-xml-demo2](http://www.mkyong.com/wp-content/uploads/2011/08/spring3-mvc-maven-xml-demo2.png)

## 7\. WAR File

To create a WAR file for deployment :

Terminal

    your-project$ mvn war:war

A WAR file will be created in `project\target\` folder.

    ${Project}\target\spring3-web-1.0-SNAPSHOT.war

[http://www.mkyong.com/spring3/spring-3-mvc-hello-world-example/](http://www.mkyong.com/spring3/spring-3-mvc-hello-world-example/)
