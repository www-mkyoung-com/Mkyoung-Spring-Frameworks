By default, if no login form is specified, Spring Security will create a default login form automatically. Please refer to this – [Spring Security hello world example](http://www.mkyong.com/spring-security/spring-security-hello-world-example/).

In this tutorial, we will show you how to create a custom login form for Spring Security (XML example).

Technologies used :

1.  Spring 3.2.8.RELEASE
2.  Spring Security 3.2.3.RELEASE
3.  Eclipse 4.2
4.  JDK 1.6
5.  Maven 3

**Note**  
In this example, previous [Spring Security hello world example](http://www.mkyong.com/spring-security/spring-security-hello-world-example/) will be reused, enhance it to support a custom login form.

## 1\. Project Demo

## 2\. Directory Structure

Review the final directory structure of this tutorial.

![spring-security-custom-login-xml-directory](http://www.mkyong.com/wp-content/uploads/2011/08/spring-security-custom-login-xml-directory.png)

## 3\. Spring Security Configuration

Defined your custom login form in Spring XML file. See explanation below :

1.  login-page=”/login” – The page to display the custom login form
2.  authentication-failure-url=”/login?error” – If authentication failed, forward to page `/login?error`
3.  logout-success-url=”/login?logout” – If logout successful, forward to view `/logout`
4.  username-parameter=”username” – The name of the request which contains the “username”. In HTML, this is the name of the input text.
5.  <csrf/> – Enable the Cross Site Request Forgery (CSRF) protection, refer to this [link](http://spring.io/blog/2013/08/21/spring-security-3-2-0-rc1-highlights-csrf-protection/). In XML, by default, CSRF protection is disabled.

Normally, we don’t involve in the authentication like login or logout processing, let Spring handle it, we just handle the successful or failed page to display.

spring-security.xml

    <beans:beans xmlns="http://www.springframework.org/schema/security"
    	xmlns:beans="http://www.springframework.org/schema/beans"
    	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    	xsi:schemaLocation="http://www.springframework.org/schema/beans
    	http://www.springframework.org/schema/beans/spring-beans-3.0.xsd
    	http://www.springframework.org/schema/security
    	http://www.springframework.org/schema/security/spring-security-3.2.xsd">

    	<http auto-config="true">
    		<intercept-url pattern="/admin**" access="ROLE_USER" />
    		<form-login
    		    login-page="/login"
    		    default-target-url="/welcome"
    			authentication-failure-url="/login?error"
    			username-parameter="username"
    			password-parameter="password" />
    		<logout logout-success-url="/login?logout" />
    		<!-- enable csrf protection -->
    		<csrf/>
    	</http>

    	<authentication-manager>
    		<authentication-provider>
    		  <user-service>
    			<user name="mkyong" password="123456" authorities="ROLE_USER" />
    		  </user-service>
    		</authentication-provider>
    	</authentication-manager>

    </beans:beans>

In above congratulation, the `/admin` and sub-folders of it are all password protected.

**Cross Site Request Forgery (CSRF) Protection**  
If CSRF is enabled, you have to include a `_csrf.token` in the page you want to login or logout. Refer to below `login.jsp` and `admin.jsp` (logout form). Otherwise, both login and logout function will be failed.

**Password in clear-text?**  
A pretty bad idea, you should always hash the password with SHA algorithm, this tutorial show you how – Spring Security password hashing example.

## 4\. Custom Login Form

A custom login form to match above (step 3) Spring Security congratulation. It should be self-explanatory.

login.jsp

    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
    <html>
    <head>
    <title>Login Page</title>
    <style>
    .error {
    	padding: 15px;
    	margin-bottom: 20px;
    	border: 1px solid transparent;
    	border-radius: 4px;
    	color: #a94442;
    	background-color: #f2dede;
    	border-color: #ebccd1;
    }

    .msg {
    	padding: 15px;
    	margin-bottom: 20px;
    	border: 1px solid transparent;
    	border-radius: 4px;
    	color: #31708f;
    	background-color: #d9edf7;
    	border-color: #bce8f1;
    }

    #login-box {
    	width: 300px;
    	padding: 20px;
    	margin: 100px auto;
    	background: #fff;
    	-webkit-border-radius: 2px;
    	-moz-border-radius: 2px;
    	border: 1px solid #000;
    }
    </style>
    </head>
    <body onload='document.loginForm.username.focus();'>

    	<h1>Spring Security Custom Login Form (XML)</h1>

    	<div id="login-box">

    		<h2>Login with Username and Password</h2>

    		<c:if test="${not empty error}">
    			<div class="error">${error}</div>
    		</c:if>
    		<c:if test="${not empty msg}">
    			<div class="msg">${msg}</div>
    		</c:if>

    		<form name='loginForm'
    		  action="<c:url value='j_spring_security_check' />" method='POST'>

    		  <table>
    			<tr>
    				<td>User:</td>
    				<td><input type='text' name='username' value=''></td>
    			</tr>
    			<tr>
    				<td>Password:</td>
    				<td><input type='password' name='password' /></td>
    			</tr>
    			<tr>
    				<td colspan='2'><input name="submit" type="submit"
    					value="submit" /></td>
    			</tr>
    		  </table>

    		  <input type="hidden" name="${_csrf.parameterName}"
    			value="${_csrf.token}" />

    		</form>
    	</div>

    </body>
    </html>

And the other two JSP pages, btw `admin.jsp` is password protected by Spring Security.

hello.jsp

    <%@page session="false"%>
    <html>
    <body>
    	<h1>Title : ${title}</h1>
    	<h1>Message : ${message}</h1>
    </body>
    </html>

admin.jsp + logout

    <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
    <%@page session="true"%>
    <html>
    <body>
    	<h1>Title : ${title}</h1>
    	<h1>Message : ${message}</h1>

    	<c:url value="/j_spring_security_logout" var="logoutUrl" />

    	<!-- csrt for log out-->
    	<form action="${logoutUrl}" method="post" id="logoutForm">
    	  <input type="hidden"
    		name="${_csrf.parameterName}"
    		value="${_csrf.token}" />
    	</form>

    	<script>
    		function formSubmit() {
    			document.getElementById("logoutForm").submit();
    		}
    	</script>

    	<c:if test="${pageContext.request.userPrincipal.name != null}">
    		<h2>
    			Welcome : ${pageContext.request.userPrincipal.name} | <a
    				href="javascript:formSubmit()"> Logout</a>
    		</h2>
    	</c:if>

    </body>
    </html>

## 5\. Spring MVC Controller

A simple controller.

HelloController.java

    package com.mkyong.web.controller;

    import org.springframework.stereotype.Controller;
    import org.springframework.web.bind.annotation.RequestMapping;
    import org.springframework.web.bind.annotation.RequestMethod;
    import org.springframework.web.bind.annotation.RequestParam;
    import org.springframework.web.servlet.ModelAndView;

    @Controller
    public class HelloController {

    	@RequestMapping(value = { "/", "/welcome**" }, method = RequestMethod.GET)
    	public ModelAndView welcomePage() {

    		ModelAndView model = new ModelAndView();
    		model.addObject("title", "Spring Security Custom Login Form");
    		model.addObject("message", "This is welcome page!");
    		model.setViewName("hello");
    		return model;

    	}

    	@RequestMapping(value = "/admin**", method = RequestMethod.GET)
    	public ModelAndView adminPage() {

    		ModelAndView model = new ModelAndView();
    		model.addObject("title", "Spring Security Custom Login Form");
    		model.addObject("message", "This is protected page!");
    		model.setViewName("admin");

    		return model;

    	}

    	//Spring Security see this :
    	@RequestMapping(value = "/login", method = RequestMethod.GET)
    	public ModelAndView login(
    		@RequestParam(value = "error", required = false) String error,
    		@RequestParam(value = "logout", required = false) String logout) {

    		ModelAndView model = new ModelAndView();
    		if (error != null) {
    			model.addObject("error", "Invalid username and password!");
    		}

    		if (logout != null) {
    			model.addObject("msg", "You've been logged out successfully.");
    		}
    		model.setViewName("login");

    		return model;

    	}

    }

## 6\. Demo

6.1\. Welcome Page – http://localhost:8080/spring-security-loginform-xml/

![spring-security-custom-login-xml-welcome](http://www.mkyong.com/wp-content/uploads/2011/08/spring-security-custom-login-xml-welcome.png)

6.2 Try to access `/admin` page, Spring Security will intercept the request and redirect to `/login`, and your custom login form is displayed.

![spring-security-custom-login-xml-login](http://www.mkyong.com/wp-content/uploads/2011/08/spring-security-custom-login-xml-login.png)

6.3\. If username and password is incorrect, error messages will be displayed, and Spring will redirect to this URL `/login?error`.

![spring-security-custom-login-xml-error](http://www.mkyong.com/wp-content/uploads/2011/08/spring-security-custom-login-xml-error.png)

6.4\. If username and password are correct, Spring will redirect to the original requested URL and display the page.

![spring-security-custom-login-xml-admin](http://www.mkyong.com/wp-content/uploads/2011/08/spring-security-custom-login-xml-admin.png)

6.5\. Try to log out, it will redirect to `/login?logout` page.

![spring-security-custom-login-xml-logout](http://www.mkyong.com/wp-content/uploads/2011/08/spring-security-custom-login-xml-logout.png)

[http://www.mkyong.com/spring-security/spring-security-form-login-example/](http://www.mkyong.com/spring-security/spring-security-form-login-example/)
