In Spring framework, you can wire beans automatically with auto-wiring feature. To enable it, just define the “**autowire**” attribute in <bean>.

    <bean id="customer" class="com.mkyong.common.Customer" autowire="byName" />

In Spring, 5 Auto-wiring modes are supported.

*   no – Default, no auto wiring, set it manually via “ref” attribute
*   byName – Auto wiring by property name. If the name of a bean is same as the name of other bean property, auto wire it.
*   byType – Auto wiring by property data type. If data type of a bean is compatible with the data type of other bean property, auto wire it.
*   constructor – byType mode in constructor argument.
*   autodetect – If a default constructor is found, use “autowired by constructor”; Otherwise, use “autowire by type”.

## Examples

A Customer and Person object for auto wiring demonstration.

    package com.mkyong.common;

    public class Customer
    {
    	private Person person;

    	public Customer(Person person) {
    		this.person = person;
    	}

    	public void setPerson(Person person) {
    		this.person = person;
    	}
    	//...
    }

    package com.mkyong.common;

    public class Person
    {
    	//...
    }

## 1\. Auto-Wiring ‘no’

This is the default mode, you need to wire your bean via ‘ref’ attribute.

    <bean id="customer" class="com.mkyong.common.Customer">
                      <property name="person" ref="person" />
    </bean>

    <bean id="person" class="com.mkyong.common.Person" />

## 2\. Auto-Wiring ‘byName’

Auto-wire a bean by property name. In this case, since the name of “person” bean is same with the name of the “customer” bean’s property (“person”), so, Spring will auto wired it via setter method – “`setPerson(Person person)`“.

    <bean id="customer" class="com.mkyong.common.Customer" autowire="byName" />

    <bean id="person" class="com.mkyong.common.Person" />

See full example – [Spring Autowiring by Name](http://www.mkyong.com/spring/spring-autowiring-by-name/).

## 3\. Auto-Wiring ‘byType’

Auto-wire a bean by property data type. In this case, since the data type of “person” bean is same as the data type of the “customer” bean’s property (Person object), so, Spring will auto wired it via setter method – “`setPerson(Person person)`“.

    <bean id="customer" class="com.mkyong.common.Customer" autowire="byType" />

    <bean id="person" class="com.mkyong.common.Person" />

See full example – [Spring Autowiring by Type](http://www.mkyong.com/spring/spring-autowiring-by-type/).

## 4\. Auto-Wiring ‘constructor’

Auto-wire a bean by property data type in constructor argument. In this case, since the data type of “person” bean is same as the constructor argument data type in “customer” bean’s property (Person object), so, Spring auto wired it via constructor method – “`public Customer(Person person)`“.

    <bean id="customer" class="com.mkyong.common.Customer" autowire="constructor" />

    <bean id="person" class="com.mkyong.common.Person" />

See full example – [Spring Autowiring by Constructor](http://www.mkyong.com/spring/spring-autowiring-by-constructor/).

## 5\. Auto-Wiring ‘autodetect’

If a default constructor is found, uses “constructor”; Otherwise, uses “byType”. In this case, since there is a default constructor in “Customer” class, so, Spring auto wired it via constructor method – “`public Customer(Person person)`“.

    <bean id="customer" class="com.mkyong.common.Customer" autowire="autodetect" />

    <bean id="person" class="com.mkyong.common.Person" />

See full example – [Spring Autowiring by AutoDetect](http://www.mkyong.com/spring/spring-autowiring-by-autodetect/).

**Note**  
It’s always good to combine both ‘auto-wire’ and ‘dependency-check’ together, to make sure the property is always auto-wire successfully.

    <bean id="customer" class="com.mkyong.common.Customer"
    		autowire="autodetect" dependency-check="objects />

    <bean id="person" class="com.mkyong.common.Person" />

## Conclusion

In my view, Spring ‘auto-wiring’ make development faster with great costs – it added complexity for the entire bean configuration file, and you don’t even know which bean will auto wired in which bean.

In practice, i rather wire it manually, it is always clean and work perfectly, or better uses [@Autowired annotation](http://www.mkyong.com/spring/spring-auto-wiring-beans-with-autowired-annotation/), which is more flexible and recommended.

[http://www.mkyong.com/spring/spring-auto-wiring-beans-in-xml/](http://www.mkyong.com/spring/spring-auto-wiring-beans-in-xml/)
