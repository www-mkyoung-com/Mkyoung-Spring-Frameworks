![spring-hibernate-logo](http://www.mkyong.com/wp-content/uploads/2014/05/spring-hibernate-logo.png)

In this tutorial, we will show you how to integrate Hibernate 4 in Spring Security, XML configuration example.

**Note**  
For annotation version, please read this [Spring Security + Hibernate Annotation Example](http://www.mkyong.com/spring-security/spring-security-hibernate-annotation-example/).

Technologies used :

1.  Spring 3.2.8.RELEASE
2.  Spring Security 3.2.3.RELEASE
3.  Hibernate 4.2.11.Final
4.  MySQL Server 5.6
5.  JDK 1.6
6.  Maven 3
7.  Eclipse 4.3

Quick Notes

1.  Create a session factory with `hibernate4.LocalSessionFactoryBean`
2.  Inject session factory into a UserDao
3.  To integrate with Spring Security, create a class that implements the `UserDetailsService` interface, and loads the User with UserDao
4.  Transaction manager must be declared, else Hibernate won’t work in Spring

## 1\. Project Directory

A final project directory structure.

![spring-security-hibernate-directory](http://www.mkyong.com/wp-content/uploads/2014/05/spring-security-hibernate-directory.png)

## 2\. Project Dependency

List of the project’s dependencies in POM file.

pom.xml

    <properties>
    	<spring.version>3.2.8.RELEASE</spring.version>
    	<spring.security.version>3.2.3.RELEASE</spring.security.version>
    	<jstl.version>1.2</jstl.version>
    	<mysql.connector.version>5.1.30</mysql.connector.version>
    	<logback.version>1.1.2</logback.version>
    	<slf4j.version>1.7.6</slf4j.version>
    	<hibernate.version>4.2.11.Final</hibernate.version>
    	<dbcp.version>1.4</dbcp.version>
    	<servletapi.version>2.5</servletapi.version>
    </properties>

    <dependencies>

    	<!-- database pool -->
    	<dependency>
    		<groupId>commons-dbcp</groupId>
    		<artifactId>commons-dbcp</artifactId>
    		<version>${dbcp.version}</version>
    	</dependency>

    	<!-- Hibernate ORM -->
    	<dependency>
    		<groupId>org.hibernate</groupId>
    		<artifactId>hibernate-core</artifactId>
    		<version>${hibernate.version}</version>
    	</dependency>

    	<!-- Spring 3 dependencies -->
    	<dependency>
    		<groupId>org.springframework</groupId>
    		<artifactId>spring-core</artifactId>
    		<version>${spring.version}</version>
    		<exclusions>
    		  <exclusion>
    			<groupId>commons-logging</groupId>
    			<artifactId>commons-logging</artifactId>
    		  </exclusion>
    		</exclusions>
    	</dependency>

    	<!-- Spring MVC -->
    	<dependency>
    		<groupId>org.springframework</groupId>
    		<artifactId>spring-webmvc</artifactId>
    		<version>${spring.version}</version>
    	</dependency>

    	<!-- Spring + aspects -->
    	<dependency>
    		<groupId>org.springframework</groupId>
    		<artifactId>spring-aspects</artifactId>
    		<version>${spring.version}</version>
    	</dependency>

    	<!-- ORM integration, e.g Hibernate -->
    	<dependency>
    		<groupId>org.springframework</groupId>
    		<artifactId>spring-orm</artifactId>
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

    	<!-- Spring Security JSP Taglib -->
    	<dependency>
    		<groupId>org.springframework.security</groupId>
    		<artifactId>spring-security-taglibs</artifactId>
    		<version>${spring.security.version}</version>
    	</dependency>

    	<!-- jstl for jsp page -->
    	<dependency>
    		<groupId>jstl</groupId>
    		<artifactId>jstl</artifactId>
    		<version>${jstl.version}</version>
    	</dependency>

    	<!-- MySql Driver -->
    	<dependency>
    		<groupId>mysql</groupId>
    		<artifactId>mysql-connector-java</artifactId>
    		<version>${mysql.connector.version}</version>
    	</dependency>

    	<!-- logging, slf4j -->
    	<dependency>
    		<groupId>org.slf4j</groupId>
    		<artifactId>slf4j-api</artifactId>
    		<version>${slf4j.version}</version>
    	</dependency>

    	<dependency>
    		<groupId>org.slf4j</groupId>
    		<artifactId>jcl-over-slf4j</artifactId>
    		<version>${slf4j.version}</version>
    	</dependency>

    	<dependency>
    		<groupId>ch.qos.logback</groupId>
    		<artifactId>logback-classic</artifactId>
    		<version>${logback.version}</version>
    	</dependency>

    	<dependency>
    		<groupId>javax.servlet</groupId>
    		<artifactId>servlet-api</artifactId>
    		<version>${servletapi.version}</version>
    		<scope>provided</scope>
    	</dependency>

    </dependencies>

## 3\. User Tables

SQL script to create tables to store user and user’s role.

mysql.sql

    CREATE  TABLE users (
      username VARCHAR(45) NOT NULL ,
      password VARCHAR(60) NOT NULL ,
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
    VALUES ('mkyong','$2a$10$04TVADrR6/SPLBjsK0N30.Jf5fNjBugSACeGv1S69dZALR7lSov0y', true);
    INSERT INTO users(username,password,enabled)
    VALUES ('alex','$2a$10$04TVADrR6/SPLBjsK0N30.Jf5fNjBugSACeGv1S69dZALR7lSov0y', true);

    INSERT INTO user_roles (username, role)
    VALUES ('mkyong', 'ROLE_USER');
    INSERT INTO user_roles (username, role)
    VALUES ('mkyong', 'ROLE_ADMIN');
    INSERT INTO user_roles (username, role)
    VALUES ('alex', 'ROLE_USER');

## 4\. User Model + Hibernate XML Mapping

Model classes and its’ XML mapping file.

User.java

    package com.mkyong.users.model;

    import java.util.HashSet;
    import java.util.Set;

    public class User {

    	private String username;
    	private String password;
    	private boolean enabled;
    	private Set<UserRole> userRole = new HashSet<UserRole>(0);

    	//getter and setter methods
    }

UserRole.java

    package com.mkyong.users.model;

    public class UserRole{

    	private Integer userRoleId;
    	private User user;
    	private String role;

    	//getter and setter methods
    }

Users.hbm.xml

    <?xml version="1.0"?>
    <!DOCTYPE hibernate-mapping PUBLIC "-//Hibernate/Hibernate Mapping DTD 3.0//EN"
    "http://www.hibernate.org/dtd/hibernate-mapping-3.0.dtd">
    <hibernate-mapping>
        <class name="com.mkyong.users.model.User" table="users" catalog="test">
            <id name="username" type="string">
                <column name="username" length="45" />
                <generator class="assigned" />
            </id>
            <property name="password" type="string">
                <column name="password" length="60" not-null="true" />
            </property>
            <property name="enabled" type="boolean">
                <column name="enabled" not-null="true" />
            </property>
            <set name="userRole" table="user_roles" inverse="true" lazy="true" fetch="select">
                <key>
                    <column name="username" length="45" not-null="true" />
                </key>
                <one-to-many class="com.mkyong.users.model.UserRole" />
            </set>
        </class>
    </hibernate-mapping>

UserRoles.hbm.xml

    <?xml version="1.0"?>
    <!DOCTYPE hibernate-mapping PUBLIC "-//Hibernate/Hibernate Mapping DTD 3.0//EN"
    "http://www.hibernate.org/dtd/hibernate-mapping-3.0.dtd">
    <hibernate-mapping>
        <class name="com.mkyong.users.model.UserRole" table="user_roles" catalog="test">
            <id name="userRoleId" type="java.lang.Integer">
                <column name="user_role_id" />
                <generator class="identity" />
            </id>
            <many-to-one name="user" class="com.mkyong.users.model.User" fetch="select">
                <column name="username" length="45" not-null="true" />
            </many-to-one>
            <property name="role" type="string">
                <column name="role" length="45" not-null="true" />
            </property>
        </class>
    </hibernate-mapping>

## 5\. DAO Class

Create a `UserDao` class to load users from the database, via Hibernate.

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

    import com.mkyong.users.model.User;

    public class UserDaoImpl implements UserDao {

    	private SessionFactory sessionFactory;

    	@SuppressWarnings("unchecked")
    	public User findByUserName(String username) {

    		List<User> users = new ArrayList<User>();

    		users = getSessionFactory().getCurrentSession()
    			.createQuery("from User where username=?")
    			.setParameter(0, username).list();

    		if (users.size() > 0) {
    			return users.get(0);
    		} else {
    			return null;
    		}

    	}

    	public SessionFactory getSessionFactory() {
    		return sessionFactory;
    	}

    	public void setSessionFactory(SessionFactory sessionFactory) {
    		this.sessionFactory = sessionFactory;
    	}

    }

## 6\. UserDetailsService

Create a custom `UserDetailsService`, load user from `UserDao`, then build the user’s authorities.

**Note**  
Example is reference from this Spring’s [JdbcDaoImpl](http://www.mkyong.com/spring-security/spring-security-hibernate-xml-example/org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl)

MyUserDetailsService.java

    package com.mkyong.users.service;

    import java.util.ArrayList;
    import java.util.HashSet;
    import java.util.List;
    import java.util.Set;

    import org.springframework.security.core.GrantedAuthority;
    import org.springframework.security.core.authority.SimpleGrantedAuthority;
    import org.springframework.security.core.userdetails.User;
    import org.springframework.security.core.userdetails.UserDetails;
    import org.springframework.security.core.userdetails.UserDetailsService;
    import org.springframework.security.core.userdetails.UsernameNotFoundException;

    import com.mkyong.users.dao.UserDao;
    import com.mkyong.users.model.UserRole;

    public class MyUserDetailsService implements UserDetailsService {

    	private UserDao userDao;

    	@Override
    	public UserDetails loadUserByUsername(final String username)
                   throws UsernameNotFoundException {

    		com.mkyong.users.model.User user = userDao.findByUserName(username);
    		List<GrantedAuthority> authorities = buildUserAuthority(user.getUserRole());

    		return buildUserForAuthentication(user, authorities);

    	}

    	// Converts com.mkyong.users.model.User user to
    	// org.springframework.security.core.userdetails.User
    	private User buildUserForAuthentication(com.mkyong.users.model.User user,
    		List<GrantedAuthority> authorities) {
    		return new User(user.getUsername(),
    			user.getPassword(), user.isEnabled(),
                            true, true, true, authorities);
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

    	public UserDao getUserDao() {
    		return userDao;
    	}

    	public void setUserDao(UserDao userDao) {
    		this.userDao = userDao;
    	}

    }

## 7\. Spring Security XML

Read the comments, it should be self-explanatory.

spring-security.xml

    <beans:beans xmlns="http://www.springframework.org/schema/security"
    	xmlns:beans="http://www.springframework.org/schema/beans"
    	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    	xsi:schemaLocation="http://www.springframework.org/schema/beans
    	http://www.springframework.org/schema/beans/spring-beans-3.0.xsd
    	http://www.springframework.org/schema/security
    	http://www.springframework.org/schema/security/spring-security-3.2.xsd">

    	<!-- enable use-expressions -->
    	<http auto-config="true" use-expressions="true">
    		<intercept-url pattern="/admin**" access="hasRole('ROLE_ADMIN')" />

    		<!-- access denied page -->
    		<access-denied-handler error-page="/403" />
    		<form-login
    		    login-page="/login"
    		    default-target-url="/welcome"
    			authentication-failure-url="/login?error"
    			username-parameter="username"
    			password-parameter="password" />
    		<logout logout-success-url="/login?logout" />
    		<!-- enable csrf protection -->
    		<csrf />
    	</http>

    	<authentication-manager>
    		<authentication-provider user-service-ref="myUserDetailsService" >
    			<password-encoder hash="bcrypt" />
    		</authentication-provider>
    	</authentication-manager>

    </beans:beans>

spring-database.xml

    <beans xmlns="http://www.springframework.org/schema/beans"
    	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    	xmlns:aop="http://www.springframework.org/schema/aop"
    	xmlns:tx="http://www.springframework.org/schema/tx"
    	xsi:schemaLocation="http://www.springframework.org/schema/beans
    	http://www.springframework.org/schema/beans/spring-beans-3.0.xsd
    	http://www.springframework.org/schema/aop
    	http://www.springframework.org/schema/aop/spring-aop-3.0.xsd
    	http://www.springframework.org/schema/tx
        http://www.springframework.org/schema/tx/spring-tx-3.0.xsd">

    	<!-- MySQL data source -->
    	<bean id="dataSource" class="org.apache.commons.dbcp.BasicDataSource"
    		destroy-method="close">

    		<property name="driverClassName" value="com.mysql.jdbc.Driver" />
    		<property name="url" value="jdbc:mysql://localhost:3306/test" />
    		<property name="username" value="root" />
    		<property name="password" value="password" />
    	</bean>

    	<!-- Hibernate session factory -->
    	<bean id="sessionFactory"
    		class="org.springframework.orm.hibernate4.LocalSessionFactoryBean">
    		<property name="dataSource" ref="dataSource" />
    		<property name="mappingResources">
    			<list>
    				<value>orm/Users.hbm.xml</value>
    				<value>orm/UserRoles.hbm.xml</value>
    			</list>
    		</property>
    		<property name="hibernateProperties">
    		    <props>
    			<prop key="hibernate.dialect">
                               org.hibernate.dialect.MySQL5Dialect
                            </prop>
    			<prop key="hibernate.format_sql">true</prop>
    			<prop key="hibernate.show_sql">true</prop>
    		    </props>
    		</property>
    	</bean>

    	<bean id="userDao" class="com.mkyong.users.dao.UserDaoImpl">
    		<property name="sessionFactory" ref="sessionFactory" />
    	</bean>

    	<bean id="myUserDetailsService"
                    class="com.mkyong.users.service.MyUserDetailsService">
    		<property name="userDao" ref="userDao" />
    	</bean>

    	<!-- MUST have transaction manager, using aop and aspects  -->
    	<bean id="transactionManager"
    		class="org.springframework.orm.hibernate4.HibernateTransactionManager">
    		<property name="sessionFactory" ref="sessionFactory"></property>
    	</bean>

    	<tx:advice id="txAdvice" transaction-manager="transactionManager">
    	    <tx:attributes>
    		<tx:method name="get*" read-only="true" />
    		<tx:method name="find*" read-only="true" />
    		<tx:method name="*" />
    	    </tx:attributes>
    	</tx:advice>

    	<aop:config>
    	    <aop:pointcut id="userServicePointCut"
    		expression="execution(* com.mkyong.users.service.*Service.*(..))" />
    	    <aop:advisor advice-ref="txAdvice" pointcut-ref="userServicePointCut" />
    	</aop:config>

    </beans>

Done.

**Note**  
Those JSP, web.xml and controller files are omitted, it’s quite a standard code, if you are interested on it, please download the full source code at the end of the article.

## 8\. Project Demo

The following video demo is for the [Spring Security database login with JDBC](http://www.mkyong.com/spring-security/spring-security-form-login-using-database/) tutorial. Since this tutorial is generating the same output, but using the Hibernate to load Users, so the video demo is reused.

8.1 Access a password protected page : http://localhost:8080/spring-security-hibernate/admin , a login page is displayed.

![spring-security-hibernate1](http://www.mkyong.com/wp-content/uploads/2014/05/spring-security-hibernate1.png)

8.2 Enter user “mkyong” and password “123456”.

![spring-security-hibernate2](http://www.mkyong.com/wp-content/uploads/2014/05/spring-security-hibernate2.png)

8.3 Try access `/admin` page with user “alex” and password “123456”, a 403 page will be displayed.

![spring-security-hibernate3](http://www.mkyong.com/wp-content/uploads/2014/05/spring-security-hibernate3.png)

[http://www.mkyong.com/spring-security/spring-security-hibernate-xml-example/](http://www.mkyong.com/spring-security/spring-security-hibernate-xml-example/)
