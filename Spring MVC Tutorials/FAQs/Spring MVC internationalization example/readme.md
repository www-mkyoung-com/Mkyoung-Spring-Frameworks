In Spring MVC application, comes with few “**LocaleResolver**” to support the internationalization or multiple languages features. In this tutorial, it shows a simple welcome page, display the message from properties file, and change the locale based on the selected language link.

## 1\. Project Folder

Directory structure of this example.

![SpringMVC-Internationalization-Folder](http://www.mkyong.com/wp-content/uploads/2010/08/SpringMVC-Internationalization-Folder.jpg)

## 2\. Properties file

Two properties files to store English and Chinese messages.

**welcome.properties**

    welcome.springmvc = Happy learning Spring MVC

**welcome_zh_CN.properties**

    welcome.springmvc = \u5feb\u4e50\u5b66\u4e60 Spring MVC

**Note**  
For UTF-8 or non-English characters , you can encode it with [native2ascii](http://www.mkyong.com/java/java-convert-chinese-character-to-unicode-with-native2ascii/) tool.

## 3\. Controller

Controller class, nothing special here, all the locale stuff is configure in the Spring’s bean configuration file later.

    package com.mkyong.common.controller;

    import javax.servlet.http.HttpServletRequest;
    import javax.servlet.http.HttpServletResponse;
    import org.springframework.web.servlet.ModelAndView;
    import org.springframework.web.servlet.mvc.AbstractController;

    public class WelcomeController extends AbstractController{

    	@Override
    	protected ModelAndView handleRequestInternal(HttpServletRequest request,
    		HttpServletResponse response) throws Exception {

    		ModelAndView model = new ModelAndView("WelcomePage");
    		return model;
    	}

    }

## 4\. Spring Configuration

To make Spring MVC application supports the internationalization, register two beans :

**1\. SessionLocaleResolver**  
Register a “SessionLocaleResolver” bean, named it exactly the same characters “**localeResolver**“. It resolves the locales by getting the predefined attribute from user’s session.

**Note**  
If you do not register any “localeResolver”, the default **AcceptHeaderLocaleResolver** will be used, which resolves the locale by checking the accept-language header in the HTTP request.

**2\. LocaleChangeInterceptor**  
Register a “LocaleChangeInterceptor” interceptor and reference it to any handler mapping that need to supports the multiple languages. The “**paramName**” is the parameter value that’s used to set the locale.

In this case,

1.  welcome.htm?language=en – Get the message from English properties file.
2.  welcome.htm?language=zh_CN – Get the message from Chinese properties file.

    <bean id="localeChangeInterceptor"
    	class="org.springframework.web.servlet.i18n.LocaleChangeInterceptor">
    	<property name="paramName" value="language" />
    </bean>

    <bean class="org.springframework.web.servlet.mvc.support.ControllerClassNameHandlerMapping" >
    	<property name="interceptors">
    	   <list>
    		<ref bean="localeChangeInterceptor" />
    	    </list>
    	</property>
    </bean>

See full example below  
**mvc-dispatcher-servlet.xml**

    <beans xmlns="http://www.springframework.org/schema/beans"
    	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    	xsi:schemaLocation="http://www.springframework.org/schema/beans
    	http://www.springframework.org/schema/beans/spring-beans-2.5.xsd">

    	<bean id="localeResolver"
    		class="org.springframework.web.servlet.i18n.SessionLocaleResolver">
    		<property name="defaultLocale" value="en" />
    	</bean>

    	<bean id="localeChangeInterceptor"
    		class="org.springframework.web.servlet.i18n.LocaleChangeInterceptor">
    		<property name="paramName" value="language" />
    	</bean>

    	<bean class="org.springframework.web.servlet.mvc.support.ControllerClassNameHandlerMapping" >
    		<property name="interceptors">
    		   <list>
    			<ref bean="localeChangeInterceptor" />
    		   </list>
    		</property>
    	</bean>

    	<!-- Register the bean -->
    	<bean class="com.mkyong.common.controller.WelcomeController" />

    	<!-- Register the welcome.properties -->
    	<bean id="messageSource"
    		class="org.springframework.context.support.ResourceBundleMessageSource">
    		<property name="basename" value="welcome" />
    	</bean>

    	<bean id="viewResolver"
        	class="org.springframework.web.servlet.view.InternalResourceViewResolver" >
            <property name="prefix">
                <value>/WEB-INF/pages/</value>
            </property>
            <property name="suffix">
                <value>.jsp</value>
            </property>
        </bean>

    </beans>

## 5\. JSP

A JSP page, contains two hyperlinks to change the locale manually, and use the **spring:message** to display the message from the corresponds properties file by checking the current user’s locale.

**WelcomePage.jsp**

    <%@ page contentType="text/html;charset=UTF-8" %>
    <%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
    <html>
    <body>
    <h1>Spring MVC internationalization example</h1>

    Language : <a href="?language=en">English</a>|<a href="?language=zh_CN">Chinese</a>

    <h2>
    welcome.springmvc : <spring:message code="welcome.springmvc" text="default text" />
    </h2>

    Current Locale : ${pageContext.response.locale}

    </body>
    </html>

**Note**  
The ${pageContext.response.locale} can be used to display the current user’s locale.

**Warning**  
Remember put the “<%@ page contentType=”text/html;charset=UTF-8″ %>” on top of the page, else the page may not able to display the UTF-8 (Chinese) characters properly.

## 7\. Demo

Access it via _http://localhost:8080/SpringMVC/welcome.htm_, change the locale by clicking on the language’s link.

**1\. English locale** – http://localhost:8080/SpringMVC/welcome.htm?language=en

![SpringMVC-Internationalization-Example-1](http://www.mkyong.com/wp-content/uploads/2010/08/SpringMVC-Internationalization-Example-1.jpg)

**2\. Chinese locale** – http://localhost:8080/SpringMVC/welcome.htm?language=zh_CN

![SpringMVC-Internationalization-Example-2](http://www.mkyong.com/wp-content/uploads/2010/08/SpringMVC-Internationalization-Example-2.jpg)

[http://www.mkyong.com/spring-mvc/spring-mvc-internationalization-example/](http://www.mkyong.com/spring-mvc/spring-mvc-internationalization-example/)
