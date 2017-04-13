Spring EL supports regular expression using a simple keyword “**matches**“, which is really awesome! For examples,

    @Value("#{'100' matches '\\d+' }")
    private boolean isDigit;

It test whether ‘**100**‘ is a valid digit via regular expression ‘**\\d+**‘.

## Spring EL in Annotation

See following Spring EL regular expression examples, some mixed with ternary operator, which makes Spring EL pretty flexible and powerful.

Below example should be self-explanatory.

    package com.mkyong.core;

    import org.springframework.beans.factory.annotation.Value;
    import org.springframework.stereotype.Component;

    @Component("customerBean")
    public class Customer {

    	// email regular expression
    	String emailRegEx = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)" +
    			"*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    	// if this is a digit?
    	@Value("#{'100' matches '\\d+' }")
    	private boolean validDigit;

    	// if this is a digit + ternary operator
    	@Value("#{ ('100' matches '\\d+') == true ? " +
    			"'yes this is digit' : 'No this is not a digit'  }")
    	private String msg;

    	// if this emailBean.emailAddress contains a valid email address?
    	@Value("#{emailBean.emailAddress matches customerBean.emailRegEx}")
    	private boolean validEmail;

    	//getter and setter methods, and constructor
    }

    package com.mkyong.core;

    import org.springframework.beans.factory.annotation.Value;
    import org.springframework.stereotype.Component;

    @Component("emailBean")
    public class Email {

    	@Value("nospam@abc.com")
    	String emailAddress;

    	//...
    }

_Output_

    Customer [isDigit=true, msg=yes this is digit, isValidEmail=true]

## Spring EL in XML

See equivalent version in bean definition XML file.

    <beans xmlns="http://www.springframework.org/schema/beans"
    	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    	xsi:schemaLocation="http://www.springframework.org/schema/beans
    	http://www.springframework.org/schema/beans/spring-beans-3.0.xsd">

    	<bean id="customerBean" class="com.mkyong.core.Customer">
    	  <property name="validDigit" value="#{'100' matches '\d+' }" />
    	  <property name="msg"
    		value="#{ ('100' matches '\d+') == true ? 'yes this is digit' : 'No this is not a digit'  }" />
    	  <property name="validEmail"
    		value="#{emailBean.emailAddress matches '^[_A-Za-z0-9-]+(\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\.[A-Za-z0-9]+)*(\.[A-Za-z]{2,})$' }" />
    	</bean>

    	<bean id="emailBean" class="com.mkyong.core.Email">
    	  <property name="emailAddress" value="nospam@abc.com" />
    	</bean>

    </beans>

[http://www.mkyong.com/spring3/spring-el-regular-expression-example/](http://www.mkyong.com/spring3/spring-el-regular-expression-example/)
