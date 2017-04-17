In Spring MVC, **<form:checkbox />** is used to render a HTML checkbox field, the checkbox values are hard-coded inside the JSP page; While the **<form:checkboxes />** is used to render multiple checkboxes, the checkbox values are generated at runtime.

In this tutorial, we show you 3 different ways of render HTML checkbox fields:

## 1\. <form:checkbox /> – Single Checkbox

Generate a classic single checkbox, with a boolean value.

    public class Customer{
    	boolean receiveNewsletter;
    	//...
    }

    <form:checkbox path="receiveNewsletter" />

**Checked by default…**  
If you set the “**receiveNewsletter**” boolean value to true, this checkbox will be checked. For example :

    public class Customer{
    	boolean receiveNewsletter = true;
    	//...
    }

## 2\. <form:checkbox /> – Multiple Checkboxes

Generate multiple checkboxes and hard-coded the value.

    public class Customer{
    	String [] favLanguages;
    	//...
    }

    <form:checkbox path="favLanguages" value="Java"/>Java
    <form:checkbox path="favLanguages" value="C++"/>C++
    <form:checkbox path="favLanguages" value=".Net"/>.Net

**Checked by default…**  
If you want to make the checkbox with value “Java” is checked by default, you can initialize the “**favLanguages**” property with value “Java”. For example :

    //SimpleFormController...
            @Override
    protected Object formBackingObject(HttpServletRequest request)
    	throws Exception {

    	Customer cust = new Customer();
    	cust.setFavLanguages(new String []{"Java"});

    	return cust;

    }

## 3\. <form:checkboxes /> – Multiple Checkboxes

Generate a runtime list for the checkboxes value, and link it to Spring’s form tag **<form:checkboxes>**.

    //SimpleFormController...
    protected Map referenceData(HttpServletRequest request) throws Exception {

    	Map referenceData = new HashMap();
    	List<String> webFrameworkList = new ArrayList<String>();
    	webFrameworkList.add("Spring MVC");
    	webFrameworkList.add("Struts 1");
    	webFrameworkList.add("Struts 2");
    	webFrameworkList.add("Apache Wicket");
    	referenceData.put("webFrameworkList", webFrameworkList);

    	return referenceData;
    }

    <form:checkboxes items="${webFrameworkList}" path="favFramework" />

**Checked by default…**  
If you want to make 2 checkboxes with value “Spring MVC” and “Struts 2” are checked by default, you can initialize the “**favFramework**” property with value “Spring MVC” and “Struts 2”. Fro example :

    //SimpleFormController...
            @Override
    protected Object formBackingObject(HttpServletRequest request)
    	throws Exception {

    	Customer cust = new Customer();
    	cust.setFavFramework(new String []{"Spring MVC","Struts 2"});

    	return cust;
    }

**Note**

    <form:checkboxes items="${dynamic-list}" path="property-to-store" />

For multiple checkboxes, as long as the “**path**” or “**property**” value is equal to any of the “**checkbox values – ${dynamic-list}**“, the matched checkbox will be checked automatically.

## Full checkbox example

Let’s go thought a complete Spring MVC checkbox example :

## 1\. Model

A customer model class to store the checkbox value.

_File : Customer.java_

    package com.mkyong.customer.model;

    public class Customer{

    	//checkbox
    	boolean receiveNewsletter = true; //checked it
    	String [] favLanguages;
    	String [] favFramework;

    	public String[] getFavFramework() {
    		return favFramework;
    	}
    	public void setFavFramework(String[] favFramework) {
    		this.favFramework = favFramework;
    	}
    	public boolean isReceiveNewsletter() {
    		return receiveNewsletter;
    	}
    	public void setReceiveNewsletter(boolean receiveNewsletter) {
    		this.receiveNewsletter = receiveNewsletter;
    	}
    	public String[] getFavLanguages() {
    		return favLanguages;
    	}
    	public void setFavLanguages(String[] favLanguages) {
    		this.favLanguages = favLanguages;
    	}
    }

## 2\. Controller

A SimpleFormController to handle the form checkbox value.

_File : CheckBoxController.java_

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

    public class CheckBoxController extends SimpleFormController{

    	public CheckBoxController(){
    		setCommandClass(Customer.class);
    		setCommandName("customerForm");
    	}

    	@Override
    	protected Object formBackingObject(HttpServletRequest request)
    		throws Exception {

    		Customer cust = new Customer();

    		//Make "Spring MVC" and "Struts 2" as default checked value
    		cust.setFavFramework(new String []{"Spring MVC","Struts 2"});

    		//Make "Java" as default checked value
    		cust.setFavLanguages(new String []{"Java"});

    		return cust;

    	}

    	@Override
    	protected ModelAndView onSubmit(HttpServletRequest request,
    		HttpServletResponse response, Object command, BindException errors)
    		throws Exception {

    		Customer customer = (Customer)command;
    		return new ModelAndView("CustomerSuccess","customer",customer);

    	}

    	//Generate the data for web framework multiple checkboxes
    	protected Map referenceData(HttpServletRequest request) throws Exception {

    		Map referenceData = new HashMap();
    		List<String> webFrameworkList = new ArrayList<String>();
    		webFrameworkList.add("Spring MVC");
    		webFrameworkList.add("Struts 1");
    		webFrameworkList.add("Struts 2");
    		webFrameworkList.add("Apache Wicket");
    		referenceData.put("webFrameworkList", webFrameworkList);

    		return referenceData;

    	}
    }

## 3\. Validator

A simple form validator make sure the “**favLanguages**” property is not empty.

_File : CheckBoxValidator.java_

    package com.mkyong.customer.validator;

    import org.springframework.validation.Errors;
    import org.springframework.validation.Validator;
    import com.mkyong.customer.model.Customer;

    public class CheckBoxValidator implements Validator{

    	@Override
    	public boolean supports(Class clazz) {
    		//just validate the Customer instances
    		return Customer.class.isAssignableFrom(clazz);
    	}

    	@Override
    	public void validate(Object target, Errors errors) {

    		Customer cust = (Customer)target;

    		if(cust.getFavLanguages().length==0){
    			errors.rejectValue("favLanguages", "required.favLanguages");
    		}
    	}
    }

_File : message.properties_

    required.favLanguages = Please select at least a favorite programming language!

## 4\. View

A JSP page to show the use of Spring’s form tag **<form:checkbox />** and **<form:checkboxes />**.

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
    	<h2>Spring's form checkbox example</h2>

    	<form:form method="POST" commandName="customerForm">
    		<form:errors path="*" cssClass="errorblock" element="div" />
    		<table>
    			<tr>
    				<td>Subscribe to newsletter? :</td>
    				<td><form:checkbox path="receiveNewsletter" /></td>
    				<td><form:errors path="receiveNewsletter" cssClass="error" /></td>
    			</tr>
    			<tr>
    				<td>Favourite Languages :</td>
    				<td>
                                           <form:checkbox path="favLanguages" value="Java" />Java
                                           <form:checkbox path="favLanguages" value="C++" />C++
                                           <form:checkbox path="favLanguages" value=".Net" />.Net
                                    </td>
    				<td><form:errors path="favLanguages" cssClass="error" />
    				</td>
    			</tr>
    			<tr>
    				<td>Favourite Web Frameworks :</td>
    				<td><form:checkboxes items="${webFrameworkList}"
    						path="favFramework" /></td>
    				<td><form:errors path="favFramework" cssClass="error" /></td>
    			</tr>
    			<tr>
    				<td colspan="3"><input type="submit" /></td>
    			</tr>
    		</table>
    	</form:form>

    </body>
    </html>

Use JSTL to loop over the submitted checkboxes value, and display it.

_File : CustomerSuccess.jsp_

    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

    <html>
    <body>
    	<h2>Spring's form checkbox example</h2>

    	Receive Newsletter : ${customer.receiveNewsletter}
    	<br />

             Favourite Languages :
    	<c:forEach items="${customer.favLanguages}" var="current">
    		[<c:out value="${current}" />]
    	</c:forEach>
    	<br />

             Favourite Web Frameworks :
    	<c:forEach items="${customer.favFramework}" var="current">
    		[<c:out value="${current}" />]
    	</c:forEach>
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

    	<bean class="com.mkyong.customer.controller.CheckBoxController">
    		<property name="formView" value="CustomerForm" />
    		<property name="successView" value="CustomerSuccess" />

    		<!-- Map a validator -->
    		<property name="validator">
    			<bean class="com.mkyong.customer.validator.CheckBoxValidator" />
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

Access the page – **http://localhost:8080/SpringMVCForm/checkbox.htm**

![SpringMVC-CheckBox-Example-1](http://www.mkyong.com/wp-content/uploads/2010/08/SpringMVC-CheckBox-Example-1.jpg)

If the user did not select any language checkboxes value while submitting the form, display and highlight the error message.

![SpringMVC-CheckBox-Example-2](http://www.mkyong.com/wp-content/uploads/2010/08/SpringMVC-CheckBox-Example-2.jpg)

If the form is submitted successfully, just display the submitted checkboxes value.

![SpringMVC-CheckBox-Example-3](http://www.mkyong.com/wp-content/uploads/2010/08/SpringMVC-CheckBox-Example-3.jpg)

[http://www.mkyong.com/spring-mvc/spring-mvc-checkbox-and-checkboxes-example/](http://www.mkyong.com/spring-mvc/spring-mvc-checkbox-and-checkboxes-example/)
