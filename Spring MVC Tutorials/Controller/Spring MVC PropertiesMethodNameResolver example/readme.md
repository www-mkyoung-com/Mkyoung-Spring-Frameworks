**PropertiesMethodNameResolver**, a flexible **MultiActionController** method name resolver, to **define the mapping between the URL and method name explicitly**. See following example :

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

## 2\. PropertiesMethodNameResolver

With **PropertiesMethodNameResolver**, you can map whatever URL name to corresponds method name easily :

    <beans ...>

     <bean
      class="org.springframework.web.servlet.mvc.support.ControllerClassNameHandlerMapping" />

     <bean class="com.mkyong.common.controller.CustomerController">
       <property name="methodNameResolver">
        <bean class="org.springframework.web.servlet.mvc.multiaction.PropertiesMethodNameResolver">
          <property name="mappings">
    	<props>
    	   <prop key="/customer/a.htm">add</prop>
    	   <prop key="/customer/b.htm">update</prop>
    	   <prop key="/customer/c.htm">delete</prop>
    	   <prop key="/customer/d.htm">list</prop>
    	   <prop key="/customer/whatever.htm">add</prop>
    	</props>
           </property>
         </bean>
        </property>
      </bean>

    </beans>

Now, the URL will map to the method name in the following pattern :

1.  /customer/a.htm –> add() method
2.  /customer/b.htm –> update() method
3.  /customer/c.htm –> delete() method
4.  /customer/d.htm –> list() method
5.  /customer/whatever.htm –> add() method

**Note **  
By default, **MultiActionController** is used the [InternalPathMethodNameResolver](http://www.mkyong.com/spring-mvc/spring-mvc-multiactioncontroller-example/) to map URL to the corresponds method name.

[http://www.mkyong.com/spring-mvc/spring-mvc-propertiesmethodnameresolver-example/](http://www.mkyong.com/spring-mvc/spring-mvc-propertiesmethodnameresolver-example/)
