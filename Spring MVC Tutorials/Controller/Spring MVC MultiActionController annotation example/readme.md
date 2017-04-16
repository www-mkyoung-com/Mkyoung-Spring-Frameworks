In this tutorial, we show you how to develop a Spring MVC annotation-based **MultiActionController**, by using `@RequestMapping`.

In XML-based MultiActionController, you have to configure the method name resolver ([InternalPathMethodNameResolver](http://www.mkyong.com/spring-mvc/spring-mvc-multiactioncontroller-example/), [PropertiesMethodNameResolver](http://www.mkyong.com/spring-mvc/spring-mvc-propertiesmethodnameresolver-example/) or [ParameterMethodNameResolver](http://www.mkyong.com/spring-mvc/spring-mvc-parametermethodnameresolver-example/)) to map the URL to a particular method name. But, life is more easier with annotation support, now you can use **@RequestMapping** annotation as a method name resolver, which used to map URL to a particular method.

**Note**  
This annotation-based example is converted from the last Spring MVC [MultiActionController XML-based example](http://www.mkyong.com/spring-mvc/spring-mvc-multiactioncontroller-example/). So, please compare and spots the different.

To configure it, define **@RequestMapping** with mapping URL above the method name.

    package com.mkyong.common.controller;

    import javax.servlet.http.HttpServletRequest;
    import javax.servlet.http.HttpServletResponse;
    import org.springframework.stereotype.Controller;
    import org.springframework.web.bind.annotation.RequestMapping;
    import org.springframework.web.servlet.ModelAndView;

    @Controller
    public class CustomerController{

    	@RequestMapping("/customer/add.htm")
    	public ModelAndView add(HttpServletRequest request,
    		HttpServletResponse response) throws Exception {

    		return new ModelAndView("CustomerAddView");

    	}

    	@RequestMapping("/customer/delete.htm")
    	public ModelAndView delete(HttpServletRequest request,
    		HttpServletResponse response) throws Exception {

    		return new ModelAndView("CustomerDeleteView");

    	}

    	@RequestMapping("/customer/update.htm")
    	public ModelAndView update(HttpServletRequest request,
    		HttpServletResponse response) throws Exception {

    		return new ModelAndView("CustomerUpdateView");

    	}

    	@RequestMapping("/customer/list.htm")
    	public ModelAndView list(HttpServletRequest request,
    		HttpServletResponse response) throws Exception {

    		return new ModelAndView("CustomerListView");

    	}
    }

Now, the URL will map to the method name in the following patterns :

1.  /customer/add.htm –> add() method
2.  /customer/delete.htm –> delete() method
3.  /customer/update.htm –> update() method
4.  /customer/list.htm –> list() method

**Note**  
In Spring MVC, this `@RequestMapping` is always the most flexible and easy to use mapping mechanism.

[http://www.mkyong.com/spring-mvc/spring-mvc-multiactioncontroller-annotation-example/](http://www.mkyong.com/spring-mvc/spring-mvc-multiactioncontroller-annotation-example/)
