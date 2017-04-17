![security](http://www.mkyong.com/wp-content/uploads/2014/04/security.jpg)

In preview [post](http://www.mkyong.com/spring-security/spring-security-hello-world-example/), we are using XML files to configure the Spring Security in a Spring MVC environment. In this tutorial, we are going to show you how to convert the previous XML-base Spring Security project into a pure Spring annotation project.

Technologies used :

1.  Spring 3.2.8.RELEASE
2.  Spring Security 3.2.3.RELEASE
3.  Eclipse 4.2
4.  JDK 1.6
5.  Maven 3
6.  Tomcat 7 (Servlet 3.x)

**Few Notes**

1.  This tutorial is using `WebApplicationInitializer` to load the Spring Context Loader automatically, which is supported in Servlet 3.x container only, for example, Tomcat 7 and Jetty 8.
2.  Since we are using `WebApplicationInitializer`, the `web.xml` file is NOT required.
3.  Spring Security annotations are supported in older Servlet 2.x container, for example, Tomcat 6\. If you use the classic XML file to load the Spring context, this tutorial is still able to deploy on Servlet 2.x container, for example, Tomcat 6

## 1\. Project Demo

See how it works.

## 2\. Directory Structure

Review the final directory structure of this tutorial.

![spring-security-helloworld-annotation-directory](http://www.mkyong.com/wp-content/uploads/2014/04/spring-security-helloworld-annotation-directory.png)

## 3\. Spring Security Dependencies

To use Spring security, you need `spring-security-web` and `spring-security-config`.

pom.xml

    <properties>
    	<jdk.version>1.6</jdk.version>
    	<spring.version>3.2.8.RELEASE</spring.version>
    	<spring.security.version>3.2.3.RELEASE</spring.security.version>
    	<jstl.version>1.2</jstl.version>
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

    	<!-- Spring Security -->
    	<dependency>
    		<groupId>org.springframework.security</groupId>
    		<artifactId>spring-security-web</artifactId>
    		<version>${spring.security.version}</version>
    	</dependency>

    	<dependency>
    		<groupId>org.springframework.security</groupId>
    		<artifactId>spring-security-config</artifactId>
    		<version>${spring.security.version}</version>
    	</dependency>

    	<!-- jstl for jsp page -->
    	<dependency>
    		<groupId>jstl</groupId>
    		<artifactId>jstl</artifactId>
    		<version>${jstl.version}</version>
    	</dependency>

    </dependencies>

## 4\. Spring MVC Web Application

A simple controller :

1.  If URL = `/welcome` or `/` , return hello page.
2.  If URL = `/admin` , return admin page.
3.  If URL = `/dba` , return admin page.

Later, we will secure the `/admin` and `/dba` URLs.

HelloController.java

    package com.mkyong.web.controller;

    import org.springframework.stereotype.Controller;
    import org.springframework.web.bind.annotation.RequestMapping;
    import org.springframework.web.bind.annotation.RequestMethod;
    import org.springframework.web.servlet.ModelAndView;

    @Controller
    public class HelloController {

    	@RequestMapping(value = { "/", "/welcome**" }, method = RequestMethod.GET)
    	public ModelAndView welcomePage() {

    		ModelAndView model = new ModelAndView();
    		model.addObject("title", "Spring Security Hello World");
    		model.addObject("message", "This is welcome page!");
    		model.setViewName("hello");
    		return model;

    	}

    	@RequestMapping(value = "/admin**", method = RequestMethod.GET)
    	public ModelAndView adminPage() {

    		ModelAndView model = new ModelAndView();
    		model.addObject("title", "Spring Security Hello World");
    		model.addObject("message", "This is protected page - Admin Page!");
    		model.setViewName("admin");

    		return model;

    	}

    	@RequestMapping(value = "/dba**", method = RequestMethod.GET)
    	public ModelAndView dbaPage() {

    		ModelAndView model = new ModelAndView();
    		model.addObject("title", "Spring Security Hello World");
    		model.addObject("message", "This is protected page - Database Page!");
    		model.setViewName("admin");

    		return model;

    	}

    }

Two JSP pages.

hello.jsp

    <%@page session="false"%>
    <html>
    <body>
    	<h1>Title : ${title}</h1>
    	<h1>Message : ${message}</h1>
    </body>
    </html>

admin.jsp

    <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
    <%@page session="true"%>
    <html>
    <body>
    	<h1>Title : ${title}</h1>
    	<h1>Message : ${message}</h1>

    	<c:if test="${pageContext.request.userPrincipal.name != null}">
    		<h2>Welcome : ${pageContext.request.userPrincipal.name}
                     | <a href="<c:url value="/logout" />" > Logout</a></h2>
    	</c:if>
    </body>
    </html>

## 5\. Spring Security Configuration

_5.1_ Create a Spring Security configuration file, and annotated with `@EnableWebSecurity`

SecurityConfig.java

    package com.mkyong.config;

    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
    import org.springframework.security.config.annotation.web.builders.HttpSecurity;
    import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
    import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

    @Configuration
    @EnableWebSecurity
    public class SecurityConfig extends WebSecurityConfigurerAdapter {

    	@Autowired
    	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    	  auth.inMemoryAuthentication().withUser("mkyong").password("123456").roles("USER");
    	  auth.inMemoryAuthentication().withUser("admin").password("123456").roles("ADMIN");
    	  auth.inMemoryAuthentication().withUser("dba").password("123456").roles("DBA");
    	}

    	@Override
    	protected void configure(HttpSecurity http) throws Exception {

    	  http.authorizeRequests()
    		.antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
    		.antMatchers("/dba/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_DBA')")
    		.and().formLogin();

    	}
    }

The equivalent of the Spring Security xml file :

    <http auto-config="true">
    	<intercept-url pattern="/admin**" access="ROLE_ADMIN" />
    	<intercept-url pattern="/dba**" access="ROLE_ADMIN,ROLE_DBA" />
    </http>

    <authentication-manager>
      <authentication-provider>
        <user-service>
    	<user name="mkyong" password="123456" authorities="ROLE_USER" />
    	<user name="admin" password="123456" authorities="ROLE_ADMIN" />
    	<user name="dba" password="123456" authorities="ROLE_DBA" />
        </user-service>
      </authentication-provider>
    </authentication-manager>

_5.2_ Create a class extends `AbstractSecurityWebApplicationInitializer`, it will load the `springSecurityFilterChain`automatically.

SpringSecurityInitializer.java

    package com.mkyong.config.core;

    import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

    public class SpringSecurityInitializer extends AbstractSecurityWebApplicationInitializer {
       //do nothing
    }

The equivalent of Spring Security in `web.xml` file :

    <filter>
    	<filter-name>springSecurityFilterChain</filter-name>
    	<filter-class>org.springframework.web.filter.DelegatingFilterProxy
                    </filter-class>
    </filter>

    <filter-mapping>
    	<filter-name>springSecurityFilterChain</filter-name>
    	<url-pattern>/*</url-pattern>
    </filter-mapping>

## 6\. Spring MVC Configuration

_6.1_ A Config class, define the view’s technology and imports above `SecurityConfig.java`.

AppConfig.java

    package com.mkyong.config;

    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.ComponentScan;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.context.annotation.Import;
    import org.springframework.web.servlet.config.annotation.EnableWebMvc;
    import org.springframework.web.servlet.view.InternalResourceViewResolver;
    import org.springframework.web.servlet.view.JstlView;

    @EnableWebMvc
    @Configuration
    @ComponentScan({ "com.mkyong.web.*" })
    @Import({ SecurityConfig.class })
    public class AppConfig {

    	@Bean
    	public InternalResourceViewResolver viewResolver() {
    		InternalResourceViewResolver viewResolver
                              = new InternalResourceViewResolver();
    		viewResolver.setViewClass(JstlView.class);
    		viewResolver.setPrefix("/WEB-INF/pages/");
    		viewResolver.setSuffix(".jsp");
    		return viewResolver;
    	}

    }

The equivalent of the Spring XML file :

    <context:component-scan base-package="com.mkyong.web.*" />

    <bean
    	class="org.springframework.web.servlet.view.InternalResourceViewResolver">
    	<property name="prefix">
    		<value>/WEB-INF/pages/</value>
    	</property>
    	<property name="suffix">
    		<value>.jsp</value>
    	</property>
    </bean>

_6.2_ Create a `Initializer` class, to load everything.

SpringMvcInitializer.java

    package com.mkyong.config.core;

    import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;
    import com.mkyong.config.AppConfig;

    public class SpringMvcInitializer
           extends AbstractAnnotationConfigDispatcherServletInitializer {

    	@Override
    	protected Class<?>[] getRootConfigClasses() {
    		return new Class[] { AppConfig.class };
    	}

    	@Override
    	protected Class<?>[] getServletConfigClasses() {
    		return null;
    	}

    	@Override
    	protected String[] getServletMappings() {
    		return new String[] { "/" };
    	}

    }

Done.

**Note**  
In Servlet 3.x container environment + Spring container will detect and loads the `Initializer` classes automatically.

## 7\. Demo

7.1\. Welcome Page – http://localhost:8080/spring-security-helloworld-annotation/welcome

![spring-security-helloworld-annotation-welcome](http://www.mkyong.com/wp-content/uploads/2014/04/spring-security-helloworld-annotation-welcome.png)

7.2 Try to access `/admin` page, Spring Security will intercept the request and redirect to `/login`, and a default login form is displayed.

![spring-security-helloworld-annotation-login](http://www.mkyong.com/wp-content/uploads/2014/04/spring-security-helloworld-annotation-login.png)

7.3\. If username and password is incorrect, error messages will be displayed, and Spring will redirect to this URL `/login?error`.

![spring-security-helloworld-annotation-login-error](http://www.mkyong.com/wp-content/uploads/2014/04/spring-security-helloworld-annotation-login-error.png)

7.4\. If username and password is correct, Spring will redirect the request to the original requested URL and display the page.

![spring-security-helloworld-annotation-admin](http://www.mkyong.com/wp-content/uploads/2014/04/spring-security-helloworld-annotation-admin.png)

7.5\. For unauthorized user, Spring will display the 403 access denied page. For example, user “mkyong” or “dba” try to access the `/admin` URL.

![spring-security-helloworld-annotation-403](http://www.mkyong.com/wp-content/uploads/2014/04/spring-security-helloworld-annotation-403.png)

[http://www.mkyong.com/spring-security/spring-security-hello-world-annotation-example/](http://www.mkyong.com/spring-security/spring-security-hello-world-annotation-example/)
