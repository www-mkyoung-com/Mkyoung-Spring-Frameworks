In Spring 3, you can enable “[mvc:annotation-driven](http://static.springsource.org/spring/docs/3.0.x/spring-framework-reference/html/mvc.html#mvc-annotation-driven)” to support [JSR303 bean validation](http://jcp.org/en/jsr/detail?id=303) via `@Valid` annotation, if any JSR 303 validator framework on the classpath.

**Note**  
Hibernate Validator is the reference implementation for JSR 303

In this tutorial, we show you how to integrate Hibernate validator with Spring MVC, via `@Valid` annotation, to perform bean validation in a HTML form.

Technologies used :

1.  Spring 3.0.5.RELEASE
2.  Hibernate Validator 4.2.0.Final
3.  JDK 1.6
4.  Eclipse 3.6
5.  Maven 3

## 1\. Project Dependencies

Hibernate validator is available at JBoss public repository.

    <repositories>
    	<repository>
    		<id>JBoss repository</id>
    		<url>http://repository.jboss.org/nexus/content/groups/public/</url>
    	</repository>
    </repositories>

    <properties>
    	<spring.version>3.0.5.RELEASE</spring.version>
    </properties>

    <dependencies>

    	<!-- Spring 3 dependencies -->
    	<dependency>
    		<groupId>org.springframework</groupId>
    		<artifactId>spring-core</artifactId>
    		<version>${spring.version}</version>
    	</dependency>

    	<dependency>
    		<groupId>org.springframework</groupId>
    		<artifactId>spring-web</artifactId>
    		<version>${spring.version}</version>
    	</dependency>

    	<dependency>
    		<groupId>org.springframework</groupId>
    		<artifactId>spring-webmvc</artifactId>
    		<version>${spring.version}</version>
    	</dependency>

    	<!-- Hibernate Validator -->
    	<dependency>
    		<groupId>org.hibernate</groupId>
    		<artifactId>hibernate-validator</artifactId>
    		<version>4.2.0.Final</version>
    	</dependency>

    </dependencies>

## 2\. JSR303 Bean Validation

A simple POJO, annotated with Hibernate validator annotation.

**Note**  
Refer to this [Hibernate validator documentation](http://docs.jboss.org/hibernate/validator/4.2/reference/en-US/html/) for detail explanation

    package com.mkyong.common.model;

    import org.hibernate.validator.constraints.NotEmpty;
    import org.hibernate.validator.constraints.Range;

    public class Customer {

    	@NotEmpty //make sure name is not empty
    	String name;

    	@Range(min = 1, max = 150) //age need between 1 and 150
    	int age;

    	//getter and setter methods

    }

## 3\. Controller + @Valid

For validation to work, just annotate the “JSR annotated model object” via `@Valid`. That’s all, other stuff is just a normal Spring MVC form handling.

    package com.mkyong.common.controller;

    import javax.validation.Valid;
    import org.springframework.stereotype.Controller;
    import org.springframework.ui.ModelMap;
    import org.springframework.validation.BindingResult;
    import org.springframework.web.bind.annotation.RequestMapping;
    import org.springframework.web.bind.annotation.RequestMethod;
    import com.mkyong.common.model.Customer;

    @Controller
    @RequestMapping("/customer")
    public class SignUpController {

    	@RequestMapping(value = "/signup", method = RequestMethod.POST)
    	public String addCustomer(@Valid Customer customer, BindingResult result) {

    		if (result.hasErrors()) {
    			return "SignUpForm";
    		} else {
    			return "Done";
    		}

    	}

    	@RequestMapping(method = RequestMethod.GET)
    	public String displayCustomerForm(ModelMap model) {

    		model.addAttribute("customer", new Customer());
    		return "SignUpForm";

    	}

    }

## 4\. Error Message

By default, if validation failed.

1.  `@NotEmpty` will display “may not be empty”
2.  `@Range` will display “must be between 1 and 150”

You can override it easily, create a properties with “key” and message. To know which @annotation bind to which key, just debug it and view value inside “`BindingResult result`“. Normally, the key is “**@Annotation Name.object.fieldname**“.

_File : messages.properties_

    NotEmpty.customer.name = Name is required!
    Range.customer.age = Age value must be between 1 and 150

## 5\. mvc:annotation-driven

Enable “`mvc:annotation-driven`” to make Spring MVC supports JSR303 validator via `@Valid`, and also bind your properties file.

    <beans xmlns="http://www.springframework.org/schema/beans"
    	xmlns:context="http://www.springframework.org/schema/context"
    	xmlns:mvc="http://www.springframework.org/schema/mvc"
            xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    	xsi:schemaLocation="
            http://www.springframework.org/schema/beans
            http://www.springframework.org/schema/beans/spring-beans-3.0.xsd
            http://www.springframework.org/schema/context
            http://www.springframework.org/schema/context/spring-context-3.0.xsd
            http://www.springframework.org/schema/mvc
            http://www.springframework.org/schema/mvc/spring-mvc-3.0.xsd">

    	<context:component-scan base-package="com.mkyong.common.controller" />

             <!-- support JSR303 annotation if JSR 303 validation present on classpath -->
    	<mvc:annotation-driven />

    	<bean id="viewResolver"
    		class="org.springframework.web.servlet.view.InternalResourceViewResolver">
    		<property name="prefix">
    			<value>/WEB-INF/pages/</value>
    		</property>
    		<property name="suffix">
    			<value>.jsp</value>
    		</property>
    	</bean>

            <!-- bind your messages.properties -->
    	<bean class="org.springframework.context.support.ResourceBundleMessageSource"
    		id="messageSource">
    		<property name="basename" value="messages" />
    	</bean>

    </beans>

## 6\. JSP Pages

Last one, normal JSP page with Spring form tag library.

_File : SignUpForm.jsp_

    <%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
    <html>
    <head>
    <style>
    .error {
    	color: #ff0000;
    }

    .errorblock {
    	color: #000;
    	background-color: #ffEEEE;
    	border: 3px solid #ff0000;
    	padding: 8px;
    	margin: 16px;
    }
    </style>
    </head>

    <body>
    	<h2>Customer SignUp Form - JSR303 @Valid example</h2>

    	<form:form method="POST" commandName="customer" action="customer/signup">
    		<form:errors path="*" cssClass="errorblock" element="div" />
    		<table>
    			<tr>
    				<td>Customer Name :</td>
    				<td><form:input path="name" /></td>
    				<td><form:errors path="name" cssClass="error" /></td>
    			</tr>
    			<tr>
    				<td>Customer Age :</td>
    				<td><form:input path="age" /></td>
    				<td><form:errors path="age" cssClass="error" /></td>
    			</tr>
    			<tr>
    				<td colspan="3"><input type="submit" /></td>
    			</tr>
    		</table>
    	</form:form>

    </body>
    </html>

_File : Done.jsp_

    <html>
    <body>
    	<h2>Done</h2>
    </body>
    </html>

## 6\. Demo

_URL : http://localhost:8080/SpringMVC/customer_ – Customer form page, with 2 text boxes for name and age.

![Spring MVC JSR303 demo page](http://www.mkyong.com/wp-content/uploads/2011/07/spring-mvc-bean-validation.png)

_URL : http://localhost:8080/SpringMVC/customer/signup_ – If you didn’t fill in the form and click on the “submit” button, your customized validation error messages will be displayed.

![Spring MVC JSR303 demo page - error message](http://www.mkyong.com/wp-content/uploads/2011/07/spring-mvc-bean-validation-error.png)

[http://www.mkyong.com/spring-mvc/spring-3-mvc-and-jsr303-valid-example/](http://www.mkyong.com/spring-mvc/spring-3-mvc-and-jsr303-valid-example/)
