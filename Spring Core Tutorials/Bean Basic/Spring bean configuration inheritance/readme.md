In Spring, the inheritance is supported in bean configuration for a bean to share common values, properties or configurations.

A child bean or inherited bean can inherit its parent bean configurations, properties and some attributes. In additional, the child beans are allow to override the inherited value.

See following full example to show you how bean configuration inheritance works in Spring.

    package com.mkyong.common;

    public class Customer {

    	private int type;
    	private String action;
    	private String Country;

    	//...

    }

Bean configuration file

    <beans xmlns="http://www.springframework.org/schema/beans"
    	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    	xsi:schemaLocation="http://www.springframework.org/schema/beans
    	http://www.springframework.org/schema/beans/spring-beans-2.5.xsd">

    	<bean id="BaseCustomerMalaysia" class="com.mkyong.common.Customer">
    		<property name="country" value="Malaysia" />
    	</bean>

    	<bean id="CustomerBean" parent="BaseCustomerMalaysia">
    		<property name="action" value="buy" />
    		<property name="type" value="1" />
    	</bean>

    </beans>

Above is a ‘BaseCustomerMalaysia’ bean contains a ‘Malaysia’ value for country property, and the ‘CustomerBean’ bean inherited this value from its parent (‘BaseCustomerMalaysia’).

Run it

    package com.mkyong.common;

    import org.springframework.context.ApplicationContext;
    import org.springframework.context.support.ClassPathXmlApplicationContext;

    public class App
    {
        public static void main( String[] args )
        {
        	ApplicationContext context =
    			new ClassPathXmlApplicationContext("SpringBeans.xml");

        	Customer cust = (Customer)context.getBean("CustomerBean");
        	System.out.println(cust);

        }
    }

output

    Customer [type=1, action=buy, Country=Malaysia]

The ‘CustomerBean’ bean just inherited the country property from its parent (‘BaseCustomerMalaysia’).

## Inheritance with abstract

In above example, the ‘BaseCustomerMalaysia’ is still able to instantiate, for example,

    Customer cust = (Customer)context.getBean("BaseCustomerMalaysia");

If you want to make this base bean as a template and not allow others to instantiate it, you can add an ‘**abstract**‘ attribute in the <bean> element. For example

    <beans xmlns="http://www.springframework.org/schema/beans"
    	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    	xsi:schemaLocation="http://www.springframework.org/schema/beans
    	http://www.springframework.org/schema/beans/spring-beans-2.5.xsd">

    	<bean id="BaseCustomerMalaysia" class="com.mkyong.common.Customer" abstract="true">
    		<property name="country" value="Malaysia" />
    	</bean>

    	<bean id="CustomerBean" parent="BaseCustomerMalaysia">
    		<property name="action" value="buy" />
    		<property name="type" value="1" />
    	</bean>

    </beans>

Now, the ‘BaseCustomerMalaysia’ bean is a pure template, for bean to inherit it only, if you try to instantiate it, you will encounter the following error message.

    Customer cust = (Customer)context.getBean("BaseCustomerMalaysia");

    org.springframework.beans.factory.BeanIsAbstractException:
    	Error creating bean with name 'BaseCustomerMalaysia':
    	Bean definition is abstract

## Pure Inheritance Template

Actually, parent bean is not necessary to define class attribute, often times, you may just need a common property for sharing. Here’s is an example

    <beans xmlns="http://www.springframework.org/schema/beans"
    	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    	xsi:schemaLocation="http://www.springframework.org/schema/beans
    	http://www.springframework.org/schema/beans/spring-beans-2.5.xsd">

    	<bean id="BaseCustomerMalaysia" abstract="true">
    		<property name="country" value="Malaysia" />
    	</bean>

    	<bean id="CustomerBean" parent="BaseCustomerMalaysia"
    	    class="com.mkyong.common.Customer">

    		<property name="action" value="buy" />
    		<property name="type" value="1" />
    	</bean>

    </beans>

In this case, the ‘BaseCustomerMalaysia’ bean is a pure template, to share its ‘country’ property only.

## Overrride it

However, you are still allow to override the inherited value by specify the new value in the child bean. Let’s see this example

    <beans xmlns="http://www.springframework.org/schema/beans"
    	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    	xsi:schemaLocation="http://www.springframework.org/schema/beans
    	http://www.springframework.org/schema/beans/spring-beans-2.5.xsd">

    	<bean id="BaseCustomerMalaysia" class="com.mkyong.common.Customer" abstract="true">
    		<property name="country" value="Malaysia" />
    	</bean>

    	<bean id="CustomerBean" parent="BaseCustomerMalaysia">
    	    <property name="country" value="Japan" />
    		<property name="action" value="buy" />
    		<property name="type" value="1" />
    	</bean>

    </beans>

The ‘CustomerBean’ bean is just override the parent (‘BaseCustomerMalaysia’) country property, from ‘Malaysia’ to ‘Japan’.

    Customer [Country=Japan, action=buy, type=1]

## Conclusion

The Spring bean configuration inheritance is very useful to avoid the repeated common value or configurations for multiple beans.

[http://www.mkyong.com/spring/spring-bean-configuration-inheritance/](http://www.mkyong.com/spring/spring-bean-configuration-inheritance/)
