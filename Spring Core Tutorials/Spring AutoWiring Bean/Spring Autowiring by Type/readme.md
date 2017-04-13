In Spring, “**Autowiring by Type**” means, if data type of a bean is compatible with the data type of other bean property, auto wire it.

For example, a “person” bean exposes a property with data type of “ability” class, Spring will find the bean with same data type of class “ability” and wire it automatically. And if no matching found, just do nothing.

You can enable this feature via `autowire="byType"` like below :

    <!-- person has a property type of class "ability" -->
    <bean id="person" class="com.mkyong.common.Person" autowire="byType" />

    <bean id="invisible" class="com.mkyong.common.Ability" >
    	<property name="skill" value="Invisible" />
    </bean>

See a full example of Spring auto wiring by type.

## 1\. Beans

Two beans, person and ability.

    package com.mkyong.common;

    public class Person
    {
    	private Ability ability;
    	//...
    }

    package com.mkyong.common;

    public class Ability
    {
    	private String skill;
    	//...
    }

## 2\. Spring Wiring

Normally, you wire the bean explicitly :

    <bean id="person" class="com.mkyong.common.Person">
    	<property name="ability" ref="invisible" />
    </bean>

    <bean id="invisible" class="com.mkyong.common.Ability" >
    	<property name="skill" value="Invisible" />
    </bean>

_Output_

    Person [ability=Ability [skill=Invisible]]

With **autowire by type enabled**, you can leave the ability property unset. Spring will find the same data type and wire it automatcailly.

    <bean id="person" class="com.mkyong.common.Person" autowire="byType" />

    <bean id="invisible" class="com.mkyong.common.Ability" >
    	<property name="skill" value="Invisible" />
    </bean>

_Output_

    Person [ability=Ability [skill=Invisible]]

Wait, what if you have two beans with same data type of class “ability”?

    <bean id="person" class="com.mkyong.common.Person" autowire="byType" />

    <bean id="steal" class="com.mkyong.common.Ability" >
    	<property name="skill" value="Steal" />
    </bean>

    <bean id="invisible" class="com.mkyong.common.Ability" >
    	<property name="skill" value="Invisible" />
    </bean>

_Output_

    Exception in thread "main" org.springframework.beans.factory.UnsatisfiedDependencyException:
    ...
    No unique bean of type [com.mkyong.common.Ability] is defined:
    expected single matching bean but found 2: [steal, invisible]; nested exception is
    org.springframework.beans.factory.NoSuchBeanDefinitionException:
    No unique bean of type [com.mkyong.common.Ability] is defined:
    expected single matching bean but found 2: [steal, invisible]

In this case, you will hits the `UnsatisfiedDependencyException` error message.

[http://www.mkyong.com/spring/spring-autowiring-by-type/](http://www.mkyong.com/spring/spring-autowiring-by-type/)
