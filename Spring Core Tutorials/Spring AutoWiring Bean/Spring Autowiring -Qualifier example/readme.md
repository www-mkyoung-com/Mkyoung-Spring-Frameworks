In Spring, @Qualifier means, which bean is qualify to autowired on a field. See following scenario :

## Autowiring Example

See below example, it will autowired a “person” bean into customer’s person property.

    package com.mkyong.common;

    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.beans.factory.annotation.Qualifier;

    public class Customer {

    	@Autowired
    	private Person person;
    	//...
    }

But, two similar beans “`com.mkyong.common.Person`” are declared in bean configuration file. Will Spring know which person bean should autowired?

    <beans xmlns="http://www.springframework.org/schema/beans"
    	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    	xsi:schemaLocation="http://www.springframework.org/schema/beans
    	http://www.springframework.org/schema/beans/spring-beans-2.5.xsd">

    <bean
    class ="org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor"/>

    	<bean id="customer" class="com.mkyong.common.Customer" />

    	<bean id="personA" class="com.mkyong.common.Person" >
    		<property name="name" value="mkyongA" />
    	</bean>

    	<bean id="personB" class="com.mkyong.common.Person" >
    		<property name="name" value="mkyongB" />
    	</bean>

    </beans>

When you run above example, it hits below exception :

    Caused by: org.springframework.beans.factory.NoSuchBeanDefinitionException:
    	No unique bean of type [com.mkyong.common.Person] is defined:
    		expected single matching bean but found 2: [personA, personB]

## @Qualifier Example

To fix above problem, you need **@Quanlifier** to tell Spring about which bean should autowired.

    package com.mkyong.common;

    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.beans.factory.annotation.Qualifier;

    public class Customer {

    	@Autowired
    	@Qualifier("personA")
    	private Person person;
    	//...
    }

In this case, bean “personA” is autowired.

    Customer [person=Person [name=mkyongA]]

[http://www.mkyong.com/spring/spring-autowiring-qualifier-example/](http://www.mkyong.com/spring/spring-autowiring-qualifier-example/)
