In Spring MVC, **InternalResourceViewResolver** is used to resolve “internal resource view” (in simple, it’s final output, jsp or htmp page) based on a predefined URL pattern. In additional, it allow you to add some predefined prefix or suffix to the view name (prefix + view name + suffix), and generate the final view page URL.

**What’s internal resource views?**  
In Spring MVC or any web application, for good practice, it’s always recommended to put the entire views or JSP files under “WEB-INF” folder, to protect it from direct access via manual entered URL. Those views under “WEB-INF” folder are named as internal resource views, as it’s only accessible by the servlet or Spring’s controllers class.

Following example show you how InternalResourceViewResolver works :

## 1\. Controller

A controller class to return a view, named “**WelcomePage**“.

    //...
    public class WelcomeController extends AbstractController{

    	@Override
    	protected ModelAndView handleRequestInternal(HttpServletRequest request,
    		HttpServletResponse response) throws Exception {

    		ModelAndView model = new ModelAndView("WelcomePage");

    		return model;
    	}
    }

## 2\. InternalResourceViewResolver

Register **InternalResourceViewResolver** bean in the Spring’s bean configuration file.

    <beans xmlns="http://www.springframework.org/schema/beans"
    	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    	xsi:schemaLocation="http://www.springframework.org/schema/beans
    	http://www.springframework.org/schema/beans/spring-beans-2.5.xsd">

       <bean
       class="org.springframework.web.servlet.mvc.support.ControllerClassNameHandlerMapping" />

    	<!-- Register the bean -->
    	<bean class="com.mkyong.common.controller.WelcomeController" />

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

Now, Spring will resolve the view’s name “**WelcomePage**” in the following way :

> prefix + view name + suffix = /WEB-INF/pages/**WelcomPage**.jsp

[http://www.mkyong.com/spring-mvc/spring-mvc-internalresourceviewresolver-example/](http://www.mkyong.com/spring-mvc/spring-mvc-internalresourceviewresolver-example/)
