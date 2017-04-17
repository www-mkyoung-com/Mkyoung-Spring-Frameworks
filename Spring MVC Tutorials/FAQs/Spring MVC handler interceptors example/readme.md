Spring MVC allow you to intercept web request through handler interceptors. The handler interceptor have to implement the **HandlerInterceptor** interface, which contains three methods :

1.  **preHandle()** – Called before the handler execution, returns a boolean value, “true” : continue the handler execution chain; “false”, stop the execution chain and return it.
2.  **postHandle()** – Called after the handler execution, allow manipulate the ModelAndView object before render it to view page.
3.  **afterCompletion()** – Called after the complete request has finished. Seldom use, cant find any use case.

In this tutorial, you will create two handler interceptors to show the use of the **HandlerInterceptor**.

1.  **ExecuteTimeInterceptor** – Intercept the web request, and log the controller execution time.
2.  **MaintenanceInterceptor** – Intercept the web request, check if the current time is in between the maintenance time, if yes then redirect it to maintenance page.

**Note**  
It’s recommended to extend the **HandlerInterceptorAdapter** for the convenient default implementations.

## 1\. ExecuteTimeInterceptor

Intercept the before and after controller execution, log the start and end of the execution time, save it into the existing intercepted controller’s modelAndView for later display.

_File : ExecuteTimeInterceptor.java_

    package com.mkyong.common.interceptor;

    import javax.servlet.http.HttpServletRequest;
    import javax.servlet.http.HttpServletResponse;

    import org.apache.log4j.Logger;
    import org.springframework.web.servlet.ModelAndView;
    import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

    public class ExecuteTimeInterceptor extends HandlerInterceptorAdapter{

    	private static final Logger logger = Logger.getLogger(ExecuteTimeInterceptor.class);

    	//before the actual handler will be executed
    	public boolean preHandle(HttpServletRequest request,
    		HttpServletResponse response, Object handler)
    	    throws Exception {

    		long startTime = System.currentTimeMillis();
    		request.setAttribute("startTime", startTime);

    		return true;
    	}

    	//after the handler is executed
    	public void postHandle(
    		HttpServletRequest request, HttpServletResponse response,
    		Object handler, ModelAndView modelAndView)
    		throws Exception {

    		long startTime = (Long)request.getAttribute("startTime");

    		long endTime = System.currentTimeMillis();

    		long executeTime = endTime - startTime;

    		//modified the exisitng modelAndView
    		modelAndView.addObject("executeTime",executeTime);

    		//log it
    		if(logger.isDebugEnabled()){
    		   logger.debug("[" + handler + "] executeTime : " + executeTime + "ms");
    		}
    	}
    }

## 2\. MaintenanceInterceptor

Intercept before the controller execution, check if the current time is in between the maintenance time, if yes then redirect it to maintenance page; else continue the execution chain.

_File : MaintenanceInterceptor.java_

    package com.mkyong.common.interceptor;

    import java.util.Calendar;

    import javax.servlet.http.HttpServletRequest;
    import javax.servlet.http.HttpServletResponse;

    import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

    public class MaintenanceInterceptor extends HandlerInterceptorAdapter{

    	private int maintenanceStartTime;
    	private int maintenanceEndTime;
    	private String maintenanceMapping;

    	public void setMaintenanceMapping(String maintenanceMapping) {
    		this.maintenanceMapping = maintenanceMapping;
    	}

    	public void setMaintenanceStartTime(int maintenanceStartTime) {
    		this.maintenanceStartTime = maintenanceStartTime;
    	}

    	public void setMaintenanceEndTime(int maintenanceEndTime) {
    		this.maintenanceEndTime = maintenanceEndTime;
    	}

    	//before the actual handler will be executed
    	public boolean preHandle(HttpServletRequest request,
    			HttpServletResponse response, Object handler)
    	    throws Exception {

    		Calendar cal = Calendar.getInstance();
    		int hour = cal.get(cal.HOUR_OF_DAY);

    		if (hour >= maintenanceStartTime && hour <= maintenanceEndTime) {
    			//maintenance time, send to maintenance page
    			response.sendRedirect(maintenanceMapping);
    			return false;
    		} else {
    			return true;
    		}

    	}
    }

## 3\. Enable the handler interceptor

To enable it, put your handler interceptor class in the handler mapping "**interceptors**" property.

    <beans xmlns="http://www.springframework.org/schema/beans"
    	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    	xsi:schemaLocation="http://www.springframework.org/schema/beans
    	http://www.springframework.org/schema/beans/spring-beans-2.5.xsd">

    	<bean class="org.springframework.web.servlet.handler.SimpleUrlHandlerMapping">
    		<property name="mappings">
    			<props>
    				<prop key="/welcome.htm">welcomeController</prop>
    			</props>
    		</property>
    		<property name="interceptors">
    			<list>
    				<ref bean="maintenanceInterceptor" />
    				<ref bean="executeTimeInterceptor" />
    			</list>
    		</property>
    	</bean>

    	<bean
    	class="org.springframework.web.servlet.mvc.support.ControllerClassNameHandlerMapping">
    		<property name="interceptors">
    			<list>
    				<ref bean="executeTimeInterceptor" />
    			</list>
    		</property>
    	</bean>

    	<bean id="welcomeController"
                      class="com.mkyong.common.controller.WelcomeController" />
    	<bean class="com.mkyong.common.controller.MaintenanceController" />

    	<bean id="executeTimeInterceptor"
                     class="com.mkyong.common.interceptor.ExecuteTimeInterceptor" />

    	<bean id="maintenanceInterceptor"
                    class="com.mkyong.common.interceptor.MaintenanceInterceptor">
    		<property name="maintenanceStartTime" value="23" />
    		<property name="maintenanceEndTime" value="24" />
    		<property name="maintenanceMapping" value="/SpringMVC/maintenance.htm" />
    	</bean>

    	<bean id="viewResolver"
    		class="org.springframework.web.servlet.view.InternalResourceViewResolver">
    		<property name="prefix">
    			<value>/WEB-INF/pages/</value>
    		</property>
    		<property name="suffix">
    			<value>.jsp</value>
    		</property>
    	</bean>

    </beans>

[http://www.mkyong.com/spring-mvc/spring-mvc-handler-interceptors-example/](http://www.mkyong.com/spring-mvc/spring-mvc-handler-interceptors-example/)
