In Spring framework, when your class contains multiple constructors with same number of arguments, it will always cause the **constructor injection argument type ambiguities** issue.

## Problem

Let’s see this customer bean example. It contains two constructor methods, both accept 3 arguments with different data type.

    package com.mkyong.common;

    public class Customer
    {
    	private String name;
    	private String address;
    	private int age;

    	public Customer(String name, String address, int age) {
    		this.name = name;
    		this.address = address;
    		this.age = age;
    	}

    	public Customer(String name, int age, String address) {
    		this.name = name;
    		this.age = age;
    		this.address = address;
    	}
    	//getter and setter methods
    	public String toString(){
    		return " name : " +name + "\n address : "
                   + address + "\n age : " + age;
    	}

    }

In Spring bean configuration file, pass a ‘mkyong’ for name, ‘188’ for address and ’28’ for age.

    <!--Spring-Customer.xml-->
    <beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://www.springframework.org/schema/beans
    http://www.springframework.org/schema/beans/spring-beans-2.5.xsd">

    	<bean id="CustomerBean" class="com.mkyong.common.Customer">

    		<constructor-arg>
    			<value>mkyong</value>
    		</constructor-arg>

    		<constructor-arg>
    			<value>188</value>
    		</constructor-arg>

    		<constructor-arg>
    			<value>28</value>
    		</constructor-arg>
            </bean>

    </beans>

Run it, what’s your expected result?

    package com.mkyong.common;

    import org.springframework.context.ApplicationContext;
    import org.springframework.context.support.ClassPathXmlApplicationContext;

    public class App
    {
        public static void main( String[] args )
        {
        	ApplicationContext context =
        	  new ClassPathXmlApplicationContext(new String[] {"Spring-Customer.xml"});

        	Customer cust = (Customer)context.getBean("CustomerBean");
        	System.out.println(cust);
        }
    }

Output

    name : mkyong
    address : 28
    age : 188

The result is not what we expected, the second constructor is run, instead of the first constructor. In Spring, the argument type ‘188’ is capable convert to int, so Spring just convert it and take the second constructor, even you assume it should be a String.

In addition, if Spring can’t resolve which constructor to use, it will prompt following error message

    constructor arguments specified but no matching constructor
    found in bean 'CustomerBean' (hint: specify index and/or
    type arguments for simple parameters to avoid type ambiguities)

## Solution

To fix it, you should always specify the exact data type for constructor, via type attribute like this :

    <beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://www.springframework.org/schema/beans
    http://www.springframework.org/schema/beans/spring-beans-2.5.xsd">

    	<bean id="CustomerBean" class="com.mkyong.common.Customer">

    		<constructor-arg type="java.lang.String">
    			<value>mkyong</value>
    		</constructor-arg>

    		<constructor-arg type="java.lang.String">
    			<value>188</value>
    		</constructor-arg>

    		<constructor-arg type="int">
    			<value>28</value>
    		</constructor-arg>

    	</bean>

    </beans>

Run it again, now you get what you expected.

Output

    name : mkyong
    address : 188
    age : 28

[http://www.mkyong.com/spring/constructor-injection-type-ambiguities-in-spring/](http://www.mkyong.com/spring/constructor-injection-type-ambiguities-in-spring/)
