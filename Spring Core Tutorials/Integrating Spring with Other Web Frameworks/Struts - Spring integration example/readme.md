Here’s a tutorial to show how to access beans declared in the Spring Ioc container in a web application developed with Apache Struts 1.x.

Spring comes with “Struts-specific” solution for access beans declared in the Spring Ioc container.

1.  Register a Spring’s ready-make Struts plug-in in the Struts configuration file.
2.  Change your Struts action class to extend the Spring’s **ActionSupport** class, a subclass of the Struts Action class.
3.  The **ActionSupport** provide a convenient **getWebApplicationContext()** method for you to access beans declared in Spring Ioc container.

## 1\. Struts + Spring dependencies

To integrate with Struts 1.x, Spring is required the “**spring-web.jar**” and “**spring-struts.jar**” libraries. You can download it from Spring web site or Maven.  
**pom.xml**

    <!-- Spring framework -->
    <dependency>
    	<groupId>org.springframework</groupId>
    	<artifactId>spring</artifactId>
    	<version>2.5.6</version>
    </dependency>

            <dependency>
    	<groupId>org.springframework</groupId>
    	<artifactId>spring-web</artifactId>
    	<version>2.5.6</version>
    </dependency>

    <dependency>
    	<groupId>org.springframework</groupId>
    	<artifactId>spring-struts</artifactId>
    	<version>2.0.8</version>
    </dependency>

## 2\. Register Struts plug-in

In your Struts configuration file (struts-config.xml), register the Spring’s ready-make Struts plug-in – “** ContextLoaderPlugIn**“.

**struts-config.xml**

    <struts-config>
        <!-- Spring Struts plugin -->
     	<plug-in className="org.springframework.web.struts.ContextLoaderPlugIn">
    		<set-property property="contextConfigLocation"
    			value="/WEB-INF/classes/SpringBeans.xml" />
      	</plug-in>
    </struts-config>

The “**ContextLoaderPlugIn**” will handle all the integration work between Struts and Spring. You can load your Spring’s bean xml file into the “**contextConfigLocation**” property.

**SpringBeans.xml**

    <beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://www.springframework.org/schema/beans
    http://www.springframework.org/schema/beans/spring-beans-2.5.xsd">

    	<!-- Beans Declaration -->
    	<import resource="com/mkyong/customer/spring/CustomerBean.xml"/>

    </beans>

## 3\. Spring’s ActionSupport

In Struts Action class, extends the Spring “**ActionSupport**” class, and get the Spring’s bean via “**getWebApplicationContext()**” method.

**CustomerBean.xml**

    <bean id="customerBo" class="com.mkyong.customer.bo.impl.CustomerBoImpl" >
    	<property name="customerDao" ref="customerDao" />
    </bean>

**Struts Action**

    package com.mkyong.customer.action;

    import java.util.List;

    import javax.servlet.http.HttpServletRequest;
    import javax.servlet.http.HttpServletResponse;

    import org.apache.struts.action.ActionForm;
    import org.apache.struts.action.ActionForward;
    import org.apache.struts.action.ActionMapping;
    import org.springframework.web.struts.ActionSupport;

    import com.mkyong.customer.bo.CustomerBo;
    import com.mkyong.customer.model.Customer;

    public class ListCustomerAction extends ActionSupport{

      public ActionForward execute(ActionMapping mapping,ActionForm form,
    	HttpServletRequest request,HttpServletResponse response)
            throws Exception {

    	CustomerBo customerBo =
    		(CustomerBo) getWebApplicationContext().getBean("customerBo");

    	...
    	return mapping.findForward("success");

      }
    }

Done.

[http://www.mkyong.com/struts/struts-spring-integration-example/](http://www.mkyong.com/struts/struts-spring-integration-example/)
