In general, to return a view or page in Spring MVC application, you need to create a class, which extends the **AbstractController **, and return a **ModelAndView()** object.

    public class WelcomeController extends AbstractController{

    	@Override
    	protected ModelAndView handleRequestInternal(HttpServletRequest request,
    		HttpServletResponse response) throws Exception {

    		ModelAndView model = new ModelAndView("WelcomePage");
    		return model;

    	}

    }

In the bean configuration file, declared a **ControllerClassNameHandlerMapping** to auto detect the mapping.

    <bean
     class="org.springframework.web.servlet.mvc.support.ControllerClassNameHandlerMapping" />

    <bean class="com.mkyong.common.controller.WelcomeController" />

But, don’t you think it’s too much configuration for a simple redirect task? Fortunately, Spring comes with **ParameterizableViewController** to simplify the above processes. With **ParameterizableViewController**, you don’t need to hard code the view name in the controller class anymore, instead, you put view name declarative through the ParameterizableViewController’s “**viewName**” property.

**Note**  
The ParameterizableViewController is a subclass of AbstractController, and return a ModelAndView based on the “viewName” property, **it’s purely a redirect class**, nothing more, nothing less :)

**ParameterizableViewController.java**

    public class ParameterizableViewController extends AbstractController{
    //...
    protected ModelAndView handleRequestInternal(
        HttpServletRequest request, HttpServletResponse response)
    	throws Exception {

    	return new ModelAndView(getViewName());
    }

## Tutorial

In this tutorial, it shows the use of **ParameterizableViewController** controller to do a page redirection in the Spring MVC application.

## 1\. ParameterizableViewController

No controller class is required, just declared the **ParameterizableViewController** bean and specify the view name through the “**viewName**” property. Additionally, you have to define an explicit mapping for it.

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
        </bean>

       <bean name="welcomeController"
                class="org.springframework.web.servlet.mvc.ParameterizableViewController">
    	    <property name="viewName" value="WelcomePage" />
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

**Define an explicit mapping is required.**

    <beans ...>
    //...
    <bean
    class="org.springframework.web.servlet.mvc.support.ControllerClassNameHandlerMapping" />

    <bean name="welcomeController"
        class="org.springframework.web.servlet.mvc.ParameterizableViewController">
    	<property name="viewName" value="WelcomePage" />
    </bean>
    //...
    </beans>

In above snippet, are you expect a view name “welcome” will return a “WelcomePage”? Sorry, it’s not, you have to define an **explicit mapping**, because the **ControllerClassNameHandlerMapping** won’t generate a mapping for any built-in Spring MVC controller.

## 2\. View

Just a simple JSP to display a head line.

**WelcomePage.jsp.jsp**

    <html>
    <body>
    <h2>ParameterizableViewController Example</h2>
    </body>
    </html>

## 3\. Demo

Access it via “_http://localhost:8080/SpringMVC/welcome.htm_“, the “**welcome.htm**” will return back a “**/WEB-INF/pages/WelcomPage.jsp**“.

![SpringMVC-ParameterizableViewController-Example-1](http://www.mkyong.com/wp-content/uploads/2010/08/SpringMVC-ParameterizableViewController-Example-1.jpg)

[http://www.mkyong.com/spring-mvc/spring-mvc-parameterizableviewcontroller-example/](http://www.mkyong.com/spring-mvc/spring-mvc-parameterizableviewcontroller-example/)
