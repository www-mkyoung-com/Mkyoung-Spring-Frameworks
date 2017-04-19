In Spring Security, if non authorized user try to access a protected page, a default “**http 403 access denied**” will be displayed :

![spring-security-403-default](http://www.mkyong.com/wp-content/uploads/2011/08/spring-security-403-default.png)

In this tutorial, we will show you how to customize 403 access denied page in Spring Security.

## 1\. Spring Security Configuration

Review a configuration, if “alex” try to access `/admin` page, above 403 access denied page will be displayed.

Spring-Security.xml

    <http auto-config="true">
    <access-denied-handler error-page="/403" />
    <intercept-url pattern="/admin**" access="ROLE_ADMIN" />
      </http>

      <authentication-manager>
    <authentication-provider>
      <user-service>
    	<user name="alex" password="123456" authorities="ROLE_USER" />
    	<user name="mkyong" password="123456" authorities="ROLE_USER, ROLE_ADMIN" />
      </user-service>
    </authentication-provider>
      </authentication-manager>

## 2\. Solution – Customize 403 Page

2.1 Create a new 403 page.

403.jsp

    <html>
    <body>
    	<h1>HTTP Status 403 - Access is denied</h1>
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
    </div><h2>${msg}</h2>
    </body>
    </html>

2.2\. To display above page, add a `error-page` like the following :

Spring-Security.xml

    <http auto-config="true">
    	<access-denied-handler error-page="/403" />
    	<intercept-url pattern="/admin**" access="ROLE_ADMIN" />
    </http>

2.3 In a controller class, add a mapping for “/403” url :

HelloController.java

    package com.mkyong.web.controller;

    import java.security.Principal;
    import org.springframework.stereotype.Controller;
    import org.springframework.web.bind.annotation.RequestMapping;
    import org.springframework.web.bind.annotation.RequestMethod;
    import org.springframework.web.servlet.ModelAndView;

    @Controller
    public class HelloController {

    	// for 403 access denied page
    	@RequestMapping(value = "/403", method = RequestMethod.GET)
    	public ModelAndView accesssDenied(Principal user) {

    		ModelAndView model = new ModelAndView();

    		if (user != null) {
    			model.addObject("msg", "Hi " + user.getName()
    			+ ", you do not have permission to access this page!");
    		} else {
    			model.addObject("msg",
    			"You do not have permission to access this page!");
    		}

    		model.setViewName("403");
    		return model;

    	}

    }

Done.

For annotation users, use this `.exceptionHandling().accessDeniedPage("/403")`.

SecurityConfig.java

    package com.mkyong.config;

    import org.springframework.context.annotation.Configuration;
    import org.springframework.security.config.annotation.web.builders.HttpSecurity;
    import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
    import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

    @Configuration
    @EnableWebSecurity
    public class SecurityConfig extends WebSecurityConfigurerAdapter {

    	@Override
    	protected void configure(HttpSecurity http) throws Exception {

    	 http.authorizeRequests()
    	    .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
    	    .and().formLogin()
    		.loginPage("/login").failureUrl("/login?error")
    		.usernameParameter("username")
    		.passwordParameter("password")
    	    .and().logout().logoutSuccessUrl("/login?logout")
    	    .and()
    		.exceptionHandling().accessDeniedPage("/403")
    	}
    }

## 3\. AccessDeniedHandler

In additional, you can create a custom `AccessDeniedHandler` to perform some business logics before pass the URL to `/403`mapping.

MyAccessDeniedHandler.java

    package com.mkyong.web.exception;

    import java.io.IOException;
    import javax.servlet.ServletException;
    import javax.servlet.http.HttpServletRequest;
    import javax.servlet.http.HttpServletResponse;
    import org.springframework.security.access.AccessDeniedException;
    import org.springframework.security.web.access.AccessDeniedHandler;

    public class MyAccessDeniedHandler implements AccessDeniedHandler {

    	private String errorPage;

    	public MyAccessDeniedHandler() {
    	}

    	public MyAccessDeniedHandler(String errorPage) {
    		this.errorPage = errorPage;
    	}

    	public String getErrorPage() {
    		return errorPage;
    	}

    	public void setErrorPage(String errorPage) {
    		this.errorPage = errorPage;
    	}

    	@Override
    	public void handle(HttpServletRequest request, HttpServletResponse response,
    		AccessDeniedException accessDeniedException)
                    throws IOException, ServletException {

    		//do some business logic, then redirect to errorPage url
    		response.sendRedirect(errorPage);

    	}

    }

Add a `ref` to http tag.

Spring-Security.xml

    <http auto-config="true">
    	<access-denied-handler ref="my403" />
    	<intercept-url pattern="/admin**" access="ROLE_ADMIN" />
    </http>

    <beans:bean id="my403"
    	class="com.mkyong.web.exception.MyAccessDeniedHandler">
    	<beans:property name="errorPage" value="403" />
    </beans:bean>

Done.

## 4\. Demo

When “alex” try to access `/admin` page, above customizing 403 access denied page will be displayed.

4.1 If using `error-page`, url will be displayed like this :

_http://localhost:8080/spring-security-403-access-denied/admin_

![spring-security-403-example1](http://www.mkyong.com/wp-content/uploads/2011/08/spring-security-403-example1.png)

4.2 If using custom access denied handler `ref`, url will be displayed like this :

_http://localhost:8080/spring-security-403-access-denied/403_

![spring-security-403-example2](http://www.mkyong.com/wp-content/uploads/2011/08/spring-security-403-example2.png)

[http://www.mkyong.com/spring-security/customize-http-403-access-denied-page-in-spring-security/](http://www.mkyong.com/spring-security/customize-http-403-access-denied-page-in-spring-security/)
