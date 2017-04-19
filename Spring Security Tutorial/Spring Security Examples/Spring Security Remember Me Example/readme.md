![spring-security-remember-me](http://www.mkyong.com/wp-content/uploads/2014/05/spring-security-remember-me.png)

In this tutorial, we will show you how to implement “Remember Me” login feature in Spring Security, which means, the system will remember the user and perform automatic login even after the user’s session is expired.

Technologies and tools used :

1.  Spring 3.2.8.RELEASE
2.  Spring Security 3.2.3.RELEASE
3.  Spring JDBC 3.2.3.RELEASE
4.  Eclipse 4.2
5.  JDK 1.6
6.  Maven 3
7.  MySQL Server 5.6
8.  Tomcat 6 and 7 (Servlet 3.x)
9.  Test with Google Chrome

Some quick notes :

1.  In Spring Security, there are two approaches to implement “remember me” – Simple Hash-Based Token and Persistent Token Approach.
2.  To understand how the “remember me” works, please read these articles – [Spring remember me reference](http://docs.spring.io/spring-security/site/docs/3.2.3.RELEASE/reference/htmlsingle/#remember-me), [Persistent Login Cookie Best Practice](http://fishbowl.pastiche.org/2004/01/19/persistent_login_cookie_best_practice/), [Improved Persistent Login Cookie Best Practice](http://jaspan.com/improved_persistent_login_cookie_best_practice).
3.  This example is using “Persistent Token Approach”, refer to Spring’s `PersistentTokenBasedRememberMeServices`.
4.  This example is using MySQL and database authentication (via Spring JDBC).
5.  Table “persistent_logins” will be created to store the login token and series.

Project workflows :

1.  If user login with a “remember me” checked, the system will store a “remember me” cookie in the requested browser.
2.  If user’s browser provides a valid “remember me” cookie, the system will perform automatic login.
3.  If user is login via “remember me” cookies, to update the user detail, user need to type the username and password again (good practice to avoid stolen cookie to update user information.

_P.S This is a very high level of how “remember me” should work, for detail, please refer to the above links in “quick notes”._

## 1\. Project Demo

## 2\. Project Directory

Review the project directory structure.

![spring-security-remember-me-directory](http://www.mkyong.com/wp-content/uploads/2014/05/spring-security-remember-me-directory.png)

## 3\. MySQL Scripts

SQL scripts to create `users`, `user_roles` and `persistent_logins`.

    CREATE  TABLE users (
      username VARCHAR(45) NOT NULL ,
      password VARCHAR(45) NOT NULL ,
      enabled TINYINT NOT NULL DEFAULT 1 ,
      PRIMARY KEY (username));

    CREATE TABLE user_roles (
      user_role_id int(11) NOT NULL AUTO_INCREMENT,
      username varchar(45) NOT NULL,
      role varchar(45) NOT NULL,
      PRIMARY KEY (user_role_id),
      UNIQUE KEY uni_username_role (role,username),
      KEY fk_username_idx (username),
      CONSTRAINT fk_username FOREIGN KEY (username) REFERENCES users (username));

    INSERT INTO users(username,password,enabled)
    VALUES ('mkyong','123456', true);

    INSERT INTO user_roles (username, role)
    VALUES ('mkyong', 'ROLE_USER');
    INSERT INTO user_roles (username, role)
    VALUES ('mkyong', 'ROLE_ADMIN');

    CREATE TABLE persistent_logins (
        username varchar(64) not null,
        series varchar(64) not null,
        token varchar(64) not null,
        last_used timestamp not null,
        PRIMARY KEY (series)
    );

## 4\. Remember Me (XML Example)

To enable “remember me” in XML configuration, puts `remember-me` tag in the `http` like this :

spring-security.xml

    <!-- enable use-expressions -->
      <http auto-config="true" use-expressions="true">
        <intercept-url pattern="/admin**" access="hasRole('ROLE_ADMIN')" />

        <form-login login-page="/login"
    default-target-url="/welcome"
    authentication-failure-url="/login?error"
    username-parameter="username"
    password-parameter="password"
    login-processing-url="/auth/login_check"
    authentication-success-handler-ref="savedRequestAwareAuthenticationSuccessHandler" />

        <logout logout-success-url="/login?logout" delete-cookies="JSESSIONID" />
        <csrf />

        <!-- enable remember me -->
        <remember-me
            token-validity-seconds="1209600"
    remember-me-parameter="remember-me"
    data-source-ref="dataSource" />

      </http>

spring-database.xml

    <bean id="dataSource"
    class="org.springframework.jdbc.datasource.DriverManagerDataSource">

    <property name="driverClassName" value="com.mysql.jdbc.Driver" />
    <property name="url" value="jdbc:mysql://localhost:3306/test" />
    <property name="username" value="root" />
    <property name="password" value="password" />
      </bean>

      <!-- If request parameter "targetUrl" is existed, then forward to this url -->
      <!-- For update login form -->
      <bean id="savedRequestAwareAuthenticationSuccessHandler"
    class="org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler">
    <property name="targetUrlParameter" value="targetUrl" />
      </bean>

1.  **token-validity-seconds** – The expire date of “remember-me” cookie, in seconds. For example, 1209600 = 2 weeks (14 days), 86400 = 1 day, 18000 = 5 hours.
2.  **remember-me-parameter** – The name of the “check box”. Defaults to ‘_spring_security_remember_me’.
3.  **data-source-ref** – If this is specified, “Persistent Token Approach” will be used. Defaults to “Simple Hash-Based Token Approach”.

## 5\. Remember Me (Annotation Example)

The equivalent of annotations :

SecurityConfig.java

    package com.mkyong.config;

    import javax.sql.DataSource;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
    import org.springframework.security.config.annotation.web.builders.HttpSecurity;
    import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
    import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
    import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
    import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
    import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

    @Configuration
    @EnableWebSecurity
    public class SecurityConfig extends WebSecurityConfigurerAdapter {

    	@Autowired
    	DataSource dataSource;
    	//...

    	@Override
    	protected void configure(HttpSecurity http) throws Exception {

    	  http.authorizeRequests()
    	      .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
    	    .and()
    	      .formLogin()
    	        .successHandler(savedRequestAwareAuthenticationSuccessHandler())
    		.loginPage("/login")
    	        .failureUrl("/login?error")
    		.loginProcessingUrl("/auth/login_check")
    		.usernameParameter("username")
    		.passwordParameter("password")
    	    .and()
    		.logout().logoutSuccessUrl("/login?logout")
    	    .and()
    	        .csrf()
    	    .and()
    		.rememberMe().tokenRepository(persistentTokenRepository())
    		.tokenValiditySeconds(1209600);
    	}

    	@Bean
    	public PersistentTokenRepository persistentTokenRepository() {
    		JdbcTokenRepositoryImpl db = new JdbcTokenRepositoryImpl();
    		db.setDataSource(dataSource);
    		return db;
    	}

    	@Bean
    	public SavedRequestAwareAuthenticationSuccessHandler
                    savedRequestAwareAuthenticationSuccessHandler() {

                   SavedRequestAwareAuthenticationSuccessHandler auth
                        = new SavedRequestAwareAuthenticationSuccessHandler();
    		auth.setTargetUrlParameter("targetUrl");
    		return auth;
    	}

    }

_P.S In annotation configuration, the default http name for “remember me” check box is “remember-me”._

## 6.HTML / JSP Pages

6.1 In JSP, you can use Spring security tag `sec:authorize access="isRememberMe()"` to determine if this user is login by “remember me” cookies.

admin.jsp

    <%@taglib prefix="sec"
    	uri="http://www.springframework.org/security/tags"%>
    <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
    <%@page session="true"%>
    <html>
    <body>
    	<h1>Title : ${title}</h1>
    	<h1>Message : ${message}</h1>

    	<c:url value="/j_spring_security_logout" var="logoutUrl" />
    	<form action="${logoutUrl}" method="post" id="logoutForm">
    		<input type="hidden" name="${_csrf.parameterName}"
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

    	<sec:authorize access="isRememberMe()">
    		<h2># This user is login by "Remember Me Cookies".</h2>
    	</sec:authorize>

    	<sec:authorize access="isFullyAuthenticated()">
    		<h2># This user is login by username / password.</h2>
    	</sec:authorize>

    </body>
    </html>

6.2 A simple login form with “remember me” check box.

login.jsp

    <form name='loginForm'
    action="<c:url value='/auth/login_check?targetUrl=${targetUrl}' />"
    method='POST'>

    <table>
    <tr>
    	<td>User:</td>
    	<td><input type='text' name='username'></td>
    </tr>
    <tr>
    	<td>Password:</td>
    	<td><input type='password' name='password' /></td>
    </tr>

    <!-- if this is login for update, ignore remember me check -->
    <c:if test="${empty loginUpdate}">
    <tr>
    	<td></td>
    	<td>Remember Me: <input type="checkbox" name="remember-me" /></td>
    </tr>
    </c:if>

    <tr>
            <td colspan='2'><input name="submit" type="submit"
    	value="submit" /></td>
    </tr>

    </table>

    <input type="hidden" name="${_csrf.parameterName}"
    	value="${_csrf.token}" />

      </form>

6.3 Update page. Only user login by using password is allowed to access this page.

update.jsp

    <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
    <%@page session="true"%>
    <html>
    <body>
    	<h1>Title : Spring Security Remember Me Example - Update Form</h1>
    	<h1>Message : This page is for ROLE_ADMIN and fully authenticated only
                (Remember me cookie is not allowed!)</h1>

    	<h2>Update Account Information...</h2>
    </body>
    </html>

## 7\. Controller

Spring controller class, read the comment for self-explanatory.

MainController.java

    package com.mkyong.web.controller;

    import javax.servlet.http.HttpServletRequest;
    import javax.servlet.http.HttpSession;
    import org.springframework.security.authentication.RememberMeAuthenticationToken;
    import org.springframework.security.core.Authentication;
    import org.springframework.security.core.context.SecurityContextHolder;
    import org.springframework.stereotype.Controller;
    import org.springframework.util.StringUtils;
    import org.springframework.web.bind.annotation.RequestMapping;
    import org.springframework.web.bind.annotation.RequestMethod;
    import org.springframework.web.bind.annotation.RequestParam;
    import org.springframework.web.servlet.ModelAndView;

    @Controller
    public class MainController {

    	@RequestMapping(value = { "/", "/welcome**" }, method = RequestMethod.GET)
    	public ModelAndView defaultPage() {

    		ModelAndView model = new ModelAndView();
    		model.addObject("title", "Spring Security Remember Me");
    		model.addObject("message", "This is default page!");
    		model.setViewName("hello");
    		return model;

    	}

    	@RequestMapping(value = "/admin**", method = RequestMethod.GET)
    	public ModelAndView adminPage() {

    		ModelAndView model = new ModelAndView();
    		model.addObject("title", "Spring Security Remember Me");
    		model.addObject("message", "This page is for ROLE_ADMIN only!");
    		model.setViewName("admin");

    		return model;

    	}

    	/**
    	 * This update page is for user login with password only.
    	 * If user is login via remember me cookie, send login to ask for password again.
    	 * To avoid stolen remember me cookie to update info
    	 */
    	@RequestMapping(value = "/admin/update**", method = RequestMethod.GET)
    	public ModelAndView updatePage(HttpServletRequest request) {

    		ModelAndView model = new ModelAndView();

    		if (isRememberMeAuthenticated()) {
    			//send login for update
    			setRememberMeTargetUrlToSession(request);
    			model.addObject("loginUpdate", true);
    			model.setViewName("/login");

    		} else {
    			model.setViewName("update");
    		}

    		return model;

    	}

    	/**
    	 * both "normal login" and "login for update" shared this form.
    	 *
    	 */
    	@RequestMapping(value = "/login", method = RequestMethod.GET)
    	public ModelAndView login(@RequestParam(value = "error", required = false) String error,
    	  @RequestParam(value = "logout", required = false) String logout,
              HttpServletRequest request) {

    		ModelAndView model = new ModelAndView();
    		if (error != null) {
    			model.addObject("error", "Invalid username and password!");

    			//login form for update page
                            //if login error, get the targetUrl from session again.
    			String targetUrl = getRememberMeTargetUrlFromSession(request);
    			System.out.println(targetUrl);
    			if(StringUtils.hasText(targetUrl)){
    				model.addObject("targetUrl", targetUrl);
    				model.addObject("loginUpdate", true);
    			}

    		}

    		if (logout != null) {
    			model.addObject("msg", "You've been logged out successfully.");
    		}
    		model.setViewName("login");

    		return model;

    	}

    	/**
    	 * Check if user is login by remember me cookie, refer
    	 * org.springframework.security.authentication.AuthenticationTrustResolverImpl
    	 */
    	private boolean isRememberMeAuthenticated() {

    		Authentication authentication =
                        SecurityContextHolder.getContext().getAuthentication();
    		if (authentication == null) {
    			return false;
    		}

    		return RememberMeAuthenticationToken.class.isAssignableFrom(authentication.getClass());
    	}

    	/**
    	 * save targetURL in session
    	 */
    	private void setRememberMeTargetUrlToSession(HttpServletRequest request){
    		HttpSession session = request.getSession(false);
    		if(session!=null){
    			session.setAttribute("targetUrl", "/admin/update");
    		}
    	}

    	/**
    	 * get targetURL from session
    	 */
    	private String getRememberMeTargetUrlFromSession(HttpServletRequest request){
    		String targetUrl = "";
    		HttpSession session = request.getSession(false);
    		if(session!=null){
    			targetUrl = session.getAttribute("targetUrl")==null?""
                                 :session.getAttribute("targetUrl").toString();
    		}
    		return targetUrl;
    	}

    }

## 8\. Demo

8.1 Access protected page – _http://localhost:8080/spring-security-remember-me/admin_, the system will redirect user to a login form. Try to login with “remember-me” checked.

![spring-security-remember-me-example-0](http://www.mkyong.com/wp-content/uploads/2014/05/spring-security-remember-me-example-0.png)

![spring-security-remember-me-example-2](http://www.mkyong.com/wp-content/uploads/2014/05/spring-security-remember-me-example-2.png)

8.2 In Google Chrome, Settings -> Show advanced settings -> Privacy, Content Setting… -> “All cookies and site data” – there are two cookies for localhost, one for current session and one for “remember me” login cookies.

![spring-security-remember-me-example-1](http://www.mkyong.com/wp-content/uploads/2014/05/spring-security-remember-me-example-1.png)

8.3 Review table “persistent_logins”, username, series and token is stored.

![spring-security-remember-me-table](http://www.mkyong.com/wp-content/uploads/2014/05/spring-security-remember-me-table.png)

8.4 Restart the web application, go Chrome “All cookies and site data”, and remove the browser’s session “JSESSIONID”. Try to access the login page again. Now, the system will “remember you” and automatic login via the login cookies in your browser.

![spring-security-remember-me-example-3](http://www.mkyong.com/wp-content/uploads/2014/05/spring-security-remember-me-example-3.png)

8.5 Try to access the “update” page – _http://localhost:8080/spring-security-remember-me/admin/update_, if user is login by remember me cookies, the system will redirect user to login form again. This is a good practice to avoid stolen cookie to update user detail.

![spring-security-remember-me-example-4](http://www.mkyong.com/wp-content/uploads/2014/05/spring-security-remember-me-example-4.png)

8.6 Done.

![spring-security-remember-me-example-5](http://www.mkyong.com/wp-content/uploads/2014/05/spring-security-remember-me-example-5.png)

## 9\. Misc

Some important Spring Security classes to study :

1.  org.springframework.security.config.annotation.web.configurers.RememberMeConfigurer.java
2.  org.springframework.security.web.authentication.rememberme.AbstractRememberMeServices.java
3.  org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices.java
4.  org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices.java
5.  org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationFilter

[http://www.mkyong.com/spring-security/spring-security-remember-me-example/](http://www.mkyong.com/spring-security/spring-security-remember-me-example/)
