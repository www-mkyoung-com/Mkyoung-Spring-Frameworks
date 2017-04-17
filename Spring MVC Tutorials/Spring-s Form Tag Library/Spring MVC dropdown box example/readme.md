In Spring MVC, form tags – **<form:select />**, **<form:option />** or **<form:options />**, are used to render HTML dropdown box. See following examples :

    //SimpleFormController
    protected Map referenceData(HttpServletRequest request) throws Exception {
    	Map referenceData = new HashMap();
    	Map<String,String> country = new LinkedHashMap<String,String>();
    	country.put("US", "United Stated");
    	country.put("CHINA", "China");
    	country.put("SG", "Singapore");
    	country.put("MY", "Malaysia");
    	referenceData.put("countryList", country);
    }

## 1\. <form:select />

Generate a dropbox box with ${countryList}.

    <form:select path="country" items="${countryList}" />

**HTML code**

    <select id="country" name="country">
       <option value="US">United Stated</option>
       <option value="CHINA">China</option>
       <option value="SG">Singapore</option>
       <option value="MY">Malaysia</option>
    </select>

## 2\. <form:options />

The <form:options /> have to enclosed with the select tag.

    <form:select path="country">
        <form:options items="${countryList}" />
    </form:select>

**HTML code**

    <select id="country" name="country">
       <option value="US">United Stated</option>
       <option value="CHINA">China</option>
       <option value="SG">Singapore</option>
       <option value="MY">Malaysia</option>
    </select>

## 3\. <form:option />

The **<form:option />** have to enclosed with the select tag as well, and render a single select option, see the following combination.

    <form:select path="country">
       <form:option value="NONE" label="--- Select ---"/>
       <form:options items="${countryList}" />
    </form:select>

**HTML code**

    <select id="country" name="country">
       <option value="NONE">--- Select ---</option>
       <option value="US">United Stated</option>
       <option value="CHINA">China</option>
       <option value="SG">Singapore</option>
       <option value="MY">Malaysia</option>
    </select>

## 4\. List box

To render a list box, just add the “**multiple=true**” attribute in the select tag.

    <form:select path="country" items="${countryList}" multiple="true" />

**HTML code**, with a hidden value to handle the country selections.

    <select id="country" name="country" multiple="multiple">
        <option value="US">United Stated</option>
        <option value="CHINA">China</option>
        <option value="SG">Singapore</option>
        <option value="MY">Malaysia</option>
    </select>
    <input type="hidden" name="_country" value="1"/>

**Select a dropdown box value**  
For dropdown box, list box or “select” options, as long as the “**path**” or “**property**” is equal to the “**select option key value**“, the options will be selected automatically.

## Full dropdown box example

Let’s go thought a complete Spring MVC dropdown box example :

## 1\. Model

A customer model class to store the dropdown box value.

_File : Customer.java_

    package com.mkyong.customer.model;

    public class Customer{

    	String country;
    	String javaSkills;

    	public String getCountry() {
    		return country;
    	}
    	public void setCountry(String country) {
    		this.country = country;
    	}
    	public String getJavaSkills() {
    		return javaSkills;
    	}
    	public void setJavaSkills(String javaSkills) {
    		this.javaSkills = javaSkills;
    	}
    }

## 2\. Controller

A `SimpleFormController` to handle the form dropdown box value. Make the java skills’s “**Spring**” as the default dropdown box selected value.

_File : DropDownBoxController.java_

    package com.mkyong.customer.controller;

    import java.util.HashMap;
    import java.util.LinkedHashMap;
    import java.util.Map;
    import javax.servlet.http.HttpServletRequest;
    import javax.servlet.http.HttpServletResponse;
    import org.springframework.validation.BindException;
    import org.springframework.web.servlet.ModelAndView;
    import org.springframework.web.servlet.mvc.SimpleFormController;
    import com.mkyong.customer.model.Customer;

    public class DropDownBoxController extends SimpleFormController{

    	public DropDownBoxController(){
    		setCommandClass(Customer.class);
    		setCommandName("customerForm");
    	}

    	@Override
    	protected Object formBackingObject(HttpServletRequest request)
    		throws Exception {

    		Customer cust = new Customer();

    		//make "Spring" as the default java skills selection
    		cust.setJavaSkills("Spring");

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

    		Map<String,String> country = new LinkedHashMap<String,String>();
    		country.put("US", "United Stated");
    		country.put("CHINA", "China");
    		country.put("SG", "Singapore");
    		country.put("MY", "Malaysia");
    		referenceData.put("countryList", country);

    		Map<String,String> javaSkill = new LinkedHashMap<String,String>();
    		javaSkill.put("Hibernate", "Hibernate");
    		javaSkill.put("Spring", "Spring");
    		javaSkill.put("Apache Wicket", "Apache Wicket");
    		javaSkill.put("Struts", "Struts");
    		referenceData.put("javaSkillsList", javaSkill);

    		return referenceData;
    	}
    }

## 3\. Validator

A simple form validator to make sure the “**country**” and “**javaSkills**” dropdown box is selected.

_File : DropDownBoxValidator.java_

    package com.mkyong.customer.validator;

    import org.springframework.validation.Errors;
    import org.springframework.validation.ValidationUtils;
    import org.springframework.validation.Validator;

    import com.mkyong.customer.model.Customer;

    public class DropDownBoxValidator implements Validator{

    	@Override
    	public boolean supports(Class clazz) {
    	   //just validate the Customer instances
    	   return Customer.class.isAssignableFrom(clazz);
    	}

    	@Override
    	public void validate(Object target, Errors errors) {

    	   Customer cust = (Customer)target;

    	   ValidationUtils.rejectIfEmptyOrWhitespace(errors, "javaSkills", "required.javaSkills");

    	   if("NONE".equals(cust.getCountry())){
    		errors.rejectValue("country", "required.country");
    	   }
    	}
    }

_File : message.properties_

    required.country = Please select a country!
    required.javaSkills = Please select a java Skill!

## 4\. View

A JSP page to show the use of Spring’s form tag **<form:select />**, **<form:option />** and **<form:options />**.

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
    	<h2>Spring's form select, option, options example</h2>

    	<form:form method="POST" commandName="customerForm">
    		<form:errors path="*" cssClass="errorblock" element="div" />
    		<table>

    			<tr>
    				<td>Country :</td>
    				<td><form:select path="country">
    					  <form:option value="NONE" label="--- Select ---" />
    					  <form:options items="${countryList}" />
    				       </form:select>
                                    </td>
    				<td><form:errors path="country" cssClass="error" /></td>
    			</tr>
    			<tr>
    				<td>Java Skills :</td>
    				<td><form:select path="javaSkills" items="${javaSkillsList}"
    					multiple="true" /></td>
    				<td><form:errors path="javaSkills" cssClass="error" /></td>
    			</tr>

    			<tr>
    				<td colspan="3"><input type="submit" /></td>
    			</tr>
    		</table>
    	</form:form>

    </body>
    </html>

Use JSTL to display submitted value.

_File : CustomerSuccess.jsp_

    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

    <html>
    <body>
    	<h2>Spring's form select, option, options example</h2>

    	Country : ${customer.country}
    	<br /> Java Skills : ${customer.javaSkills}
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

    	<bean class="com.mkyong.customer.controller.DropDownBoxController">
    		<property name="formView" value="CustomerForm" />
    		<property name="successView" value="CustomerSuccess" />

    		<!-- Map a validator -->
    		<property name="validator">
    			<bean class="com.mkyong.customer.validator.DropDownBoxValidator" />
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

Access the page – **http://localhost:8080/SpringMVCForm/dropdownbox.htm**

![SpringMVC-DropDownBox-Example-1](http://www.mkyong.com/wp-content/uploads/2010/08/SpringMVC-DropDownBox-Example-1.jpg)

If the user did not select any dropdown box value while submitting the form, display and highlight the error message.

![SpringMVC-DropDownBox-Example-2](http://www.mkyong.com/wp-content/uploads/2010/08/SpringMVC-DropDownBox-Example-2.jpg)

If the form is submitted successfully, just display the submitted dropdown box values.

![SpringMVC-DropDownBox-Example-3](http://www.mkyong.com/wp-content/uploads/2010/08/SpringMVC-DropDownBox-Example-3.jpg)

[http://www.mkyong.com/spring-mvc/spring-mvc-dropdown-box-example/](http://www.mkyong.com/spring-mvc/spring-mvc-dropdown-box-example/)
