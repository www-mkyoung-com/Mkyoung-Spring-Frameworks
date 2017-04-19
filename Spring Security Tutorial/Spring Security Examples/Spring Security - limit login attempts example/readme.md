![spring-security-limit-login-attempts-locked](http://www.mkyong.com/wp-content/uploads/2014/04/spring-security-limit-login-attempts-locked.png)

In this tutorial, we will show you how to limit login attempts in Spring Security, which means, if a user try to login with an invalid password more than 3 times, the system will lock the user and make it unable to login anymore.

Technologies and tools used :

1.  Spring 3.2.8.RELEASE
2.  Spring Security 3.2.3.RELEASE
3.  Spring JDBC 3.2.3.RELEASE
4.  Eclipse 4.2
5.  JDK 1.6
6.  Maven 3
7.  MySQL Server 5.6
8.  Tomcat 7 (Servlet 3.x)

Some quick notes for this tutorial :

1.  MySQL database will be used.
2.  This is a Spring Security annotation-based example.
3.  Create a “users” table with column “accountNonLocked”.
4.  Create a “user_attempts” table to store the invalid login attempts.
5.  Spring JDBC will be used.
6.  Display custom error messages based on the returned exception.
7.  Create a customized “authenticationProvider”

## 1\. Solution

Review the existing Spring Security’s authentication class, the “locked” feature is already implemented. To enable the limit login attempts, you need to set the `UserDetails.isAccountNonLocked` to false.

DaoAuthenticationProvider.java

    package org.springframework.security.authentication.dao;

    public class DaoAuthenticationProvider
    	extends AbstractUserDetailsAuthenticationProvider {
    	//...
    }

AbstractUserDetailsAuthenticationProvider.java

    package org.springframework.security.authentication.dao;

    public abstract class AbstractUserDetailsAuthenticationProvider
        implements AuthenticationProvider, InitializingBean,
        MessageSourceAware {

        private class DefaultPreAuthenticationChecks implements UserDetailsChecker {
            public void check(UserDetails user) {
              if (!user.isAccountNonLocked()) {
                  logger.debug("User account is locked");

              throw new LockedException(
                  messages.getMessage("AbstractUserDetailsAuthenticationProvider.locked",
                     "User account is locked"), user);
              }
               //...
            }
        }

## 2\. Project Demo

## 3\. Project Directory

Review the final project structure (Annotation-based):

![spring-security-limit-login-attempts-directory](http://www.mkyong.com/wp-content/uploads/2014/04/spring-security-limit-login-attempts-directory.png)

## 4\. Database

Here are the MySQL scripts to create the users, user_roles and user_attempts tables.

4.1 Create a “users” table, with column “accountNonLocked”.

users.sql

    CREATE  TABLE users (
      username VARCHAR(45) NOT NULL ,
      password VARCHAR(45) NOT NULL ,
      enabled TINYINT NOT NULL DEFAULT 1 ,
      accountNonExpired TINYINT NOT NULL DEFAULT 1 ,
      accountNonLocked TINYINT NOT NULL DEFAULT 1 ,
      credentialsNonExpired TINYINT NOT NULL DEFAULT 1,
      PRIMARY KEY (username));

4.2 Create a “user_roles” table.

user_roles.sql

    CREATE TABLE user_roles (
      user_role_id int(11) NOT NULL AUTO_INCREMENT,
      username varchar(45) NOT NULL,
      role varchar(45) NOT NULL,
      PRIMARY KEY (user_role_id),
      UNIQUE KEY uni_username_role (role,username),
      KEY fk_username_idx (username),
      CONSTRAINT fk_username FOREIGN KEY (username) REFERENCES users (username));

4.3 Create a “user_attempts” table.

user_attempts.sql

    CREATE TABLE user_attempts (
      id int(11) NOT NULL AUTO_INCREMENT,
      username varchar(45) NOT NULL,
      attempts varchar(45) NOT NULL,
      lastModified datetime NOT NULL,
      PRIMARY KEY (id)
    );

4.4 Inserts an user for testing.

    INSERT INTO users(username,password,enabled)
    VALUES ('mkyong','123456', true);

    INSERT INTO user_roles (username, role)
    VALUES ('mkyong', 'ROLE_USER');
    INSERT INTO user_roles (username, role)
    VALUES ('mkyong', 'ROLE_ADMIN');

## 5\. UserAttempts Classes

This class represents the data of “user_attempts” table.

UserAttempts.java

    package com.mkyong.users.model;

    import java.util.Date;

    public class UserAttempts {

    	private int id;
    	private String username;
    	private int attempts;
    	private Date lastModified;

    	//getter and setter

    }

## 6\. DAO Classes

A DAO class to update the invalid login attempts, read the comments for self-explanatory.

UserDetailsDao.java

    package com.mkyong.users.dao;

    import com.mkyong.users.model.UserAttempts;

    public interface UserDetailsDao {

    	void updateFailAttempts(String username);
    	void resetFailAttempts(String username);
    	UserAttempts getUserAttempts(String username);

    }

UserDetailsDaoImpl.java

    package com.mkyong.users.dao;

    import java.sql.ResultSet;
    import java.sql.SQLException;
    import java.util.Date;

    import javax.annotation.PostConstruct;
    import javax.sql.DataSource;

    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.dao.EmptyResultDataAccessException;
    import org.springframework.jdbc.core.RowMapper;
    import org.springframework.jdbc.core.support.JdbcDaoSupport;
    import org.springframework.security.authentication.LockedException;
    import org.springframework.stereotype.Repository;

    import com.mkyong.users.model.UserAttempts;

    @Repository
    public class UserDetailsDaoImpl extends JdbcDaoSupport implements UserDetailsDao {

    	private static final String SQL_USERS_UPDATE_LOCKED = "UPDATE USERS SET accountNonLocked = ? WHERE username = ?";
    	private static final String SQL_USERS_COUNT = "SELECT count(*) FROM USERS WHERE username = ?";

    	private static final String SQL_USER_ATTEMPTS_GET = "SELECT * FROM USER_ATTEMPTS WHERE username = ?";
    	private static final String SQL_USER_ATTEMPTS_INSERT = "INSERT INTO USER_ATTEMPTS (USERNAME, ATTEMPTS, LASTMODIFIED) VALUES(?,?,?)";
    	private static final String SQL_USER_ATTEMPTS_UPDATE_ATTEMPTS = "UPDATE USER_ATTEMPTS SET attempts = attempts + 1, lastmodified = ? WHERE username = ?";
    	private static final String SQL_USER_ATTEMPTS_RESET_ATTEMPTS = "UPDATE USER_ATTEMPTS SET attempts = 0, lastmodified = null WHERE username = ?";

    	private static final int MAX_ATTEMPTS = 3;

    	@Autowired
    	private DataSource dataSource;

    	@PostConstruct
    	private void initialize() {
    		setDataSource(dataSource);
    	}

    	@Override
    	public void updateFailAttempts(String username) {

    	  UserAttempts user = getUserAttempts(username);
    	  if (user == null) {
    		if (isUserExists(username)) {
    			// if no record, insert a new
    			getJdbcTemplate().update(SQL_USER_ATTEMPTS_INSERT, new Object[] { username, 1, new Date() });
    		}
    	  } else {

    		if (isUserExists(username)) {
    			// update attempts count, +1
    			getJdbcTemplate().update(SQL_USER_ATTEMPTS_UPDATE_ATTEMPTS, new Object[] { new Date(), username});
    		}

    		if (user.getAttempts() + 1 >= MAX_ATTEMPTS) {
    			// locked user
    			getJdbcTemplate().update(SQL_USERS_UPDATE_LOCKED, new Object[] { false, username });
    			// throw exception
    			throw new LockedException("User Account is locked!");
    		}

    	  }

    	}

    	@Override
    	public UserAttempts getUserAttempts(String username) {

    	  try {

    		UserAttempts userAttempts = getJdbcTemplate().queryForObject(SQL_USER_ATTEMPTS_GET,
    			new Object[] { username }, new RowMapper<UserAttempts>() {
    			public UserAttempts mapRow(ResultSet rs, int rowNum) throws SQLException {

    				UserAttempts user = new UserAttempts();
    				user.setId(rs.getInt("id"));
    				user.setUsername(rs.getString("username"));
    				user.setAttempts(rs.getInt("attempts"));
    				user.setLastModified(rs.getDate("lastModified"));

    				return user;
    			}

    		});
    		return userAttempts;

    	  } catch (EmptyResultDataAccessException e) {
    		return null;
    	  }

    	}

    	@Override
    	public void resetFailAttempts(String username) {

    	  getJdbcTemplate().update(
                 SQL_USER_ATTEMPTS_RESET_ATTEMPTS, new Object[] { username });

    	}

    	private boolean isUserExists(String username) {

    	  boolean result = false;

    	  int count = getJdbcTemplate().queryForObject(
                                SQL_USERS_COUNT, new Object[] { username }, Integer.class);
    	  if (count > 0) {
    		result = true;
    	  }

    	  return result;
    	}

    }

## 7\. UserDetailsService

By default, `JdbcDaoImpl` will always set `accountNonLocked` to true, which is not what we want. Review the source code.

JdbcDaoImpl.java

    package org.springframework.security.core.userdetails.jdbc;

    public class JdbcDaoImpl extends JdbcDaoSupport implements UserDetailsService {
      //...
      protected List<UserDetails> loadUsersByUsername(String username) {
    	return getJdbcTemplate().query(usersByUsernameQuery, new String[] {username}, new RowMapper<UserDetails>() {
    	  public UserDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
    		String username = rs.getString(1);
    		String password = rs.getString(2);
    		boolean enabled = rs.getBoolean(3);
    		return new User(username, password, enabled, true, true, true, AuthorityUtils.NO_AUTHORITIES);
    	  }

      });
    }

To save development time, you can extend the `JdbcDaoImpl` and override both `loadUsersByUsername` and `createUserDetails` to get the customized `UserDetails`.

CustomUserDetailsService.java

    package com.mkyong.users.service;

    import java.sql.ResultSet;
    import java.sql.SQLException;
    import java.util.List;

    import javax.annotation.PostConstruct;
    import javax.sql.DataSource;

    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.beans.factory.annotation.Value;
    import org.springframework.jdbc.core.RowMapper;
    import org.springframework.security.core.GrantedAuthority;
    import org.springframework.security.core.authority.AuthorityUtils;
    import org.springframework.security.core.userdetails.User;
    import org.springframework.security.core.userdetails.UserDetails;
    import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
    import org.springframework.stereotype.Service;

    /**
     * Reference org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl
     *
     * @author mkyong
     *
     */
    @Service("userDetailsService")
    public class CustomUserDetailsService extends JdbcDaoImpl {

    	@Autowired
    	private DataSource dataSource;

    	@PostConstruct
    	private void initialize() {
    		setDataSource(dataSource);
    	}

    	@Override
    	@Value("select * from users where username = ?")
    	public void setUsersByUsernameQuery(String usersByUsernameQueryString) {
    		super.setUsersByUsernameQuery(usersByUsernameQueryString);
    	}

    	@Override
    	@Value("select username, role from user_roles where username =?")
    	public void setAuthoritiesByUsernameQuery(String queryString) {
    		super.setAuthoritiesByUsernameQuery(queryString);
    	}

    	//override to get accountNonLocked
    	@Override
    	public List<UserDetails> loadUsersByUsername(String username) {
    	  return getJdbcTemplate().query(super.getUsersByUsernameQuery(), new String[] { username },
    		new RowMapper<UserDetails>() {
    		  public UserDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
    			String username = rs.getString("username");
    			String password = rs.getString("password");
    			boolean enabled = rs.getBoolean("enabled");
    			boolean accountNonExpired = rs.getBoolean("accountNonExpired");
    			boolean credentialsNonExpired = rs.getBoolean("credentialsNonExpired");
    			boolean accountNonLocked = rs.getBoolean("accountNonLocked");

    			return new User(username, password, enabled, accountNonExpired, credentialsNonExpired,
    				accountNonLocked, AuthorityUtils.NO_AUTHORITIES);
    		  }

    	  });
    	}

    	//override to pass accountNonLocked
    	@Override
    	public UserDetails createUserDetails(String username, UserDetails userFromUserQuery,
    			List<GrantedAuthority> combinedAuthorities) {
    		String returnUsername = userFromUserQuery.getUsername();

    		if (super.isUsernameBasedPrimaryKey()) {
    		  returnUsername = username;
    		}

    		return new User(returnUsername, userFromUserQuery.getPassword(),
                           userFromUserQuery.isEnabled(),
    		       userFromUserQuery.isAccountNonExpired(),
                           userFromUserQuery.isCredentialsNonExpired(),
    			userFromUserQuery.isAccountNonLocked(), combinedAuthorities);
    	}

    }

## 8\. DaoAuthenticationProvider

Create a custom authentication provider, for every invalid login attempts, update to user_attempts table and throws `LockedException` if the maximum attempts is hit.

LimitLoginAuthenticationProvider.java

    package com.mkyong.web.handler;

    import java.util.Date;

    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.beans.factory.annotation.Qualifier;
    import org.springframework.security.authentication.BadCredentialsException;
    import org.springframework.security.authentication.LockedException;
    import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
    import org.springframework.security.core.Authentication;
    import org.springframework.security.core.AuthenticationException;
    import org.springframework.security.core.userdetails.UserDetailsService;
    import org.springframework.stereotype.Component;

    import com.mkyong.users.dao.UserDetailsDao;
    import com.mkyong.users.model.UserAttempts;

    @Component("authenticationProvider")
    public class LimitLoginAuthenticationProvider extends DaoAuthenticationProvider {

    	@Autowired
    	UserDetailsDao userDetailsDao;

    	@Autowired
    	@Qualifier("userDetailsService")
    	@Override
    	public void setUserDetailsService(UserDetailsService userDetailsService) {
    		super.setUserDetailsService(userDetailsService);
    	}

    	@Override
    	public Authentication authenticate(Authentication authentication)
              throws AuthenticationException {

    	  try {

    		Authentication auth = super.authenticate(authentication);

    		//if reach here, means login success, else an exception will be thrown
    		//reset the user_attempts
    		userDetailsDao.resetFailAttempts(authentication.getName());

    		return auth;

    	  } catch (BadCredentialsException e) {

    		//invalid login, update to user_attempts
    		userDetailsDao.updateFailAttempts(authentication.getName());
    		throw e;

    	  } catch (LockedException e){

    		//this user is locked!
    		String error = "";
    		UserAttempts userAttempts =
                        userDetailsDao.getUserAttempts(authentication.getName());

                   if(userAttempts!=null){
    			Date lastAttempts = userAttempts.getLastModified();
    			error = "User account is locked! <br><br>Username : "
                               + authentication.getName() + "<br>Last Attempts : " + lastAttempts;
    		}else{
    			error = e.getMessage();
    		}

    	  throw new LockedException(error);
    	}

    	}

    }

## 9\. Spring Controller

A standard controller class, refer to the `login` method, it shows you how to play around with the session value – “**SPRING_SECURITY_LAST_EXCEPTION**“, and customize the error message.

MainController.java

    package com.mkyong.web.controller;

    import javax.servlet.http.HttpServletRequest;

    import org.springframework.security.authentication.AnonymousAuthenticationToken;
    import org.springframework.security.authentication.BadCredentialsException;
    import org.springframework.security.authentication.LockedException;
    import org.springframework.security.core.Authentication;
    import org.springframework.security.core.context.SecurityContextHolder;
    import org.springframework.security.core.userdetails.UserDetails;
    import org.springframework.stereotype.Controller;
    import org.springframework.web.bind.annotation.RequestMapping;
    import org.springframework.web.bind.annotation.RequestMethod;
    import org.springframework.web.bind.annotation.RequestParam;
    import org.springframework.web.servlet.ModelAndView;

    @Controller
    public class MainController {

    	@RequestMapping(value = { "/", "/welcome**" }, method = RequestMethod.GET)
    	public ModelAndView defaultPage() {

    		ModelAndView model = new ModelAndView();
    		model.addObject("title", "Spring Security Limit Login - Annotation");
    		model.addObject("message", "This is default page!");
    		model.setViewName("hello");
    		return model;

    	}

    	@RequestMapping(value = "/admin**", method = RequestMethod.GET)
    	public ModelAndView adminPage() {

    		ModelAndView model = new ModelAndView();
    		model.addObject("title", "Spring Security Limit Login - Annotation");
    		model.addObject("message", "This page is for ROLE_ADMIN only!");
    		model.setViewName("admin");

    		return model;

    	}

    	@RequestMapping(value = "/login", method = RequestMethod.GET)
    	public ModelAndView login(
                    @RequestParam(value = "error", required = false) String error,
    		@RequestParam(value = "logout", required = false) String logout,
                    HttpServletRequest request) {

    		ModelAndView model = new ModelAndView();
    		if (error != null) {
    			model.addObject("error",
                               getErrorMessage(request, "SPRING_SECURITY_LAST_EXCEPTION"));
    		}

    		if (logout != null) {
    			model.addObject("msg", "You've been logged out successfully.");
    		}
    		model.setViewName("login");

    		return model;

    	}

    	//customize the error message
    	private String getErrorMessage(HttpServletRequest request, String key){

    		Exception exception =
                       (Exception) request.getSession().getAttribute(key);

    		String error = "";
    		if (exception instanceof BadCredentialsException) {
    			error = "Invalid username and password!";
    		}else if(exception instanceof LockedException) {
    			error = exception.getMessage();
    		}else{
    			error = "Invalid username and password!";
    		}

    		return error;
    	}

    	// for 403 access denied page
    	@RequestMapping(value = "/403", method = RequestMethod.GET)
    	public ModelAndView accesssDenied() {

    		ModelAndView model = new ModelAndView();

    		// check if user is login
    		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    		if (!(auth instanceof AnonymousAuthenticationToken)) {
    			UserDetails userDetail = (UserDetails) auth.getPrincipal();
    			System.out.println(userDetail);

    			model.addObject("username", userDetail.getUsername());

    		}

    		model.setViewName("403");
    		return model;

    	}

    }

## 10\. Spring Security Configuration

Attached your customized `authenticationProvider`.

SecurityConfig.java

    package com.mkyong.config;

    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.beans.factory.annotation.Qualifier;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.security.authentication.AuthenticationProvider;
    import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
    import org.springframework.security.config.annotation.web.builders.HttpSecurity;
    import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
    import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

    @Configuration
    @EnableWebSecurity
    public class SecurityConfig extends WebSecurityConfigurerAdapter {

    	@Autowired
    	@Qualifier("authenticationProvider")
    	AuthenticationProvider authenticationProvider;

    	@Autowired
    	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    		auth.authenticationProvider(authenticationProvider);
    	}

    	@Override
    	protected void configure(HttpSecurity http) throws Exception {

    		http.authorizeRequests().antMatchers("/admin/**")
    		  .access("hasRole('ROLE_USER')").and().formLogin()
    		  .loginPage("/login").failureUrl("/login?error")
    			.usernameParameter("username")
    			.passwordParameter("password")
    		  .and().logout().logoutSuccessUrl("/login?logout").and().csrf();
    	}
    }

Done.

## 11\. Demo

Demo page – http://localhost:8080/spring-security-limit-login-annotation/admin

11.1, First invalid login attempts, a normal error message will be displayed.

![spring-security-limit-login-attempts-1](http://www.mkyong.com/wp-content/uploads/2014/04/spring-security-limit-login-attempts-1.png)

11.2, If the maximum number of invalid login attempts are hit, error message “User account is locked” will be displayed.

![spring-security-limit-login-attempts-locked](http://www.mkyong.com/wp-content/uploads/2014/04/spring-security-limit-login-attempts-locked.png)

11.3, If user in “locked” status, and still try to login again. Locked detail will be displayed.

![spring-security-limit-login-attempts-locked-detail](http://www.mkyong.com/wp-content/uploads/2014/04/spring-security-limit-login-attempts-locked-detail.png)

11.4 Review the “users” table, if “accountNonLocked” = 0 or false, means this user is in locked status.

![spring-security-limit-login-attempts-locked-database](http://www.mkyong.com/wp-content/uploads/2014/04/spring-security-limit-login-attempts-locked-database.png)

[http://www.mkyong.com/spring-security/spring-security-limit-login-attempts-example/](http://www.mkyong.com/spring-security/spring-security-limit-login-attempts-example/)
