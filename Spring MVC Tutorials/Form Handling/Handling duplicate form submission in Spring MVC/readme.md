In last [Spring MVC form handling](http://www.mkyong.com/spring-mvc/spring-mvc-form-handling-example/) example, if you refresh the form success view, most browsers will prompt a pop-up dialog to confirm about the form resubmission. If you click “yes”, the form will be resubmitted again, this scenario is well-known as duplicated form submission.

_Figure : example of duplicated form submission._

![SpringMVC-Duplicate-Form-Submit](http://www.mkyong.com/wp-content/uploads/2010/08/SpringMVC-Double-Submits.jpg)

The common solution to this is using “**Post/Redirect/Get**” Design Pattern. It will redirect to another URL if the form submission is successfully, instead of returning a web page directly.

**Note**  
Check the details explanation of [Post/Redirect/Get Design Pattern in Wiki](http://en.wikipedia.org/wiki/Post/Redirect/Get).

## Post/Redirect/Get Design Pattern in Spring MVC

In this tutorial, we show you how to apply the “**Post/Redirect/Get**” Design Pattern in Spring MVC to solve the duplicated form submission problem in [last form handling](http://www.mkyong.com/spring-mvc/spring-mvc-form-handling-example/) example.

## 1\. Duplicate form submission

See below normal form declaration that will hits the duplicate form submission problem.

_File : mvc-dispatcher-servlet.xml_

    <bean
    class="org.springframework.web.servlet.mvc.support.ControllerClassNameHandlerMapping" />

       <bean class="com.mkyong.customer.controller.CustomerController">
    	<property name="formView" value="CustomerForm" />
    	<property name="successView" value="CustomerSuccess" />
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

In above snippet, the `CustomerController` returns a “**CustomerSuccess**” view directly, which should be **replace with a redirect URL** instead.

## 2\. Redirect View

Declared a review view, named “**customerSuccessRedirect**” and return an URL “**CustomerSuccess.htm**“.

_File : spring-views.xml_

    <beans ...>
       <!-- Redirect view -->
       <bean id="customerSuccessRedirect"
           class="org.springframework.web.servlet.view.RedirectView">
           <property name="url" value="CustomerSuccess.htm" />
        </bean>
    </beans>

## 3\. Spring Configuration

Update the **mvc-dispatcher-servlet.xml** settings to link all Spring’s configuration together.

1.  Update the “**successView**” to the new redirect view, named “**customerSuccessRedirect**“.
2.  Declare a “**XmlViewResolver**” to load the redirect view.
3.  Put a priority order for the “**InternalResourceViewResolver**” and “**XmlViewResolver**“, otherwise the “**InternalResourceViewResolver**” will always match and give your application no chance to call the “**XmlViewResolver**“.
4.  Declare a “**ParameterizableViewController**” controller to match the redirect URL and return a view to user. Since the “**ControllerClassNameHandlerMapping**” won’t generated the mapping for any build-in Spring’s controller, so you have to define the explicit mapping in “**SimpleUrlHandlerMapping**“.

_File : mvc-dispatcher-servlet.xml_

    <bean
    class="org.springframework.web.servlet.mvc.support.ControllerClassNameHandlerMapping" />

       <bean class="com.mkyong.customer.controller.CustomerController">
    	<property name="formView" value="CustomerForm" />
    	<property name="successView" value="customerSuccessRedirect" />

    	<!-- it was
    	<property name="successView" value="CustomerSuccess" />
    	-->
       </bean>

       <!-- Redirect Controller -->
       <bean class="org.springframework.web.servlet.handler.SimpleUrlHandlerMapping">
            <property name="mappings">
                <props>
                    <prop key="/CustomerSuccess.htm">customerSuccessController</prop>
                </props>
            </property>
       </bean>

       <bean id="customerSuccessController"
            class="org.springframework.web.servlet.mvc.ParameterizableViewController">
           <property name="viewName" value="CustomerSuccess" />
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

## 4\. How it works?

1\. Access URL : **http://localhost:8080/SpringMVC/customer.htm**.

2\. Fill in and submits the form.

3\. Return “successView”, which is “**customerSuccessRedirect**“.

    <bean class="com.mkyong.customer.controller.CustomerController">
    <property name="formView" value="CustomerForm" />
    <property name="successView" value="customerSuccessRedirect" />
       </bean>

4\. “XmlViewResolver” match it and return a “RedirectView” with URL “**CustomerSuccess.htm**“.

    <bean id="customerSuccessRedirect"
        class="org.springframework.web.servlet.view.RedirectView">
        <property name="url" value="CustomerSuccess.htm" />
     </bean>

5\. “SimpleUrlHandlerMapping” match it and return a ParameterizableViewController, “**customerSuccessController**“, and return the view name “**CustomerSuccess**“.

    <bean class="org.springframework.web.servlet.handler.SimpleUrlHandlerMapping">
         <property name="mappings">
             <props>
                 <prop key="/CustomerSuccess.htm">customerSuccessController</prop>
             </props>
         </property>
    </bean>
    <bean id="customerSuccessController"
         class="org.springframework.web.servlet.mvc.ParameterizableViewController">
        <property name="viewName" value="CustomerSuccess" />
    </bean>

6\. “InternalResourceViewResolver” match it and return the final view “**/WEB-INF/pages/CustomerSuccess.jsp**“.

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

7\. URL changed to **http://localhost:8080/SpringMVC/CustomerSuccess.htm**.

8\. Try refresh the success form page , the form resubmission dialog will not prompt anymore.

**Note **  
The overall concept is return a redirect URL instead of a direct page.

[http://www.mkyong.com/spring-mvc/handling-duplicate-form-submission-in-spring-mvc/](http://www.mkyong.com/spring-mvc/handling-duplicate-form-submission-in-spring-mvc/)
