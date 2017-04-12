In Spring framework, whenever a bean is used for only one particular property, it’s advise to declare it as an inner bean. And the inner bean is supported both in setter injection ‘`property`‘ and constructor injection ‘`constructor-arg`‘.

See a detail example to demonstrate the use of Spring inner bean.

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

    	@Override
    	public String toString() {
    		return "Customer [person=" + person + "]";
    	}
    }

    package com.mkyong.common;

    public class Person
    {
    	private String name;
    	private String address;
    	private int age;

    	//getter and setter methods

    	@Override
    	public String toString() {
    		return "Person [address=" + address + ",
                                   age=" + age + ", name=" + name + "]";
    	}
    }

Often times, you may use ‘`ref`‘ attribute to reference the “Person” bean into “Customer” bean, person property as following :

    <beans xmlns="http://www.springframework.org/schema/beans"
    	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    	xsi:schemaLocation="http://www.springframework.org/schema/beans
    	http://www.springframework.org/schema/beans/spring-beans-2.5.xsd">

    	<bean id="CustomerBean" class="com.mkyong.common.Customer">
    		<property name="person" ref="PersonBean" />
    	</bean>

    	<bean id="PersonBean" class="com.mkyong.common.Person">
    		<property name="name" value="mkyong" />
    		<property name="address" value="address1" />
    		<property name="age" value="28" />
    	</bean>

    </beans>

In general, it’s fine to reference like this, but since the ‘mkyong’ person bean is only used for Customer bean only, it’s better to declare this ‘mkyong’ person as an inner bean as following :

    <beans xmlns="http://www.springframework.org/schema/beans"
    	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    	xsi:schemaLocation="http://www.springframework.org/schema/beans
    	http://www.springframework.org/schema/beans/spring-beans-2.5.xsd">

    	<bean id="CustomerBean" class="com.mkyong.common.Customer">
    		<property name="person">
    			<bean class="com.mkyong.common.Person">
    				<property name="name" value="mkyong" />
    				<property name="address" value="address1" />
    				<property name="age" value="28" />
    			</bean>
    		</property>
    	</bean>
    </beans>

This inner bean also supported in constructor injection as following :

    <beans xmlns="http://www.springframework.org/schema/beans"
    	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    	xsi:schemaLocation="http://www.springframework.org/schema/beans
    	http://www.springframework.org/schema/beans/spring-beans-2.5.xsd">

    	<bean id="CustomerBean" class="com.mkyong.common.Customer">
    		<constructor-arg>
    			<bean class="com.mkyong.common.Person">
    				<property name="name" value="mkyong" />
    				<property name="address" value="address1" />
    				<property name="age" value="28" />
    			</bean>
    		</constructor-arg>
    	</bean>
    </beans>

**Note**  
The id or name value in bean class is not necessary in an inner bean, it will simply ignored by the Spring container.

Run it

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

_Output_

    Customer [person=Person [address=address1, age=28, name=mkyong]]

[http://www.mkyong.com/spring/spring-inner-bean-examples/](http://www.mkyong.com/spring/spring-inner-bean-examples/)
