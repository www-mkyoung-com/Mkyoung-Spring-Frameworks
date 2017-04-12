Spring examples to show you how to inject values into collections type (List, Set, Map, and Properties). 4 major collection types are supported :

*   List – <list/>
*   Set – <set/>
*   Map – <map/>
*   Properties – <props/>

## Spring beans

A Customer object, with four collection properties.

    package com.mkyong.common;

    import java.util.List;
    import java.util.Map;
    import java.util.Properties;
    import java.util.Set;

    public class Customer
    {
    	private List<Object> lists;
    	private Set<Object> sets;
    	private Map<Object, Object> maps;
    	private Properties pros;

    	//...
    }

See different code snippets to declare collection in bean configuration file.

## 1\. List example

    <property name="lists">
    	<list>
    		<value>1</value>
    		<ref bean="PersonBean" />
    		<bean class="com.mkyong.common.Person">
    			<property name="name" value="mkyongList" />
    			<property name="address" value="address" />
    			<property name="age" value="28" />
    		</bean>
    	</list>
    </property>

## 2\. Set example

    <property name="sets">
    	<set>
    		<value>1</value>
    		<ref bean="PersonBean" />
    		<bean class="com.mkyong.common.Person">
    			<property name="name" value="mkyongSet" />
    			<property name="address" value="address" />
    			<property name="age" value="28" />
    		</bean>
    	</set>
    </property>

## 3\. Map example

    <property name="maps">
    	<map>
    		<entry key="Key 1" value="1" />
    		<entry key="Key 2" value-ref="PersonBean" />
    		<entry key="Key 3">
    			<bean class="com.mkyong.common.Person">
    				<property name="name" value="mkyongMap" />
    				<property name="address" value="address" />
    				<property name="age" value="28" />
    			</bean>
    		</entry>
    	</map>
    </property>

## 4\. Properties example

    <property name="pros">
    	<props>
    		<prop key="admin">admin@nospam.com</prop>
    		<prop key="support">support@nospam.com</prop>
    	</props>
    </property>

Full Spring’s bean configuration file.

    <beans xmlns="http://www.springframework.org/schema/beans"
    	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    	xsi:schemaLocation="http://www.springframework.org/schema/beans
    	http://www.springframework.org/schema/beans/spring-beans-2.5.xsd">

    	<bean id="CustomerBean" class="com.mkyong.common.Customer">

    		<!-- java.util.List -->
    		<property name="lists">
    			<list>
    				<value>1</value>
    				<ref bean="PersonBean" />
    				<bean class="com.mkyong.common.Person">
    					<property name="name" value="mkyongList" />
    					<property name="address" value="address" />
    					<property name="age" value="28" />
    				</bean>
    			</list>
    		</property>

    		<!-- java.util.Set -->
    		<property name="sets">
    			<set>
    				<value>1</value>
    				<ref bean="PersonBean" />
    				<bean class="com.mkyong.common.Person">
    					<property name="name" value="mkyongSet" />
    					<property name="address" value="address" />
    					<property name="age" value="28" />
    				</bean>
    			</set>
    		</property>

    		<!-- java.util.Map -->
    		<property name="maps">
    			<map>
    				<entry key="Key 1" value="1" />
    				<entry key="Key 2" value-ref="PersonBean" />
    				<entry key="Key 3">
    					<bean class="com.mkyong.common.Person">
    						<property name="name" value="mkyongMap" />
    						<property name="address" value="address" />
    						<property name="age" value="28" />
    					</bean>
    				</entry>
    			</map>
    		</property>

    		<!-- java.util.Properties -->
    		<property name="pros">
    			<props>
    				<prop key="admin">admin@nospam.com</prop>
    				<prop key="support">support@nospam.com</prop>
    			</props>
    		</property>

    	</bean>

    	<bean id="PersonBean" class="com.mkyong.common.Person">
    		<property name="name" value="mkyong1" />
    		<property name="address" value="address 1" />
    		<property name="age" value="28" />
    	</bean>

    </beans>

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

Output

    Customer [

    lists=[
    1,
    Person [address=address 1, age=28, name=mkyong1],
    Person [address=address, age=28, name=mkyongList]
    ],

    maps={
    key 1=1,
    key 2=Person [address=address 1, age=28, name=mkyong1],
    key 3=Person [address=address, age=28, name=mkyongMap]
    },

    pros={admin=admin@nospam.com, support=support@nospam.com},

    sets=[
    1,
    Person [address=address 1, age=28, name=mkyong1],
    Person [address=address, age=28, name=mkyongSet]]
    ]

[http://www.mkyong.com/spring/spring-collections-list-set-map-and-properties-example/](http://www.mkyong.com/spring/spring-collections-list-set-map-and-properties-example/)
