In Spring MVC application, **MultiActionController** is used to group related actions into a single controller, the method handler have to follow below signature :

    public (ModelAndView | Map | String | void) actionName(
    	HttpServletRequest, HttpServletResponse [,HttpSession] [,CommandObject]);

## 1\. MultiActionController

See a MultiActionController example.

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

With **ControllerClassNameHandlerMapping** configured.

    <beans ...>

     <bean
      class="org.springframework.web.servlet.mvc.support.ControllerClassNameHandlerMapping" />

      <bean class="com.mkyong.common.controller.CustomerController" />

    </beans>

## 2\. Mapping Examples

Now, the reuqested URL will map to the method name in the following patterns :

1.  **Customer**Controller –> **/customer/***
2.  /customer/**add**.htm –> **add()**
3.  /customer/**delete**.htm –> **delete()**
4.  /customer/**update**.htm –> **update()**
5.  /customer/**list**.htm –> **list()**

## 3\. InternalPathMethodNameResolver

The InternalPathMethodNameResolver is the default **MultiActionController** implementation to map URL to method name. But, you are still allow to add prefix or suffix to the method name :

    <beans ...>
     <bean
      class="org.springframework.web.servlet.mvc.support.ControllerClassNameHandlerMapping" />

      <bean class="com.mkyong.common.controller.CustomerController">
         <property name="methodNameResolver">
    	<bean class="org.springframework.web.servlet.mvc.multiaction.InternalPathMethodNameResolver">
    	   <property name="prefix" value="test" />
    	   <property name="suffix" value="Customer" />
    	</bean>
         </property>
       </bean>
    </beans>

Now, the URL will map to the method name in the following pattern :

1.  **Customer**Controller –> **/customer/***
2.  /customer/**add**.htm –> test**add**Customer()
3.  /customer/**delete**.htm –> test**delete**Customer()
4.  /customer/**update**.htm –> test**update**Customer()
5.  /customer/**list**.htm –> test**listC**ustomer()

Note  
With annotation, the MultiActionController is more easy to configure, visit this [MultiActionController annotation example](http://www.mkyong.com/spring-mvc/spring-mvc-multiactioncontroller-annotation-example/) for detail.

[http://www.mkyong.com/spring-mvc/spring-mvc-multiactioncontroller-example/](http://www.mkyong.com/spring-mvc/spring-mvc-multiactioncontroller-example/)
