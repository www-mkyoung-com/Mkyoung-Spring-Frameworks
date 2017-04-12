The ‘**MapFactoryBean**‘ class provides developer a way to create a concrete Map collection class (HashMap and TreeMap) in Spring’s bean configuration file.

Here’s a MapFactoryBean example, it will instantiate a HashMap at runtime,, and inject it into a bean property.

    package com.mkyong.common;

    import java.util.Map;

    public class Customer
    {
    	private Map maps;
    	//...
    }

Spring’s bean configuration file.

    <beans xmlns="http://www.springframework.org/schema/beans"
    	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    	xsi:schemaLocation="http://www.springframework.org/schema/beans
    	http://www.springframework.org/schema/beans/spring-beans-2.5.xsd">

    	<bean id="CustomerBean" class="com.mkyong.common.Customer">
    		<property name="maps">
    			<bean class="org.springframework.beans.factory.config.MapFactoryBean">
    				<property name="targetMapClass">
    					<value>java.util.HashMap</value>
    				</property>
    				<property name="sourceMap">
    					<map>
    						<entry key="Key1" value="1" />
    						<entry key="Key2" value="2" />
    						<entry key="Key3" value="3" />
    					</map>
    				</property>
    			</bean>
    		</property>
    	</bean>

    </beans>

Alternatively, you also can use util schema and <util:map> to achieve the same thing.

    <beans xmlns="http://www.springframework.org/schema/beans"
    	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    	xmlns:util="http://www.springframework.org/schema/util"
    	xsi:schemaLocation="http://www.springframework.org/schema/beans
    	http://www.springframework.org/schema/beans/spring-beans-2.5.xsd
    	http://www.springframework.org/schema/util
    	http://www.springframework.org/schema/util/spring-util-2.5.xsd">

    	<bean id="CustomerBean" class="com.mkyong.common.Customer">
    		<property name="maps">
    			<util:map map-class="java.util.HashMap">
    				<entry key="Key1" value="1" />
    				<entry key="Key2" value="2" />
    				<entry key="Key3" value="3" />
    			</util:map>
    		</property>
    	</bean>

    </beans>

Remember to include the util schema, else you will hit the following error

    Caused by: org.xml.sax.SAXParseException:
    	The prefix "util" for element "util:map" is not bound.

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

    Customer [maps={Key2=2, Key1=1, Key3=3}] Type=[class java.util.HashMap]

You have instantiated a HashMap and injected it into Customer’s map property at runtime.

[http://www.mkyong.com/spring/spring-mapfactorybean-example/](http://www.mkyong.com/spring/spring-mapfactorybean-example/)
