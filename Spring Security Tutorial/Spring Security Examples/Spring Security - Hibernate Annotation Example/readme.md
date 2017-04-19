![spring-hibernate-logo](http://www.mkyong.com/wp-content/uploads/2014/05/spring-hibernate-logo.png)

In this tutorial, previous [Spring Security + Hibernate4 XML](http://www.mkyong.com/spring-security/spring-security-hibernate-xml-example/) example will be reused, and convert it to a annotation-based example.

Technologies used :

1.  Spring 3.2.8.RELEASE
2.  Spring Security 3.2.3.RELEASE
3.  Hibernate 4.2.11.Final
4.  MySQL Server 5.6
5.  Tomcat 7 (Servlet 3.x container)

Quick Note :

1.  Create a session factory with `LocalSessionFactoryBuilder`
2.  Inject session factory into a UserDao
3.  Integrate UserDao into a custom `UserDetailsService`, to load users from the database.

## 1\. Project Directory

A final project directory structure.

![spring-security-hibernate-annotation-directory](http://www.mkyong.com/wp-content/uploads/2014/05/spring-security-hibernate-annotation-directory.png)

## 2\. User Model + Mapping File

Model classes and its’ annotation-based mapping file.

User.java

    package com.mkyong.users.model;

    import java.util.HashSet;
    import java.util.Set;
    import javax.persistence.Column;
    import javax.persistence.Entity;
    import javax.persistence.FetchType;
    import javax.persistence.Id;
    import javax.persistence.OneToMany;
    import javax.persistence.Table;

    @Entity
    @Table(name = "users", catalog = "test")
    public class User {

    	private String username;
    	private String password;
    	private boolean enabled;
    	private Set<UserRole> userRole = new HashSet<UserRole>(0);

    	public User() {
    	}

    	public User(String username, String password, boolean enabled) {
    		this.username = username;
    		this.password = password;
    		this.enabled = enabled;
    	}

    	public User(String username, String password,
    		boolean enabled, Set<UserRole> userRole) {
    		this.username = username;
    		this.password = password;
    		this.enabled = enabled;
    		this.userRole = userRole;
    	}

    	@Id
    	@Column(name = "username", unique = true,
    		nullable = false, length = 45)
    	public String getUsername() {
    		return this.username;
    	}

    	public void setUsername(String username) {
    		this.username = username;
    	}

    	@Column(name = "password",
    		nullable = false, length = 60)
    	public String getPassword() {
    		return this.password;
    	}

    	public void setPassword(String password) {
    		this.password = password;
    	}

    	@Column(name = "enabled", nullable = false)
    	public boolean isEnabled() {
    		return this.enabled;
    	}

    	public void setEnabled(boolean enabled) {
    		this.enabled = enabled;
    	}

    	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    	public Set<UserRole> getUserRole() {
    		return this.userRole;
    	}

    	public void setUserRole(Set<UserRole> userRole) {
    		this.userRole = userRole;
    	}

    }

UserRole.java

    package com.mkyong.users.model;

    import static javax.persistence.GenerationType.IDENTITY;
    import javax.persistence.Column;
    import javax.persistence.Entity;
    import javax.persistence.FetchType;
    import javax.persistence.GeneratedValue;
    import javax.persistence.Id;
    import javax.persistence.JoinColumn;
    import javax.persistence.ManyToOne;
    import javax.persistence.Table;
    import javax.persistence.UniqueConstraint;

    @Entity
    @Table(name = "user_roles", catalog = "test",
    	uniqueConstraints = @UniqueConstraint(
    		columnNames = { "role", "username" }))
    public class UserRole{

    	private Integer userRoleId;
    	private User user;
    	private String role;

    	public UserRole() {
    	}

    	public UserRole(User user, String role) {
    		this.user = user;
    		this.role = role;
    	}

    	@Id
    	@GeneratedValue(strategy = IDENTITY)
    	@Column(name = "user_role_id",
    		unique = true, nullable = false)
    	public Integer getUserRoleId() {
    		return this.userRoleId;
    	}

    	public void setUserRoleId(Integer userRoleId) {
    		this.userRoleId = userRoleId;
    	}

    	@ManyToOne(fetch = FetchType.LAZY)
    	@JoinColumn(name = "username", nullable = false)
    	public User getUser() {
    		return this.user;
    	}

    	public void setUser(User user) {
    		this.user = user;
    	}

    	@Column(name = "role", nullable = false, length = 45)
    	public String getRole() {
    		return this.role;
    	}

    	public void setRole(String role) {
    		this.role = role;
    	}

    }

## 3\. DAO Class

DAO classes, to load data from the database, via Hibernate.

UserDao.java

    package com.mkyong.users.dao;

    import com.mkyong.users.model.User;

    public interface UserDao {

    	User findByUserName(String username);

    }

UserDaoImpl.java

    package com.mkyong.users.dao;

    import java.util.ArrayList;
    import java.util.List;

    import org.hibernate.SessionFactory;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Repository;

    import com.mkyong.users.model.User;

    @Repository
    public class UserDaoImpl implements UserDao {

    	@Autowired
    	private SessionFactory sessionFactory;

    	@SuppressWarnings("unchecked")
    	public User findByUserName(String username) {

    		List<User> users = new ArrayList<User>();

    		users = sessionFactory.getCurrentSession()
    			.createQuery("from User where username=?")
    			.setParameter(0, username)
    			.list();

    		if (users.size() > 0) {
    			return users.get(0);
    		} else {
    			return null;
    		}

    	}

    }

## 4\. UserDetailsService

Uses `@Transactional` to declare a transactional method.

MyUserDetailsService.java

    package com.mkyong.users.service;

    import java.util.ArrayList;
    import java.util.HashSet;
    import java.util.List;
    import java.util.Set;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.security.core.GrantedAuthority;
    import org.springframework.security.core.authority.SimpleGrantedAuthority;
    import org.springframework.security.core.userdetails.User;
    import org.springframework.security.core.userdetails.UserDetails;
    import org.springframework.security.core.userdetails.UserDetailsService;
    import org.springframework.security.core.userdetails.UsernameNotFoundException;
    import org.springframework.stereotype.Service;
    import org.springframework.transaction.annotation.Transactional;

    import com.mkyong.users.dao.UserDao;
    import com.mkyong.users.model.UserRole;

    @Service("userDetailsService")
    public class MyUserDetailsService implements UserDetailsService {

    	//get user from the database, via Hibernate
    	@Autowired
    	private UserDao userDao;

    	@Transactional(readOnly=true)
    	@Override
    	public UserDetails loadUserByUsername(final String username)
    		throws UsernameNotFoundException {

    		com.mkyong.users.model.User user = userDao.findByUserName(username);
    		List<GrantedAuthority> authorities =
                                          buildUserAuthority(user.getUserRole());

    		return buildUserForAuthentication(user, authorities);

    	}

    	// Converts com.mkyong.users.model.User user to
    	// org.springframework.security.core.userdetails.User
    	private User buildUserForAuthentication(com.mkyong.users.model.User user,
    		List<GrantedAuthority> authorities) {
    		return new User(user.getUsername(), user.getPassword(),
    			user.isEnabled(), true, true, true, authorities);
    	}

    	private List<GrantedAuthority> buildUserAuthority(Set<UserRole> userRoles) {

    		Set<GrantedAuthority> setAuths = new HashSet<GrantedAuthority>();

    		// Build user's authorities
    		for (UserRole userRole : userRoles) {
    			setAuths.add(new SimpleGrantedAuthority(userRole.getRole()));
    		}

    		List<GrantedAuthority> Result = new ArrayList<GrantedAuthority>(setAuths);

    		return Result;
    	}

    }

## 5\. Spring Security Annotation

Declares and binds everything with annotations, read the comments, it should be self-explanatory.

SecurityConfig.java

    package com.mkyong.config;

    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.beans.factory.annotation.Qualifier;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
    import org.springframework.security.config.annotation.web.builders.HttpSecurity;
    import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
    import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
    import org.springframework.security.core.userdetails.UserDetailsService;
    import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
    import org.springframework.security.crypto.password.PasswordEncoder;

    @Configuration
    @EnableWebSecurity
    public class SecurityConfig extends WebSecurityConfigurerAdapter {

    	@Autowired
    	@Qualifier("userDetailsService")
    	UserDetailsService userDetailsService;

    	@Autowired
    	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    	}

    	@Override
    	protected void configure(HttpSecurity http) throws Exception {

    	    http.authorizeRequests().antMatchers("/admin/**")
    		.access("hasRole('ROLE_ADMIN')").and().formLogin()
    		.loginPage("/login").failureUrl("/login?error")
    		.usernameParameter("username")
    		.passwordParameter("password")
    		.and().logout().logoutSuccessUrl("/login?logout")
    		.and().csrf()
    		.and().exceptionHandling().accessDeniedPage("/403");
    	}

    	@Bean
    	public PasswordEncoder passwordEncoder(){
    		PasswordEncoder encoder = new BCryptPasswordEncoder();
    		return encoder;
    	}

    }

Uses `LocalSessionFactoryBuilder` to create a session factory.

AppConfig.java

    package com.mkyong.config;

    import java.util.Properties;
    import org.apache.commons.dbcp.BasicDataSource;
    import org.hibernate.SessionFactory;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.ComponentScan;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.context.annotation.Import;
    import org.springframework.orm.hibernate4.HibernateTransactionManager;
    import org.springframework.orm.hibernate4.LocalSessionFactoryBuilder;
    import org.springframework.transaction.annotation.EnableTransactionManagement;
    import org.springframework.web.servlet.config.annotation.EnableWebMvc;
    import org.springframework.web.servlet.view.InternalResourceViewResolver;
    import org.springframework.web.servlet.view.JstlView;

    @EnableWebMvc
    @Configuration
    @ComponentScan({ "com.mkyong.*" })
    @EnableTransactionManagement
    @Import({ SecurityConfig.class })
    public class AppConfig {

            @Bean
            public SessionFactory sessionFactory() {
                    LocalSessionFactoryBuilder builder =
    			new LocalSessionFactoryBuilder(dataSource());
                    builder.scanPackages("com.mkyong.users.model")
                          .addProperties(getHibernateProperties());

                    return builder.buildSessionFactory();
            }

    	private Properties getHibernateProperties() {
                    Properties prop = new Properties();
                    prop.put("hibernate.format_sql", "true");
                    prop.put("hibernate.show_sql", "true");
                    prop.put("hibernate.dialect",
                        "org.hibernate.dialect.MySQL5Dialect");
                    return prop;
            }

    	@Bean(name = "dataSource")
    	public BasicDataSource dataSource() {

    		BasicDataSource ds = new BasicDataSource();
    	        ds.setDriverClassName("com.mysql.jdbc.Driver");
    		ds.setUrl("jdbc:mysql://localhost:3306/test");
    		ds.setUsername("root");
    		return ds;
    	}

    	//Create a transaction manager
    	@Bean
            public HibernateTransactionManager txManager() {
                    return new HibernateTransactionManager(sessionFactory());
            }

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

Done.

## 6\. Project Demo

The following video demo is for the [Spring Security database login](http://www.mkyong.com/spring-security/spring-security-form-login-using-database/) tutorial. Since this tutorial is generating the same output, so the video demo is reused.

6.1 Access a password protected page :_ http://localhost:8080/spring-security-hibernate-annotation/admin_ , a login page is displayed.

![spring-security-hibernate-annotation1](http://www.mkyong.com/wp-content/uploads/2014/05/spring-security-hibernate-annotation1.png)

6.2 Enter user “mkyong” and password “123456”.

![spring-security-hibernate-annotation2](http://www.mkyong.com/wp-content/uploads/2014/05/spring-security-hibernate-annotation2.png)

6.3 Try access `/admin` page with user “alex” and password “123456”, a 403 page will be displayed.

![spring-security-hibernate-annotation3](http://www.mkyong.com/wp-content/uploads/2014/05/spring-security-hibernate-annotation3.png)

[http://www.mkyong.com/spring-security/spring-security-hibernate-annotation-example/](http://www.mkyong.com/spring-security/spring-security-hibernate-annotation-example/)
