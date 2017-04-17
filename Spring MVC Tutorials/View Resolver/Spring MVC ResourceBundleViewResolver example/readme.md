In Spring MVC, **ResourceBundleViewResolver** is used to resolve “view named” based on view beans in “.properties” file.

By default, `ResourceBundleViewResolver` will loads the view beans from file **views.properties**, which located at the root of the project class path. However, this location can be overridden through the “**basename**” property, for example,

    <beans ...>
    	<bean class="org.springframework.web.servlet.view.ResourceBundleViewResolver">
    		<property name="basename" value="spring-views" />
    	</bean>
    </beans>

In above case, it loads the view beans from “**spring-views.properties**“, which located at the root of the project class path.

**Note**  
The `ResourceBundleViewResolver` has the ability to load view beans from different resource bundles for different locales, but this use case is rarely required.

ResourceBundleViewResolver example to show you how it works :

## 1\. Controller

A controller class, return a view, named “**WelcomePage**“.

    //...
    public class WelcomeController extends AbstractController{

    	@Override
    	protected ModelAndView handleRequestInternal(HttpServletRequest request,
    		HttpServletResponse response) throws Exception {

    		ModelAndView model = new ModelAndView("WelcomePage");

    		return model;
    	}
    }

## 2\. ResourceBundleViewResolver

Register `ResourceBundleViewResolver` in the Spring’s bean configuration file, change the default view beans location to “**spring-views.properties**“.

    <beans xmlns="http://www.springframework.org/schema/beans"
    	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    	xsi:schemaLocation="http://www.springframework.org/schema/beans
    	http://www.springframework.org/schema/beans/spring-beans-2.5.xsd">

      <bean
      class="org.springframework.web.servlet.mvc.support.ControllerClassNameHandlerMapping" />

    	<!-- Register the bean -->
    	<bean class="com.mkyong.common.controller.WelcomeController" />

    	<bean class="org.springframework.web.servlet.view.ResourceBundleViewResolver">
    		<property name="basename" value="spring-views" />
    	</bean>

    </beans>

## 3\. View beans

Declare each view bean as a normal resource bundle style (key & message), where

1.  “**WelcomePage**” is the view name to match.
2.  “**.(class)**” is the type of view.
3.  “**.url**” is the view’s URL location.

_File : spring-views.properties_

    WelcomePage.(class)=org.springframework.web.servlet.view.JstlView
    WelcomePage.url=/WEB-INF/pages/WelcomePage.jsp

**Note**  
Put this “`spring-views.properties`” file on your project class path.

**How it works ?**  
When view name “**WelcomPage**” is returned by controller, the ResourceBundleViewResolver will find the key start with “**WelcomPage**” in “**spring-views.properties**” file, and return the corresponds view’s URL “**/WEB-INF/pages/WelcomPage.jsp**” back to the DispatcherServlet.

[http://www.mkyong.com/spring-mvc/spring-mvc-resourcebundleviewresolver-example/](http://www.mkyong.com/spring-mvc/spring-mvc-resourcebundleviewresolver-example/)
