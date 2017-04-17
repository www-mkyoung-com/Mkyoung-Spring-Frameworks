**ParameterMethodNameResolver**, a MultiActionController method name resolver to **map URL to method name via request parameter name**, and the parameter name is customizable through the “**paramName**” property. See following example :

## 1\. MultiActionController

A MultiActionController example.

    package com.mkyong.common.controller;

    import javax.servlet.http.HttpServletRequest;
    import javax.servlet.http.HttpServletResponse;
    import org.springframework.web.servlet.ModelAndView;
    import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

    public class CustomerController extends MultiActionController{

    	public ModelAndView add(HttpServletRequest request,
    		HttpServletResponse response) throws Exception {

    		return new ModelAndView("CustomerPage", "msg","add() method");

    	}

    	public ModelAndView delete(HttpServletRequest request,
    		HttpServletResponse response) throws Exception {

    		return new ModelAndView("CustomerPage", "msg","delete() method");

    	}

    	public ModelAndView update(HttpServletRequest request,
    		HttpServletResponse response) throws Exception {

    		return new ModelAndView("CustomerPage", "msg","update() method");

    	}

    	public ModelAndView list(HttpServletRequest request,
    		HttpServletResponse response) throws Exception {

    		return new ModelAndView("CustomerPage", "msg","list() method");

    	}

    }

## 2\. ParameterMethodNameResolver

With **ParameterMethodNameResolver** configured, and define the parameter name thought the “**paramName**” property:

    <beans ...>

     <bean
      class="org.springframework.web.servlet.mvc.support.ControllerClassNameHandlerMapping" />

      <bean class="com.mkyong.common.controller.CustomerController">
         <property name="methodNameResolver">
    	<bean class="org.springframework.web.servlet.mvc.multiaction.ParameterMethodNameResolver">
    	   <property name="paramName" value="action"/>
    	</bean>
         </property>
       </bean>

    </beans>

Now, the URL will map to the method name via the “action” request parameter name :

1.  /customer/*.htm**?action=add** –> add() method
2.  /customer/whatever.htm**?action=add** –> add() method
3.  /customer/*.htm**?action=update** –> update() method
4.  /customer/*.htm**?action=delete** –> delete() method
5.  /customer/*.htm**?action=list** –> list() method

_P.S the “*****” means any text._

**Note **  
By default, **MultiActionController** is used the [InternalPathMethodNameResolver](http://www.mkyong.com/spring-mvc/spring-mvc-multiactioncontroller-example/) to map URL to the corresponds method name.

[http://www.mkyong.com/spring-mvc/spring-mvc-parametermethodnameresolver-example/](http://www.mkyong.com/spring-mvc/spring-mvc-parametermethodnameresolver-example/)
