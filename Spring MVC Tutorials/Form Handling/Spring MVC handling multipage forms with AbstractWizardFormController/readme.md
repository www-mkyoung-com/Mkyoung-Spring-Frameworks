In last [Spring MVC form handling example](http://www.mkyong.com/spring-mvc/spring-mvc-form-handling-example/), we should you the use of **SimpleFormController** to handle single page form submission, which is quite straightforward and easy.

But, sometimes, you may need to deal with “**wizard form**“, which need handle form into multiple pages, and ask user to fill in the form page by page. The main concern in this wizard form situation is how to store the model data (data filled in by user) and bring it across multiple pages?

## SAbstractWizardFormController

Fortunately, Spring MVC comes with **AbstractWizardFormController** class to handle this wizard form easily. In this tutorial, we show you how to use **AbstractWizardFormController** class to store and bring the form’s data across multiple pages, apply validation and display the form’s data at the last page.

## 1\. Wizard Form Pages

5 pages for this demonstration, work in following sequences :

    [User] --> WelcomePage --> Page1 --> Page2 --> Page3 --> ResultPage

With **AbstractWizardFormController**, the page sequence is determined by the “name” of the submit button:

1.  _finish: Finish the wizard form.
2.  _cancel: Cancel the wizard form.
3.  _targetx: Move to the target page, where x is the zero-based page index. e.g **_target0**,** _target1** and etc.

_1\. WelcomePage.jsp_  
A welcome page, with a hyperlink to start the wizard form process.

    <html>
    <body>
    	<div class="ads-in-post hide_if_width_less_800">
    <script async src="//pagead2.googlesyndication.com/pagead/js/adsbygoogle.js"></script>
    <!-- new 728x90 - After2ndH4 -->
    <ins class="adsbygoogle hide_if_width_less_800"
         style="display:inline-block;width:728px;height:90px"
         data-ad-client="ca-pub-2836379775501347"
         data-ad-slot="3642936086"
    	 data-ad-region="mkyongregion"></ins>
    <script>
    (adsbygoogle = window.adsbygoogle || []).push({});
    </script>
    </div><h2>Handling multipage forms in Spring MVC</h2>
    	Click here to start playing -
    	<a href="user.htm">AbstractWizardFormController example</a>
    </body>
    </html>

_2\. Page1Form.jsp_  
Page 1, with a “username” text box, display error message if any, and contains 2 submit buttons , where :

1.  _target1 – move to page 2.
2.  _cancel – cancel the wizard form process and move it to the cancel page

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
    	<h2>Page1Form.jsp</h2>

    	<form:form method="POST" commandName="userForm">
    		<form:errors path="*" cssClass="errorblock" element="div" />
    		<table>
    			<tr>
    				<td>Username :</td>
    				<td><form:input path="userName" />
    				</td>
    				<td><form:errors path="userName" cssClass="error" />
    				</td>
    			</tr>
    			<tr>
    			<tr>
    				<td colspan="3"><input type="submit" value="Next"
    					name="_target1" /> <input type="submit" value="Cancel"
    					name="_cancel" /></td>
    			</tr>
    		</table>
    	</form:form>

    </body>
    </html>

_3\. Page2Form.jsp_  
Page 2, with a “password” field, display error message if any, and contains 3 submit buttons , where :

1.  _target0 – move to page 1.
2.  _target2 – move to page 3.
3.  _cancel – cancel the wizard form process and move it to the cancel page

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
    	<h2>Page2Form.jsp</h2>

    	<form:form method="POST" commandName="userForm">
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
    			<tr>
    				<td colspan="3"><input type="submit" value="Previous"
    					name="_target0" /> <input type="submit" value="Next"
    					name="_target2" /> <input type="submit" value="Cancel"
    					name="_cancel" /></td>
    			</tr>
    		</table>
    	</form:form>

    </body>
    </html>

_4\. Page3Form.jsp_  
Page 3, with a “remark” text box, display error message if any, and contains 3 submit buttons , where :

1.  _target1 – move to page 2.
2.  _finish – finish the wizard form process and move it to the finish page.
3.  _cancel – cancel the wizard form process and move it to the cancel page.

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
    	<h2>Page3Form.jsp</h2>

    	<form:form method="POST" commandName="userForm">
    		<form:errors path="*" cssClass="errorblock" element="div" />
    		<table>
    			<tr>
    				<td>Remark :</td>
    				<td><form:input path="remark" />
    				</td>
    				<td><form:errors path="remark" cssClass="error" />
    				</td>
    			</tr>
    			<tr>
    			<tr>
    				<td colspan="3"><input type="submit" value="Previous"
    					name="_target1" /> <input type="submit" value="Finish"
    					name="_finish" /> <input type="submit" value="Cancel"
    					name="_cancel" /></td>
    			</tr>
    		</table>
    	</form:form>

    </body>
    </html>

_5\. ResultForm.jsp_  
Display all the form’s data which collected from the previous 3 pages.

    <html>
    <body>
    	<h2>ResultForm.jsp</h2>

    	<table>
    		<tr>
    			<td>UserName :</td>
    			<td>${user.userName}</td>
    		</tr>
    		<tr>
    			<td>Password :</td>
    			<td>${user.password}</td>
    		</tr>
    		<tr>
    			<td>Remark :</td>
    			<td>${user.remark}</td>
    		</tr>
    	</table>

    </body>
    </html>

## 2\. Model

Create a model class to store the form’s data.

_File : User.java_

    package com.mkyong.common.model;

    public class User{

    	String userName;
    	String password;
    	String remark;

    	//getter and setter methods
    }

## 3\. AbstractWizardFormController

Extends the **AbstractWizardFormController**, just override following methods

1.  **processFinish**– Fire when user click on the submit button with a name of “**_finish**“.
2.  **processCancel** – Fire when user click on the submit button with a name of “**_cancel**“.
3.  **formBackingObject** – Use “User” model class to store all the form’s data in multiple pages.

_File : UserController.java_

    package com.mkyong.common.controller;

    import javax.servlet.http.HttpServletRequest;
    import javax.servlet.http.HttpServletResponse;
    import org.springframework.validation.BindException;
    import org.springframework.validation.Errors;
    import org.springframework.web.servlet.ModelAndView;
    import org.springframework.web.servlet.mvc.AbstractWizardFormController;
    import com.mkyong.common.model.User;
    import com.mkyong.common.validator.UserValidator;

    public class UserController extends AbstractWizardFormController{

    	public UserController(){
    		setCommandName("userForm");
    	}

    	@Override
    	protected Object formBackingObject(HttpServletRequest request)
    		throws Exception {

    		return new User();
    	}
    	@Override
    	protected ModelAndView processFinish(HttpServletRequest request,
    		HttpServletResponse response, Object command, BindException errors)
    		throws Exception {

    		//Get the data from command object
    		User user = (User)command;
    		System.out.println(user);

    		//where is the finish page?
    		return new ModelAndView("ResultForm", "user", user);
    	}

    	@Override
    	protected ModelAndView processCancel(HttpServletRequest request,
    		HttpServletResponse response, Object command, BindException errors)
    		throws Exception {

    		//where is the cancel page?
    		return new ModelAndView("WelcomePage");
    	}
    }

A simple controller to return a “**WelcomePage**” view.

_File : WelcomeController.java_

    package com.mkyong.common.controller;

    import javax.servlet.http.HttpServletRequest;
    import javax.servlet.http.HttpServletResponse;

    import org.springframework.web.servlet.ModelAndView;
    import org.springframework.web.servlet.mvc.AbstractController;

    public class WelcomeController extends AbstractController{

    	@Override
    	protected ModelAndView handleRequestInternal(HttpServletRequest request,
    		HttpServletResponse response) throws Exception {

    		return new ModelAndView("WelcomePage");
    	}

    }

## 4\. Multipage / Wizard Form Validation

In `SimpleFormController`, you create a validator class, put all the validation logic inside the **validate()** method, and register the validator to the simple form controller decoratively.

But, it’s a bit different in **AbstractWizardFormController**. First, create a validator class, and also the validation method for each of the page, as following :

_File : UserValidator.java_

    package com.mkyong.common.validator;

    import org.springframework.validation.Errors;
    import org.springframework.validation.ValidationUtils;
    import org.springframework.validation.Validator;
    import com.mkyong.common.model.User;

    public class UserValidator implements Validator{

    	@Override
    	public boolean supports(Class clazz) {
    		//just validate the User instances
    		return User.class.isAssignableFrom(clazz);
    	}

    	//validate page 1, userName
    	public void validatePage1Form(Object target, Errors errors) {
    		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "userName",
    		        "required.userName", "Field name is required.");
    	}

    	//validate page 2, password
    	public void validatePage2Form(Object target, Errors errors) {
    		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password",
    			"required.password", "Field name is required.");
    	}

    	//validate page 3, remark
    	public void validatePage3Form(Object target, Errors errors) {
    		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "remark",
    			"required.remark", "Field name is required.");
    	}

    	@Override
    	public void validate(Object target, Errors errors) {
    		validatePage1Form(target, errors);
    		validatePage2Form(target, errors);
    		validatePage3Form(target, errors);
    	}
    }

_File : User.properties_ – Properties to store the error message

    required.userName = Username is required!
    required.password = Password is required!
    required.remark = Remark is required!

And, in the wizard form controller (`UserController.java`), override the **validatePage()** by calling the validator manually (no more declaration like simple form controller).

See the updated version of **UserController.java**.

    public class UserController extends AbstractWizardFormController{
    	//other methods, see above

    	@Override
    	protected void validatePage(Object command, Errors errors, int page) {

    		UserValidator validator = (UserValidator) getValidator();

    		//page is 0-indexed
    		switch (page) {
    		   case 0: //if page 1 , go validate with validatePage1Form
    			validator.validatePage1Form(command, errors);
    			break;
    		   case 1: //if page 2 , go validate with validatePage2Form
    			validator.validatePage2Form(command, errors);
    			break;
    		   case 2: //if page 3 , go validate with validatePage3Form
    			validator.validatePage3Form(command, errors);
    			break;
    		}
    	}
    }

In the **validatePage()** method, use a “**switch**” function to determine which page is calling and associated it with the corresponds validator. The page is in 0-indexed.

## 5\. Spring Configuration

Declare the wizard form controller (`UserController.java`), put all the pages in the correct order and register a validator.

    <bean class="com.mkyong.common.controller.UserController" >
        	   <property name="pages">
    	<list>
    	<!-- follow sequence -->
    	<value>Page1Form</value> <!-- page1, _target0 -->
    	<value>Page2Form</value> <!-- page2, _target1 -->
    	<value>Page3Form</value> <!-- page3, _target2 -->
    	</list>
       </property>
       <property name="validator">
    	<bean class="com.mkyong.common.validator.UserValidator" />
       </property>
           </bean>

**Note**  
In the “pages” property, the order of the list value is used to define the sequence of the page in the wizard form.

See full example :

    <beans xmlns="http://www.springframework.org/schema/beans"
    	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    	xsi:schemaLocation="http://www.springframework.org/schema/beans
    	http://www.springframework.org/schema/beans/spring-beans-2.5.xsd">

     <bean
      class="org.springframework.web.servlet.mvc.support.ControllerClassNameHandlerMapping" />

        <bean class="com.mkyong.common.controller.WelcomeController" />
        <bean class="com.mkyong.common.controller.UserController" >
        	<property name="pages">
    	   <list>
    		<!-- follow sequence -->
    		<value>Page1Form</value> <!-- page1 -->
    		<value>Page2Form</value> <!-- page2 -->
    		<value>Page3Form</value> <!-- page3 -->
    	   </list>
    	   </property>
    	   <property name="validator">
    		<bean class="com.mkyong.common.validator.UserValidator" />
    	   </property>
          </bean>

          <!-- Register User.properties for validation error message -->
          <bean id="messageSource"
               class="org.springframework.context.support.ResourceBundleMessageSource">
    	   <property name="basename" value="User" />
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

## 5\. Demo

URL : **http://localhost:8080/SpringMVC/welcome.htm**

**1\. WelcomePage.jsp**, click on the link, move to **Page1Form.jsp**.

![SpringMVC-Multipage-Forms-Example1](http://www.mkyong.com/wp-content/uploads/2010/08/SpringMVC-Multipage-Forms-Example1.jpg)

**2\. Page1Form.jsp**, contains a “username” text box field, and 2 buttons :

1.  “next” button – move to Page2Form.jsp.
2.  “cancel” button – move to WelcomePage.jsp

![SpringMVC-Multipage-Forms-Example2](http://www.mkyong.com/wp-content/uploads/2010/08/SpringMVC-Multipage-Forms-Example2.jpg)

If the “username” is empty while submitting the form, display the error message.

![SpringMVC-Multipage-Forms-Example2-Error](http://www.mkyong.com/wp-content/uploads/2010/08/SpringMVC-Multipage-Forms-Example2-Error.jpg)

**3\. Page2Form.jsp**, contains a “password” field, and 3 buttons :

1.  “previous” button – move to Page1Form.jsp.
2.  “next” button – move to Page3Form.jsp.
3.  “cancel” button – move to WelcomePage.jsp.

![SpringMVC-Multipage-Forms-Example3](http://www.mkyong.com/wp-content/uploads/2010/08/SpringMVC-Multipage-Forms-Example3.jpg)

**4\. Page3Form.jsp**, contains a “remark” textbox field, and 3 buttons :

1.  “previous” button – move to Page2Form.jsp.
2.  “finish” button – move to ResultForm.jsp.
3.  “cancel” button – move to WelcomePage.jsp.

![SpringMVC-Multipage-Forms-Example4](http://www.mkyong.com/wp-content/uploads/2010/08/SpringMVC-Multipage-Forms-Example4.jpg)

**5\. ResultForm.jsp**, display all the form’s data.

![SpringMVC-Multipage-Forms-Example5](http://www.mkyong.com/wp-content/uploads/2010/08/SpringMVC-Multipage-Forms-Example5.jpg)

[http://www.mkyong.com/spring-mvc/spring-mvc-handling-multipage-forms-with-abstractwizardformcontroller/](http://www.mkyong.com/spring-mvc/spring-mvc-handling-multipage-forms-with-abstractwizardformcontroller/)
