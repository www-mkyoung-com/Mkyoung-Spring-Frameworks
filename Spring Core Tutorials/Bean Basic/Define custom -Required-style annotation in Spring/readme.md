The [@Required annotation](http://www.mkyong.com/spring/spring-dependency-checking-with-required-annotation/) is used to make sure a particular property has been set. If you are migrate your existing project to Spring framework or have your own @Required-style annotation for whatever reasons, Spring is allow you to define your custom @Required-style annotation, which is equivalent to @Required annotation.

In this example, you will create a custom `@Required-style` annotation named **@Mandatory**, which is equivalent to `@Required` annotation.

## 1\. Create the @Mandatory interface

    package com.mkyong.common;

    import java.lang.annotation.ElementType;
    import java.lang.annotation.Retention;
    import java.lang.annotation.RetentionPolicy;
    import java.lang.annotation.Target;

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface Mandatory {
    }

## 2\. Apply it to a property

    package com.mkyong.common;

    public class Customer
    {
    	private Person person;
    	private int type;
    	private String action;

    	@Mandatory
    	public void setPerson(Person person) {
    		this.person = person;
    	}
    	//getter and setter methods
    }

## 3\. Register it

Include your new **@Mandatory** annotation in ‘RequiredAnnotationBeanPostProcessor’ class.

    <beans xmlns="http://www.springframework.org/schema/beans"
    	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    	xsi:schemaLocation="http://www.springframework.org/schema/beans
    	http://www.springframework.org/schema/beans/spring-beans-2.5.xsd">

    <bean
    class="org.springframework.beans.factory.annotation.RequiredAnnotationBeanPostProcessor">
    	<property name="requiredAnnotationType" value="com.mkyong.common.Mandatory"/>
    </bean>

    	<bean id="CustomerBean" class="com.mkyong.common.Customer">
    		<property name="action" value="buy" />
    		<property name="type" value="1" />
    	</bean>

    </beans>

## 4\. Done

Done, you just created a new custom @Required-style annotation named **@Mandatory**, which is equivalent to @Required annotation.
