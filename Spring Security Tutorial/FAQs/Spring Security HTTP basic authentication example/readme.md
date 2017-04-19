When HTTP basic authentication is configured, web browser will display a login dialog for user authentication. This tutorial show you how to configure HTTP basic authentication in Spring Security.

    <http>
    <intercept-url pattern="/welcome*" access="ROLE_USER" />
    <http-basic />
      </http>

Last [Spring Security form-based login example](http://www.mkyong.com/spring-security/spring-security-form-login-example/) will be reused, but switch authentication to support HTTP basic.

## 1\. Spring Security

To enable HTTP basic, just change “**form-login**” to “**http-basic**” tag.

    <beans:beans xmlns="http://www.springframework.org/schema/security"
    	xmlns:beans="http://www.springframework.org/schema/beans"
    	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    	xsi:schemaLocation="http://www.springframework.org/schema/beans
    	http://www.springframework.org/schema/beans/spring-beans-3.0.xsd
    	http://www.springframework.org/schema/security
    	http://www.springframework.org/schema/security/spring-security-3.0.3.xsd">

    	<!-- HTTP basic authentication in Spring Security -->
    	<http>
    		<intercept-url pattern="/welcome*" access="ROLE_USER" />
    		<http-basic />
    	</http>

    	<authentication-manager>
    	   <authentication-provider>
    	       <user-service>
    		   <user name="mkyong" password="123456" authorities="ROLE_USER" />
    	       </user-service>
    	   </authentication-provider>
    	</authentication-manager>

    </beans:beans>

Done, that’s all.

## 2\. Demo

When access the secured URL, browser will display a login dialog box automatically.

_URL : http://localhost:8080/SpringMVC/welcome_

![http basic example](http://www.mkyong.com/wp-content/uploads/2011/08/spring-security-http-basic.png)

[http://www.mkyong.com/spring-security/spring-security-http-basic-authentication-example/](http://www.mkyong.com/spring-security/spring-security-http-basic-authentication-example/)
