Spring’s [dependency checking](http://www.mkyong.com/spring/spring-properties-dependency-checking/) in bean configuration file is used to make sure all properties of a certain types (primitive, collection or object) have been set. In most scenarios, you just need to make sure a particular property has been set, but not all properties..

For this case, you need **@Required** annotation, see following example :

## @Required example

A Customer object, apply @Required in setPerson() method to make sure the person property has been set.

    package com.mkyong.common;

    import org.springframework.beans.factory.annotation.Required;

    public class Customer
    {
    	private Person person;
    	private int type;
    	private String action;

    	public Person getPerson() {
    		return person;
    	}
    	@Required
    	public void setPerson(Person person) {
    		this.person = person;
    	}
    }

Simply apply the @Required annotation will not enforce the property checking, you also need to register an **RequiredAnnotationBeanPostProcessor** to aware of the @Required annotation in bean configuration file.

The RequiredAnnotationBeanPostProcessor can be enabled in two ways.

1\. Include <context:annotation-config />

Add Spring context and <context:annotation-config /> in bean configuration file.

    <beans
    	...
    	xmlns:context="http://www.springframework.org/schema/context"
    	...
    	http://www.springframework.org/schema/context
    	http://www.springframework.org/schema/context/spring-context-2.5.xsd">
    	...
    	<context:annotation-config />
    	...
    </beans>

Full example,

    <beans xmlns="http://www.springframework.org/schema/beans"
    	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    	xmlns:context="http://www.springframework.org/schema/context"
    	xsi:schemaLocation="http://www.springframework.org/schema/beans
    	http://www.springframework.org/schema/beans/spring-beans-2.5.xsd
    	http://www.springframework.org/schema/context
    	http://www.springframework.org/schema/context/spring-context-2.5.xsd">

    	<context:annotation-config />

    	<bean id="CustomerBean" class="com.mkyong.common.Customer">
    		<property name="action" value="buy" />
    		<property name="type" value="1" />
    	</bean>

    	<bean id="PersonBean" class="com.mkyong.common.Person">
    		<property name="name" value="mkyong" />
    		<property name="address" value="address ABC" />
    		<property name="age" value="29" />
    	</bean>

    </beans>

2\. Include RequiredAnnotationBeanPostProcessor

Include ‘RequiredAnnotationBeanPostProcessor’ directly in bean configuration file.

    <beans xmlns="http://www.springframework.org/schema/beans"
    	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    	xsi:schemaLocation="http://www.springframework.org/schema/beans
    	http://www.springframework.org/schema/beans/spring-beans-2.5.xsd">

    <bean
    class="org.springframework.beans.factory.annotation.RequiredAnnotationBeanPostProcessor"/>

    	<bean id="CustomerBean" class="com.mkyong.common.Customer">
    		<property name="action" value="buy" />
    		<property name="type" value="1" />
    	</bean>

    	<bean id="PersonBean" class="com.mkyong.common.Person">
    		<property name="name" value="mkyong" />
    		<property name="address" value="address ABC" />
    		<property name="age" value="29" />
    	</bean>

    </beans>

If you run it , the following error message will be throw, because person property is unset.

    org.springframework.beans.factory.BeanInitializationException:
    	Property 'person' is required for bean 'CustomerBean'

## Conclusion

Try @Required annotation, it is more flexible than dependency checking in XML file, because it can apply to a particular property only.

[http://www.mkyong.com/spring/spring-dependency-checking-with-required-annotation/](http://www.mkyong.com/spring/spring-dependency-checking-with-required-annotation/)
