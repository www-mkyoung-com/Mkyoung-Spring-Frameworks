In Spring Security, access control or authorization is easy to implement. See following code snippet :

    <http auto-config="true">
    <intercept-url pattern="/admin*" access="ROLE_ADMIN" />
      </http>

It means, only user with authority of “**ROLE_ADMIN**” is allow to access URI **/admin***. If non authorized user try to access it, a “**http 403 access denied page**” will be displayed.

**Spring EL + Access Control**  
See equivalent version in Spring EL. It is more flexible and contains many useful ready made functions like “_hasIpAddress_“, make sure check all available el functions in this [official Spring el access control documentation](http://static.springsource.org/spring-security/site/docs/3.0.x/reference/el-access.html).

    <http auto-config="true" use-expressions="true">
    <intercept-url pattern="/admin*" access="hasRole('ROLE_ADMIN')" />
      </http>

In this tutorial, we show you how to use Spring Security to implement access control to url “**/admin***“, where only user authorized with “**ROLE_ADMIN**” is allow to access this page.

## 1\. Project Dependencies

Access control is included in core Spring Security jar. Refer to this [Spring Security hello world example](http://www.mkyong.com/spring-security/spring-security-hello-world-example/) for list of the required dependencies.

## 2\. Spring MVC

Spring MVC controller and return a “hello” view, it should be self-explanatory.

_File : WelcomeController.java_

    package com.mkyong.common.controller;

    import org.springframework.stereotype.Controller;
    import org.springframework.ui.ModelMap;
    import org.springframework.web.bind.annotation.RequestMapping;
    import org.springframework.web.bind.annotation.RequestMethod;

    @Controller
    public class WelcomeController {

    	@RequestMapping(value = "/admin", method = RequestMethod.GET)
    	public String welcomeAdmin(ModelMap model) {

    		model.addAttribute("message", "Spring Security - ROLE_ADMIN");
    		return "hello";

    	}

    }

_File : hello.jsp_

    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
    </div><h2>Message : ${message}</h2>

    	<a href="<c:url value="j_spring_security_logout" />" > Logout</a>
    </body>
    </html>

## 3\. Spring Security

Full Spring security configuration, only user “**eclipse**” is allow to access “**/admin**” page.

    <beans:beans xmlns="http://www.springframework.org/schema/security"
    	xmlns:beans="http://www.springframework.org/schema/beans"
    	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    	xsi:schemaLocation="http://www.springframework.org/schema/beans
    	http://www.springframework.org/schema/beans/spring-beans-3.0.xsd
    	http://www.springframework.org/schema/security
    	http://www.springframework.org/schema/security/spring-security-3.0.3.xsd">

    	<http auto-config="true">
    		<intercept-url pattern="/admin*" access="ROLE_ADMIN" />
    		<logout logout-success-url="/admin" />
    	</http>

    	<authentication-manager>
    	  <authentication-provider>
    	   <user-service>
    		<user name="mkyong" password="password" authorities="ROLE_USER" />
    		<user name="eclipse" password="password" authorities="ROLE_ADMIN" />
    	   </user-service>
    	  </authentication-provider>
    	</authentication-manager>

    </beans:beans>

## 4\. Demo

URL : _http://localhost:8080/SpringMVC/admin_

1\. Default login form is displayed.

![demo page - access control](http://www.mkyong.com/wp-content/uploads/2011/08/spring-security-access-control-login.png)

2\. If user “**mkyong**” is logged in, “**http 403 is access denied page**” will be displayed, because “**mkyong**” is “**ROLE_USER**“.

![demo page - access denied](http://www.mkyong.com/wp-content/uploads/2011/08/spring-security-access-control-denied.png)

3\. If user “**eclipse**” is logged in, “**hello.jsp**” will be displayed, because “**eclipse**” is “**ROLE_ADMIN**“.

![demo page - success](http://www.mkyong.com/wp-content/uploads/2011/08/spring-security-access-control-success.png)

**Customize 403 page**  
Default 403 page is ugly, read this example – [How to customize http 403 access denied page in spring security](http://www.mkyong.com/spring-security/customize-http-403-access-denied-page-in-spring-security/).

[http://www.mkyong.com/spring-security/spring-security-access-control-example/](http://www.mkyong.com/spring-security/spring-security-access-control-example/)
