The ‘**SetFactoryBean**‘ class provides developer a way to create a concrete Set collection (HashSet and TreeSet) in Spring’s bean configuration file.

Here’s a ListFactoryBean example, it will instantiate an HashSet at runtime, and inject it into a bean property

    package com.mkyong.common;

    import java.util.Set;

    public class Customer
    {
    	private Set sets;
    	//...
    }

Spring’s bean configuration file.

    <beans xmlns="http://www.springframework.org/schema/beans"
    	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    	xsi:schemaLocation="http://www.springframework.org/schema/beans
    	http://www.springframework.org/schema/beans/spring-beans-2.5.xsd">

    	<bean id="CustomerBean" class="com.mkyong.common.Customer">
    		<property name="sets">
    			<bean class="org.springframework.beans.factory.config.SetFactoryBean">
    				<property name="targetSetClass">
    					<value>java.util.HashSet</value>
    				</property>
    				<property name="sourceSet">
    					<list>
    						<value>1</value>
    						<value>2</value>
    						<value>3</value>
    					</list>
    				</property>
    			</bean>
    		</property>
    	</bean>

    </beans>

Alternatively, you also can use util schema and <util:set> to achieve the same thing.

    <beans xmlns="http://www.springframework.org/schema/beans"
    	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    	xmlns:util="http://www.springframework.org/schema/util"
    	xsi:schemaLocation="http://www.springframework.org/schema/beans
    	http://www.springframework.org/schema/beans/spring-beans-2.5.xsd
    	http://www.springframework.org/schema/util
    	http://www.springframework.org/schema/util/spring-util-2.5.xsd">

    	<bean id="CustomerBean" class="com.mkyong.common.Customer">
    		<property name="sets">
    			<util:set set-class="java.util.HashSet">
    				<value>1</value>
    				<value>2</value>
    				<value>3</value>
    			</util:set>
    		</property>
    	</bean>

    </beans>

Remember to include the util schema, else you will hit the following error

    Caused by: org.xml.sax.SAXParseException:
    	The prefix "util" for element "util:set" is not bound.

Run it…

    package com.mkyong.common;

    import org.springframework.context.ApplicationContext;
    import org.springframework.context.support.ClassPathXmlApplicationContext;

    public class App
    {
        public static void main( String[] args )
        {
        	ApplicationContext context = new ClassPathXmlApplicationContext("SpringBeans.xml");

        	Customer cust = (Customer)context.getBean("CustomerBean");
        	System.out.println(cust);

        }
    }

Ouput

    Customer [sets=[3, 2, 1]] Type=[class java.util.HashSet]

You have instantiated HashSet and and injected it into Customer’s sets property at runtime.

[http://www.mkyong.com/spring/spring-setfactorybean-example/](http://www.mkyong.com/spring/spring-setfactorybean-example/)
