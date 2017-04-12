In Spring,you can use dependency checking feature to make sure the required properties have been set or injected.

## Dependency checking modes

4 dependency checking modes are supported:

*   none – No dependency checking.
*   simple – If any properties of primitive type (int, long,double…) and collection types (map, list..) have not been set, UnsatisfiedDependencyException will be thrown.
*   objects – If any properties of object type have not been set, UnsatisfiedDependencyException will be thrown.
*   all – If any properties of any type have not been set, an UnsatisfiedDependencyException  
    will be thrown.

_P.S The default mode is none_

## Example

A Customer and Person object for the demonstration.

    package com.mkyong.common;

    public class Customer
    {
    	private Person person;
    	private int type;
    	private String action;

    	//getter and setter methods
    }

    package com.mkyong.common;

    public class Person
    {
    	private String name;
    	private String address;
    	private int age;

    	//getter and setter methods
    }

## 1\. none dependency checking

Spring bean configuration file with ‘none’ dependency checking mode.

    <beans xmlns="http://www.springframework.org/schema/beans"
    	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    	xsi:schemaLocation="http://www.springframework.org/schema/beans
    	http://www.springframework.org/schema/beans/spring-beans-2.5.xsd">

    	<bean id="CustomerBean" class="com.mkyong.common.Customer" >
    		<property name="action" value="buy" />
    	</bean>

    	<bean id="PersonBean" class="com.mkyong.common.Person">
    		<property name="name" value="mkyong" />
    		<property name="address" value="address ABC" />
    		<property name="age" value="29" />
    	</bean>

    </beans>

If you did not explicitly define the dependency checking mode, it’s default to ‘none’. No dependency checking will perform.

## 2\. simple dependency checking

Spring bean configuration file with ‘simple’ dependency checking mode.

    <beans xmlns="http://www.springframework.org/schema/beans"
    	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    	xsi:schemaLocation="http://www.springframework.org/schema/beans
    	http://www.springframework.org/schema/beans/spring-beans-2.5.xsd">

    	<bean id="CustomerBean" class="com.mkyong.common.Customer"
             dependency-check="simple">

    		<property name="person" ref="PersonBean" />
    		<property name="action" value="buy" />
    	</bean>

    	<bean id="PersonBean" class="com.mkyong.common.Person">
    		<property name="name" value="mkyong" />
    		<property name="address" value="address ABC" />
    		<property name="age" value="29" />
    	</bean>

    </beans>

The ‘type’ property (primitive type or collection types) have not been set, an **UnsatisfiedDependencyException** will throw.

    org.springframework.beans.factory.UnsatisfiedDependencyException:
    Error creating bean with name 'CustomerBean'
    defined in class path resource [config/Spring-Customer.xml]:
    Unsatisfied dependency expressed through bean property 'type':
    Set this property value or disable dependency checking for this bean.

## 3\. objects dependency checking

Spring bean configuration file with ‘objects’ dependency checking mode.

    <beans xmlns="http://www.springframework.org/schema/beans"
    	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    	xsi:schemaLocation="http://www.springframework.org/schema/beans
    	http://www.springframework.org/schema/beans/spring-beans-2.5.xsd">

    	<bean id="CustomerBean" class="com.mkyong.common.Customer"
             dependency-check="objects">

    		<property name="action" value="buy" />
    		<property name="type" value="1" />
    	</bean>

    	<bean id="PersonBean" class="com.mkyong.common.Person">
    		<property name="name" value="mkyong" />
    		<property name="address" value="address ABC" />
    		<property name="age" value="29" />
    	</bean>

    </beans>

The ‘person’ property (objects type) have not been set, an **UnsatisfiedDependencyException** will throw.

    org.springframework.beans.factory.UnsatisfiedDependencyException:
    Error creating bean with name 'CustomerBean'
    defined in class path resource [config/Spring-Customer.xml]:
    Unsatisfied dependency expressed through bean property 'person':
    Set this property value or disable dependency checking for this bean.

## 4\. all dependency checking

Spring bean configuration file with ‘all’ dependency checking mode.

    <beans xmlns="http://www.springframework.org/schema/beans"
    	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    	xsi:schemaLocation="http://www.springframework.org/schema/beans
    	http://www.springframework.org/schema/beans/spring-beans-2.5.xsd">

    	<bean id="CustomerBean" class="com.mkyong.common.Customer"
             dependency-check="all">

    		<property name="action" value="buy" />
    	</bean>

    	<bean id="PersonBean" class="com.mkyong.common.Person">
    		<property name="name" value="mkyong" />
    		<property name="address" value="address ABC" />
    		<property name="age" value="29" />
    	</bean>

    </beans>

The combination of ‘simple’ and ‘objects’ mode, if any properties of any type (primitive, collection and object) have not been set, an **UnsatisfiedDependencyException** will be thrown.

## Global default dependency checking

Explicitly define the dependency checking mode for every beans is tedious and error prone, you can set a default-dependency-check attribute in the <beans> root element to force the entire beans declared within <beans> root element to apply this rule. However, this root default mode will be overridden by a bean’s own mode if specified.

    <beans xmlns="http://www.springframework.org/schema/beans"
    	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    	xsi:schemaLocation="http://www.springframework.org/schema/beans
    	http://www.springframework.org/schema/beans/spring-beans-2.5.xsd"
    	default-dependency-check="all">

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

All beans declared in this configuration file are default to ‘all’ dependency checking mode.

[http://www.mkyong.com/spring/spring-properties-dependency-checking/](http://www.mkyong.com/spring/spring-properties-dependency-checking/)
