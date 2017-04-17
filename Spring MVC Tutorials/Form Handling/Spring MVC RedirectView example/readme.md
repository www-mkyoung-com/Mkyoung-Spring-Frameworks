In Spring MVC, org.springframework.web.servlet.view.RedirectView, as name indicated, a view redirect to another absolute, context relative, or current request relative URL. In this tutorial, we show you a complete example to use **RedirectView** class.

## 1\. RedirectView

Declare a RedirectView bean, named “**DummyRedirect**“, redirect to URL “**DummyRedirectPage.htm**“.

_File : spring-views.xml_

    <beans ...>
       <!-- Redirect view -->
       <bean id="DummyRedirect"
    	   class="org.springframework.web.servlet.view.RedirectView">
               <property name="url" value="DummyRedirectPage.htm" />
        </bean>
    </beans>

## 2\. Controller

A controller to return a ModelAndView named “**DummyRedirect**“, which is a RedirectView view.

_File : DummyController.java_

    package com.mkyong.common.controller;

    import javax.servlet.http.HttpServletRequest;
    import javax.servlet.http.HttpServletResponse;
    import org.springframework.web.servlet.ModelAndView;
    import org.springframework.web.servlet.mvc.AbstractController;

    public class DummyController extends AbstractController{

    	@Override
    	protected ModelAndView handleRequestInternal(HttpServletRequest request,
    		HttpServletResponse response) throws Exception {

    		return new ModelAndView("DummyRedirect");

    	}
    }

## 3\. Spring configuration

Declared all mappings.

_File : mvc-dispatcher-servlet.xml_

    <beans ...>

      <bean
      class="org.springframework.web.servlet.mvc.support.ControllerClassNameHandlerMapping" />

       <bean class="com.mkyong.common.controller.DummyController" />

       <bean class="org.springframework.web.servlet.handler.SimpleUrlHandlerMapping">
           <property name="mappings">
                <props>
                    <prop key="/DummyRedirectPage.htm">dummyRedirectController</prop>
                </props>
            </property>
        </bean>

        <bean id="dummyRedirectController"
             class="org.springframework.web.servlet.mvc.ParameterizableViewController">
    	 <property name="viewName" value="DummyPage" />
        </bean>

        <bean id="viewResolver"
              class="org.springframework.web.servlet.view.InternalResourceViewResolver" >
              <property name="prefix">
                    <value>/WEB-INF/pages/</value>
               </property>
              <property name="suffix">
                    <value>.jsp</value>
              </property>
              <property name="order" value="1" />
         </bean>

         <bean class="org.springframework.web.servlet.view.XmlViewResolver">
    	   <property name="location">
    	       <value>/WEB-INF/spring-views.xml</value>
    	   </property>
    	   <property name="order" value="0" />
         </bean>

    </beans>

## 4\. How it works?

1\. Access the URL **http://localhost:8080/SpringMVC/dummy.htm**.

2\. “ControllerClassNameHandlerMapping” matched “**DummyController**” and return a **ModelAndView(“DummyRedirect”)**.

3\. “**XmlViewResolver**” matched it and return an URL “**DummyRedirectPage.htm**“.

    <bean id="DummyRedirect"
    	   class="org.springframework.web.servlet.view.RedirectView">
       <property name="url" value="DummyRedirectPage.htm" />
    </bean>

4\. “**SimpleUrlHandlerMapping**” matched it and return a controller “**dummyRedirectController**“.

    <bean class="org.springframework.web.servlet.handler.SimpleUrlHandlerMapping">
        <property name="mappings">
            <props>
                <prop key="/DummyRedirectPage.htm">dummyRedirectController</prop>
            </props>
        </property>
    </bean>

5\. The ParameterizableViewController controller, “**dummyRedirectController**“, return a view named “**DummyPage**“.

    <bean id="dummyRedirectController"
            class="org.springframework.web.servlet.mvc.ParameterizableViewController">
    <property name="viewName" value="DummyPage" />
        </bean>

6\. InternalResourceViewResolver matche it and return the final jsp page, “**/WEB-INF/pages/DummyPage.jsp**“.

    <bean id="viewResolver"
    	   class="org.springframework.web.servlet.view.InternalResourceViewResolver" >
          <property name="prefix">
              <value>/WEB-INF/pages/</value>
           </property>
          <property name="suffix">
             <value>.jsp</value>
          </property>
          <property name="order" value="1" />
    </bean>

7\. URL changed to “**http://localhost:8080/SpringMVC/DummyRedirectPage.htm**“.

**Redirect Prefix**  
Alternative, if you have InternalResourceViewResolver configured, you can use the “**Redirect Prefix**” in the view name to resolve the redirect view. For example,

_File : DummyController.java_

    //...
    public class DummyController extends AbstractController{

    	@Override
    	protected ModelAndView handleRequestInternal(HttpServletRequest request,
    		HttpServletResponse response) throws Exception {

    		return new ModelAndView("redirect:DummyRedirectPage.htm");

    	}
    }

**Use case**  
One of the use case is applying the “RedirectView” concept to solve the [duplicated form submission in Spring MVC](http://www.mkyong.com/spring-mvc/handling-duplicate-form-submission-in-spring-mvc/).

[http://www.mkyong.com/spring-mvc/spring-mvc-redirectview-example/](http://www.mkyong.com/spring-mvc/spring-mvc-redirectview-example/)
