In J2EE / servlet web application, you can map error page to specify exception like this :

web.xml

    <error-page>
    <error-code>404</error-code>
    <location>/WEB-INF/pages/404.jsp</location>
      </error-page>

      <error-page>
    <exception-type>com.mkyong.web.exception.CustomException</exception-type>
    <location>/WEB-INF/pages/error/custom_error.jsp</location>
      </error-page>

      <error-page>
    <exception-type>java.lang.Exception</exception-type>
    <location>/WEB-INF/pages/generic_error.jsp</location>
      </error-page>

The above code should be self-exploratory. If the exception handling function exists in the servlet container, why we still need to use the Spring to handle the exception?

Generally, there are two reasons :

1.  **Customize Error Page** – The servlet container will render the error page directly; While the Spring allows you to populate model or data to the error page, so that you can customize a more user friendly error page.
2.  **Business logic** – Spring allow you to apply extra business logic before rendering the error page, like logging, auditing and etc.

In this tutorial, we will show you two examples to handle the exception in Spring.

1.  For Spring 2.x, we use `SimpleMappingExceptionResolver` in the XML file.
2.  For Spring 3.x, we can simplify the XML configuration via `@ExceptionHandler` annotation.

## 1\. SimpleMappingExceptionResolver Example

Review the directory structure.

![spring-mvc-exception-example-directory](http://www.mkyong.com/wp-content/uploads/2010/08/spring-mvc-exception-example-directory.png)

A custom exception.

CustomGenericException.java

    package com.mkyong.web.exception;

    public class CustomGenericException extends RuntimeException {

    	private static final long serialVersionUID = 1L;

    	private String errCode;
    	private String errMsg;

    	//getter and setter methods

    	public CustomGenericException(String errCode, String errMsg) {
    		this.errCode = errCode;
    		this.errMsg = errMsg;
    	}

    }

This controller class, just throw a `CustomGenericException` with custom error code and error description.

CustomerController.java

    package com.mkyong.web.controller;

    import javax.servlet.http.HttpServletRequest;
    import javax.servlet.http.HttpServletResponse;
    import org.springframework.web.servlet.ModelAndView;
    import org.springframework.web.servlet.mvc.AbstractController;
    import com.mkyong.web.exception.CustomGenericException;

    public class CustomerController extends AbstractController {

      @Override
      protected ModelAndView handleRequestInternal(HttpServletRequest request,
    	HttpServletResponse response) throws Exception {

    	throw new CustomGenericException("E888", "This is custom message - ABC");

      }

    }

Review the `SimpleMappingExceptionResolver` below :

mvc-dispatcher-servlet.xml

    <beans xmlns="http://www.springframework.org/schema/beans"
    	xmlns:context="http://www.springframework.org/schema/context"
    	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    	xmlns:mvc="http://www.springframework.org/schema/mvc"
    	xsi:schemaLocation="
            http://www.springframework.org/schema/beans
            http://www.springframework.org/schema/beans/spring-beans-3.0.xsd
            http://www.springframework.org/schema/mvc
    	http://www.springframework.org/schema/mvc/spring-mvc-3.0.xsd
            http://www.springframework.org/schema/context
            http://www.springframework.org/schema/context/spring-context-3.0.xsd">

    	<context:component-scan base-package="com.mkyong" />

    	<bean
    	class="org.springframework.web.servlet.mvc.support.ControllerClassNameHandlerMapping"/>

    	<!-- Register the bean -->
    	<bean class="com.mkyong.web.controller.CustomerController" />

    	<bean
    	  class="org.springframework.web.servlet.handler.SimpleMappingExceptionResolver">
    	  <property name="exceptionMappings">
    		<props>
    			<prop key="com.mkyong.wb.exception.CustomGenericException">
    				error/generic_error
    			</prop>
    			<prop key="java.lang.Exception">error/exception_error</prop>
    		</props>
    	  </property>
    	</bean>

    	<bean
    	  class="org.springframework.web.servlet.view.InternalResourceViewResolver">
    		<property name="prefix">
    			<value>/WEB-INF/pages/</value>
    		</property>
    		<property name="suffix">
    			<value>.jsp</value>
    		</property>
    	</bean>

    	<mvc:annotation-driven />

    </beans>

In above, when

1.  CustomGenericException is thrown, it will map to the view name “error/generic_error”.
2.  Any other Exceptions is thrown, it will map to the view name “error/exception_error”.

In the JSP page, you can access the exception instance via `${exception}`.

pages/error/generic_error.jsp.jsp

    <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
    <html>
    <body>

    	<c:if test="${not empty exception.errCode}">
    		<h1>${exception.errCode} : System Errors</h1>
    	</c:if>

    	<c:if test="${empty exception.errCode}">
    		<h1>System Errors</h1>
    	</c:if>

    	<c:if test="${not empty exception.errMsg}">
    		<div class="ads-in-post hide_if_width_less_800">
    <script async src="//pagead2.googlesyndication.com/pagead/js/adsbygoogle.js"></script>
    <!-- new 728x90 - After1stH4 -->
    <ins class="adsbygoogle hide_if_width_less_800"
         style="display:inline-block;width:728px;height:90px"
         data-ad-client="ca-pub-2836379775501347"
         data-ad-slot="7391621200"
         data-ad-region="mkyongregion"></ins>
    <script>
    (adsbygoogle = window.adsbygoogle || []).push({});
    </script>
    </div><h2>${exception.errMsg}</h2>
    	</c:if>

    </body>
    </html>

Demo – _http://localhost:8080/SpringMvcExample/customer_

![spring-mvc-exception-handle-1](http://www.mkyong.com/wp-content/uploads/2010/08/spring-mvc-exception-handle-1.png)

[http://www.mkyong.com/spring-mvc/spring-mvc-exception-handling-example/](http://www.mkyong.com/spring-mvc/spring-mvc-exception-handling-example/)
