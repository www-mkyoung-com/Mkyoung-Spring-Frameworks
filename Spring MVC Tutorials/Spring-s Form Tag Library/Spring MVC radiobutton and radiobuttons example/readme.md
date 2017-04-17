In Spring MVC, **<form:radiobutton />** is used to render a HTML radio button, and the radio button values are hard coded inside the JSP page; While the **<form:radiobuttons />** is used to render multiple radio buttons, and the radio button values are generated at runtime.

In this tutorial, we show you how to use **<form:radiobutton />** and **<form:radiobuttons />**.

## 1\. <form:radiobutton />

Generate a radio button, and hard-coded the value.

    public class Customer{
    	String sex;
    	//...
    }

    <form:radiobutton path="sex" value="M"/>Male
    <form:radiobutton path="sex" value="F"/>Female

**Default value…**  
To make the “**Male**” as the default selected value in above radio buttons, just set the “**sex**” property to “**M**“, for example :

    public class Customer{
    	String sex = "M";
    	//...
    }

or

    //SimpleFormController...
    @Override
    protected Object formBackingObject(HttpServletRequest request)
    	throws Exception {

    	Customer cust = new Customer();
    	//Make "Male" as the default radio button selected value
    	cust.setSex("M");

    	return cust;

    }

## 2\. <form:radiobuttons />

Generate multiple radio buttons, and the values are generated at runtime.

    //SimpleFormController...
    protected Map referenceData(HttpServletRequest request) throws Exception {

    	Map referenceData = new HashMap();

    	List<String> numberList = new ArrayList<String>();
    	numberList.add("Number 1");
    	numberList.add("Number 2");
    	numberList.add("Number 3");
    	numberList.add("Number 4");
    	numberList.add("Number 5");
    	referenceData.put("numberList", numberList);

    	return referenceData;
    }

    <form:radiobuttons path="favNumber" items="${numberList}"  />

**Default value…**  
To make the “**Number 1**” as the default selected value in above radio buttons, just set the “**favNumber**” property to “**Number 1**“, for example :

    public class Customer{
    	String favNumber = "Number 1";
    	//...
    }

or

    //SimpleFormController...
    @Override
    protected Object formBackingObject(HttpServletRequest request)
    	throws Exception {

    	Customer cust = new Customer();
    	//Make "Number 1" as the default radio button selected value
    	cust.setFavNumber("Number 1")

    	return cust;
    }

**Note**  
For radio button selection, as long as the “**path**” or “**property**” is equal to the “**radio button value**“, the radio button will be selected automatically.

## Full radio button example

Let’s go thought a complete Spring MVC radio button example :

## 1\. Model

A customer model class to store the radio button value.

_File : Customer.java_

    package com.mkyong.customer.model;

    public class Customer{

    	String favNumber;
    	String sex;

    	public String getFavNumber() {
    		return favNumber;
    	}
    	public void setFavNumber(String favNumber) {
    		this.favNumber = favNumber;
    	}
    	public String getSex() {
    		return sex;
    	}
    	public void setSex(String sex) {
    		this.sex = sex;
    	}

    }

## 2\. Controller

A `SimpleFormController` to handle the form radio button value. Make the radio button “M” as the default selected value.

_File : RadioButtonController.java_

    package com.mkyong.customer.controller;

    import java.util.ArrayList;
    import java.util.HashMap;
    import java.util.List;
    import java.util.Map;

    import javax.servlet.http.HttpServletRequest;
    import javax.servlet.http.HttpServletResponse;
    import org.springframework.validation.BindException;
    import org.springframework.web.servlet.ModelAndView;
    import org.springframework.web.servlet.mvc.SimpleFormController;
    import com.mkyong.customer.model.Customer;

    public class RadioButtonController extends SimpleFormController{

    	public RadioButtonController(){
    		setCommandClass(Customer.class);
    		setCommandName("customerForm");
    	}

    	@Override
    	protected Object formBackingObject(HttpServletRequest request)
    		throws Exception {

    		Customer cust = new Customer();
    		//Make "Make" as default radio button checked value
    		cust.setSex("M");

    		return cust;
    	}

    	@Override
    	protected ModelAndView onSubmit(HttpServletRequest request,
    		HttpServletResponse response, Object command, BindException errors)
    		throws Exception {

    		Customer customer = (Customer)command;
    		return new ModelAndView("CustomerSuccess","customer",customer);
    	}

    	protected Map referenceData(HttpServletRequest request) throws Exception {

    		Map referenceData = new HashMap();

    		List<String> numberList = new ArrayList<String>();
    		numberList.add("Number 1");
    		numberList.add("Number 2");
    		numberList.add("Number 3");
    		numberList.add("Number 4");
    		numberList.add("Number 5");
    		referenceData.put("numberList", numberList);

    		return referenceData;
    	}
    }

## 3\. Validator

A simple form validator to make sure the “**sex**” and “**number**” radio button is selected.

_File : RadioButtonValidator.java_

    package com.mkyong.customer.validator;

    import org.springframework.validation.Errors;
    import org.springframework.validation.ValidationUtils;
    import org.springframework.validation.Validator;

    import com.mkyong.customer.model.Customer;

    public class RadioButtonValidator implements Validator{

    	@Override
    	public boolean supports(Class clazz) {
    		//just validate the Customer instances
    		return Customer.class.isAssignableFrom(clazz);
    	}

    	@Override
    	public void validate(Object target, Errors errors) {

    		Customer cust = (Customer)target;

    		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "sex", "required.sex");
    		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "favNumber", "required.favNumber");

    	}
    }

_File : message.properties_

    required.sex = Please select a sex!
    required.favNumber = Please select a number!

## 4\. View

A JSP page to show the use of Spring’s form tag **<form:radiobutton />** and **<form:radiobuttons />**.

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
    	<h2>Spring's form radio button example</h2>

    	<form:form method="POST" commandName="customerForm">
    		<form:errors path="*" cssClass="errorblock" element="div" />
    		<table>
    			<tr>
    				<td>Sex :</td>
    				<td><form:radiobutton path="sex" value="M" />Male <form:radiobutton
    					path="sex" value="F" />Female</td>
    				<td><form:errors path="sex" cssClass="error" /></td>
    			</tr>
    			<tr>
    				<td>Choose a number :</td>
    				<td><form:radiobuttons path="favNumber" items="${numberList}" />
                                    </td>
    				<td><form:errors path="favNumber" cssClass="error" /></td>
    			</tr>
    			<tr>
    				<td colspan="3"><input type="submit" /></td>
    			</tr>
    		</table>
    	</form:form>

    </body>
    </html>

Use JSTL to loop over the submitted radio button values, and display it.

_File : CustomerSuccess.jsp_

    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

    <html>
    <body>
    	<h2>Spring's form radio button example</h2>
    	Sex : ${customer.sex}
    	<br /> Favourite Number : ${customer.favNumber}
    	<br />
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

    	<bean class="com.mkyong.customer.controller.RadioButtonController">
    		<property name="formView" value="CustomerForm" />
    		<property name="successView" value="CustomerSuccess" />

    		<!-- Map a validator -->
    		<property name="validator">
    			<bean class="com.mkyong.customer.validator.RadioButtonValidator" />
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

Access the page – **http://localhost:8080/SpringMVCForm/radiobutton.htm**

![SpringMVC-RadioButton-Example-1](http://www.mkyong.com/wp-content/uploads/2010/08/SpringMVC-RadioButton-Example-1.jpg)

If the user did not select any radio button value while submitting the form, display and highlight the error message.

![SpringMVC-RadioButton-Example-2](http://www.mkyong.com/wp-content/uploads/2010/08/SpringMVC-RadioButton-Example-2.jpg)

If the form is submitted successfully, just display the submitted radio button values.

![SpringMVC-RadioButton-Example-3](http://www.mkyong.com/wp-content/uploads/2010/08/SpringMVC-RadioButton-Example-3.jpg)

[http://www.mkyong.com/spring-mvc/spring-mvc-radiobutton-and-radiobuttons-example/](http://www.mkyong.com/spring-mvc/spring-mvc-radiobutton-and-radiobuttons-example/)
