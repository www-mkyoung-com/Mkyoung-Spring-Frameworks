In Spring Security, to log out, just add a link to url “**j_spring_security_logout**“, for example :

    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
    <html>
    <body>
    	<h2>messages, whatever</h2>
    	<a href="<c:url value="j_spring_security_logout" />" > Logout</a>
    </body>
    </html>

In Spring security, declares “`logout`” tag, and configure the “`logout-success-url`” attribute :

    <beans:beans xmlns="http://www.springframework.org/schema/security"
    	xmlns:beans="http://www.springframework.org/schema/beans"
    	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    	xsi:schemaLocation="http://www.springframework.org/schema/beans
    	http://www.springframework.org/schema/beans/spring-beans-3.0.xsd
    	http://www.springframework.org/schema/security
    	http://www.springframework.org/schema/security/spring-security-3.0.3.xsd">

    	<http auto-config="true">
    		<intercept-url pattern="/welcome*" access="ROLE_USER" />
    		<logout logout-success-url="/welcome" />
    	</http>

    	<authentication-manager>
    	  <authentication-provider>
    	    <user-service>
    		<user name="mkyong" password="password" authorities="ROLE_USER" />
    	    </user-service>
    	  </authentication-provider>
    	</authentication-manager>

    </beans:beans>

[http://www.mkyong.com/spring-security/spring-security-logout-example/](http://www.mkyong.com/spring-security/spring-security-logout-example/)
