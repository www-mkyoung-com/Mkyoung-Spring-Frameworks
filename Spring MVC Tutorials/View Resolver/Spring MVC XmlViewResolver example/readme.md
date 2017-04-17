In Spring MVC, **XmlViewResolver** is used to resolve “view name” based on view beans in the XML file. By default, `XmlViewResolver` will loads the view beans from **/WEB-INF/views.xml**, however, this location can be overridden through the “**location**” property :

    <beans ...>
    	<bean class="org.springframework.web.servlet.view.XmlViewResolver">
    	   <property name="location">
    		<value>/WEB-INF/spring-views.xml</value>
    	   </property>
    	</bean>
    </beans>

In above case, it loads the view beans from “**/WEB-INF/spring-views.xml**“. See XmlViewResolver example :

## 1\. Controller

A controller class, returns a view, named “**WelcomePage**“.

    //...
    public class WelcomeController extends AbstractController{

    	@Override
    	protected ModelAndView handleRequestInternal(HttpServletRequest request,
    		HttpServletResponse response) throws Exception {

    		ModelAndView model = new ModelAndView("WelcomePage");

    		return model;
    	}
    }

## 2\. XmlViewResolver

Register the XmlViewResolver in the Spring’s bean configuration file, loads the view beans from “**/WEB-INF/spring-views.xml**“.

    <beans xmlns="http://www.springframework.org/schema/beans"
    	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    	xsi:schemaLocation="http://www.springframework.org/schema/beans
    	http://www.springframework.org/schema/beans/spring-beans-2.5.xsd">

       <bean
       class="org.springframework.web.servlet.mvc.support.ControllerClassNameHandlerMapping" />

    	<!-- Register the bean -->
    	<bean class="com.mkyong.common.controller.WelcomeController" />

    	<bean class="org.springframework.web.servlet.view.XmlViewResolver">
    	   <property name="location">
    	       <value>/WEB-INF/spring-views.xml</value>
    	   </property>
    	</bean>

    </beans>

## 3\. View beans

The “**view bean**” is just a normal Spring bean declared in the Spring’s bean configuration file, where

1.  “**id**” is the “view name” to resolve.
2.  “**class**” is the type of the view.
3.  “**url**” property is the view’s url location.

_File : spring-views.xml_

    <beans xmlns="http://www.springframework.org/schema/beans"
    	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    	xsi:schemaLocation="http://www.springframework.org/schema/beans
    	http://www.springframework.org/schema/beans/spring-beans-2.5.xsd">

    	<bean id="WelcomePage"
    		class="org.springframework.web.servlet.view.JstlView">
    		<property name="url" value="/WEB-INF/pages/WelcomePage.jsp" />
    	</bean>

    </beans>

**How it works ?**  
When a view name “**WelcomPage**” is returned by controller, the` XmlViewResolver` will find the bean id “**WelcomPage**” in “spring-views.xml” file, and return the corresponds view’s URL “**/WEB-INF/pages/WelcomPage.jsp**” back to the `DispatcherServlet`.

[http://www.mkyong.com/spring-mvc/spring-mvc-xmlviewresolver-example/](http://www.mkyong.com/spring-mvc/spring-mvc-xmlviewresolver-example/)
