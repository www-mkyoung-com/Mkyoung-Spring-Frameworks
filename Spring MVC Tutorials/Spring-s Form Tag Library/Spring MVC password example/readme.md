In Spring MVC, you can use **<form:password />** tag to render a HTML password field. For example,

    <form:password path="password" />

It will renders following HTML code

    <input id="password" name="password" type="password" value=""/>

**Note**  
In Spring’s documentation, it mention about the ‘**showPassword**‘ attribute will display the password value, but it’s failed in my testing, may be you can try it yourself.

In this tutorial, we show you how to use Spring’s form tag “**password**” to **render two HTML password** fields – “password” and “confirmPassword”. Additionally, add a validator checks on both password fields : must not be blank, and the “password” field must match with “confirmPasswod” field.

## 1\. Controller

A **SimpleFormController** to handle the form value.

_File : PasswordController.java_

    package com.mkyong.customer.controller;

    import javax.servlet.http.HttpServletRequest;
    import javax.servlet.http.HttpServletResponse;

    import org.springframework.validation.BindException;
    import org.springframework.web.servlet.ModelAndView;
    import org.springframework.web.servlet.mvc.SimpleFormController;
    import com.mkyong.customer.model.Customer;

    public class PasswordController extends SimpleFormController{

    	public PasswordController(){
    		setCommandClass(Customer.class);
    		setCommandName("customerForm");
    	}

    	@Override
    	protected ModelAndView onSubmit(HttpServletRequest request,
    			HttpServletResponse response, Object command, BindException errors)
    			throws Exception {

    		Customer customer = (Customer)command;
    		return new ModelAndView("CustomerSuccess","customer",customer);

    	}

    }

## 2\. Model

A Customer object to store the password value.

_File : Customer.java_

    package com.mkyong.customer.model;

    public class Customer{

    	String password;
    	String confirmPassword;
    	//getter and setter methods for password and confirmPassword
    }

## 3\. Form Validator

Create a password validator class to check on the both password fields : must not be blank, “password” and “confirmPassword” must be match. Otherwise, get the corresponds message from the resource bundle (properties file).

_File : PasswordValidator.java_

    package com.mkyong.customer.validator;

    import org.springframework.validation.Errors;
    import org.springframework.validation.ValidationUtils;
    import org.springframework.validation.Validator;

    import com.mkyong.customer.model.Customer;

    public class PasswordValidator implements Validator{

    	@Override
    	public boolean supports(Class clazz) {
    		//just validate the Customer instances
    		return Customer.class.isAssignableFrom(clazz);
    	}

    	@Override
    	public void validate(Object target, Errors errors) {

    		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password",
    			"required.password", "Field name is required.");

    		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "confirmPassword",
    				"required.confirmPassword", "Field name is required.");

    		Customer cust = (Customer)target;

    		if(!(cust.getPassword().equals(cust.getConfirmPassword()))){
    			errors.rejectValue("password", "notmatch.password");
    		}

    	}

    }

_File : message.properties_

    required.password = Password is required!
    required.passwordConfirm = Confirm password is required!
    notmatch.password = Password and Conform password is not match!

## 4\. View

A JSP page to use the Spring’s form tag “**password**” to render two HTML password fields, and put some CSS styles to highlight the error message.

_File : CustomerForm.jsp_

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
    	<h2>Spring's form password example</h2>

    	<form:form method="POST" commandName="customerForm">
    		<form:errors path="*" cssClass="errorblock" element="div" />
    		<table>
    			<tr>
    				<td>Password :</td>
    				<td><form:password path="password" />
    				</td>
    				<td><form:errors path="password" cssClass="error" />
    				</td>
    			</tr>
    			<tr>
    				<td>Confirm Password :</td>
    				<td><form:password path="confirmPassword" />
    				</td>
    				<td><form:errors path="confirmPassword" cssClass="error" />
    				</td>
    			</tr>
    			<tr>
    				<td colspan="3"><input type="submit" />
    				</td>
    			</tr>
    		</table>
    	</form:form>

    </body>
    </html>

If the form is submitted, render the successful page and display the submitted password value.

_File : CustomerSuccess.jsp_

    <html>
    <body>
    	<h2>Spring's form password example</h2>

    	Password : ${customer.password}
    	<br /> Confirm Password : ${customer.confirmPassword}

    </body>
    </html>

## 5\. Spring Bean Configuration

Link it all ~

    <beans xmlns="http://www.springframework.org/schema/beans"
    	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    	xsi:schemaLocation="http://www.springframework.org/schema/beans
    	http://www.springframework.org/schema/beans/spring-beans-2.5.xsd">

      <bean
      class="org.springframework.web.servlet.mvc.support.ControllerClassNameHandlerMapping" />

    	<bean class="com.mkyong.customer.controller.PasswordController">
    		<property name="formView" value="CustomerForm" />
    		<property name="successView" value="CustomerSuccess" />

    		<!-- Map a validator -->
    		<property name="validator">
    			<bean class="com.mkyong.customer.validator.PasswordValidator" />
    		</property>
    	</bean>

    	<!-- Register the Customer.properties -->
    	<bean id="messageSource"
    		class="org.springframework.context.support.ResourceBundleMessageSource">
    		<property name="basename" value="message" />
    	</bean>

    	<bean id="viewResolver"
    		class="org.springframework.web.servlet.view.InternalResourceViewResolver">
    		<property name="prefix">
    			<value>/WEB-INF/pages/</value>
    		</property>
    		<property name="suffix">
    			<value>.jsp</value>
    		</property>
    	</bean>

    </beans>

## 6\. Demo

Access the page – **http://localhost:8080/SpringMVCForm/password.htm**

![SpringMVC-Password-Example-1](http://www.mkyong.com/wp-content/uploads/2010/08/SpringMVC-Password-Example-1.jpg)

If the “password” is not match the “confirmPassword” while submitting the form, display and highlight the error message.

![SpringMVC-Password-Example-2](http://www.mkyong.com/wp-content/uploads/2010/08/SpringMVC-Password-Example-2.jpg)

If the form is submitted successfully, just display the submitted password value.

![SpringMVC-Password-Example-3](http://www.mkyong.com/wp-content/uploads/2010/08/SpringMVC-Password-Example-3.jpg)

[http://www.mkyong.com/spring-mvc/spring-mvc-password-example/](http://www.mkyong.com/spring-mvc/spring-mvc-password-example/)
