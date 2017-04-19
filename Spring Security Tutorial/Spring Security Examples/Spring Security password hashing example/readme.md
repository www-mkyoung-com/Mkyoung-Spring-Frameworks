In this tutorial, we will show you how to use `BCryptPasswordEncoder` to hash a password and perform a login authentication in Spring Security.

In the old days, normally, we used MD5 `Md5PasswordEncoder` or SHA `ShaPasswordEncoder` hashing algorithm to encode a password… you are still allowed to use whatever encoder you like, but Spring recommends to use [BCrypt](http://en.wikipedia.org/wiki/Bcrypt)`BCryptPasswordEncoder`, a stronger hashing algorithm with randomly generated salt.

Technologies used :

1.  Spring 3.2.8.RELEASE
2.  Spring Security 3.2.3.RELEASE
3.  Spring JDBC 3.2.3.RELEASE
4.  MySQL Server 5.6

## 1\. Review PasswordEncoder

The familiar old authentication `PasswordEncoder` interface is deprecated…

    package org.springframework.security.authentication.encoding;

    //Implementation : Md5PasswordEncoder and ShaPasswordEncoder
    @Deprecated
    public interface PasswordEncoder {

Instead, you should use this new crypto `PasswordEncoder` interface.

    package org.springframework.security.crypto.password;

    //Implementation : BCryptPasswordEncoder
    public interface PasswordEncoder {

## 2\. Generate a BCrypt Password

First, hash a password and put it into a database, for login authentication later. This example uses `BCryptPasswordEncoder`to hash a password “123456”.

PasswordEncoderGenerator.java

    package com.mkyong.web.controller;

    import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

    public class PasswordEncoderGenerator {

      public static void main(String[] args) {

    	int i = 0;
    	while (i < 10) {
    		String password = "123456";
    		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    		String hashedPassword = passwordEncoder.encode(password);

    		System.out.println(hashedPassword);
    		i++;
    	}

      }
    }

In BCrypt hashing algorithm, each time, a different hash value of length 60 is generated.

    $2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8.
    $2a$10$trT3.R/Nfey62eczbKEnueTcIbJXW.u1ffAo/XfyLpofwNDbEB86O
    $2a$10$teJrCEnsxNT49ZpXU7n22O27aCGbVYYe/RG6/XxdWPJbOLZubLIi2
    $2a$10$BHG59UT6p7bgT6U2fQ/9wOyTIdejh4Rk1vWilvl4b6ysNPdhnViUS
    $2a$10$W9oRWeFmOT0bByL5fmAceucetmEYFg2yzq3e50mcu.CO7rUDb/poG
    $2a$10$HApapHvDStTEwjjneMCvxuqUKVyycXZRfXMwjU0rRmaWMsjWQp/Zu
    $2a$10$GYCkBzp2NlpGS/qjp5f6NOWHeF56ENAlHNuSssSJpE1MMYJevHBWO
    $2a$10$gwbTCaIR/qE1uYhvEY6GG.bNDQcZuYQX9tkVwaK/aD7ZLPptC.7QC
    $2a$10$5uKS72xK2ArGDgb2CwjYnOzQcOmB7CPxK6fz2MGcDBM9vJ4rUql36
    $2a$10$6TajU85/gVrGUm5fv5Z8beVF37rlENohyLk3BEpZJFi6Av9JNkw9O

It's normal to get a different value each time you hash a value with BCrypt, because salt is generated randomly. In this tutorial, we get the first output and inserts it into the database.

## 3\. Database

Create tables and insert a user "mkyong" for testing.

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
    VALUES ('mkyong','$2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8.', true);

    INSERT INTO user_roles (username, role)
    VALUES ('mkyong', 'ROLE_USER');
    INSERT INTO user_roles (username, role)
    VALUES ('mkyong', 'ROLE_ADMIN');

## 4\. Enable Password Encoder

A few ways to enable the password encoder in XML configuration.

4.1 Using the default _BCryptPasswordEncoder_.

spring-security.xml

    <authentication-manager>
    <authentication-provider>
        <password-encoder hash="bcrypt" />
    </authentication-provider>
      </authentication-manager>

4.2 Pass a "strength" parameter to the _BCryptPasswordEncoder_.

spring-security.xml

    <authentication-manager>
    <authentication-provider>
        <password-encoder ref="encoder" />
    </authentication-provider>
      </authentication-manager>

      <beans:bean id="encoder"
    class="org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder">
    <beans:constructor-arg name="strength" value="11" />
      </beans:bean>

4.3 Pass an encoder to _DaoAuthenticationProvider_.

spring-security.xml

    <bean id="authProvider"
    class="org.springframework.security.authentication.dao.DaoAuthenticationProvider">
    <property name="userDetailsService" ref="customUserService" />
    <property name="passwordEncoder" ref="encoder" />
      </bean>

      <bean id="encoder"
    class="org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder"/>

4.4 Annotation example.

    @Configuration
    @EnableWebSecurity
    public class SecurityConfig extends WebSecurityConfigurerAdapter {

    	@Autowired
    	DataSource dataSource;

    	@Autowired
    	public void configAuthentication(AuthenticationManagerBuilder auth)
    		throws Exception {

    		auth.jdbcAuthentication().dataSource(dataSource)
    			.passwordEncoder(passwordEncoder())
    			.usersByUsernameQuery("sql...")
    			.authoritiesByUsernameQuery("sql...");
    	}

    	@Bean
    	public PasswordEncoder passwordEncoder(){
    		PasswordEncoder encoder = new BCryptPasswordEncoder();
    		return encoder;
    	}

## 5\. Project Demo

Access a password protected page : _localhost:8080/spring-security-password-hashing/admin_, a login page is displayed. Enter a password "123456", Spring Security will hash the password and compare it with the hashed password from database.

![spring-security-password-encoder](http://www.mkyong.com/wp-content/uploads/2011/08/spring-security-password-encoder.png)

User and password in Database.

![spring-security-password-encoder-database](http://www.mkyong.com/wp-content/uploads/2011/08/spring-security-password-encoder-database.png)

[http://www.mkyong.com/spring-security/spring-security-password-hashing-example/](http://www.mkyong.com/spring-security/spring-security-password-hashing-example/)
