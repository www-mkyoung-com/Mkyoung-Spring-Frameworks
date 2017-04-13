The Spring EL is similar with OGNL and JSF EL, and evaluated or executed during the bean creation time. In addition, all Spring expressions are available via XML or annotation.

In this tutorial, we show you how to use **Spring Expression Language(SpEL)**, to inject String, integer and bean into property, both in XML and annotation.

## 1\. Spring EL Dependency

Declares the core Spring jars in Maven `pom.xml` file, it will download the Spring EL dependencies automatically.

_File : pom.xml_

    <properties>
    	<spring.version>3.0.5.RELEASE</spring.version>
    </properties>

    <dependencies>

    	<!-- Spring 3 dependencies -->
    	<dependency>
    		<groupId>org.springframework</groupId>
    		<artifactId>spring-core</artifactId>
    		<version>${spring.version}</version>
    	</dependency>

    	<dependency>
    		<groupId>org.springframework</groupId>
    		<artifactId>spring-context</artifactId>
    		<version>${spring.version}</version>
    	</dependency>

    <dependencies>

## 2\. Spring Beans

Two simple beans, later use SpEL to inject values into property, in XML and annotation.

    package com.mkyong.core;

    public class Customer {

    	private Item item;

    	private String itemName;

    }

    package com.mkyong.core;

    public class Item {

    	private String name;

    	private int qty;

    }

## 3\. Spring EL in XML

The SpEL are enclosed with `#{ SpEL expression }`, see following example in XML bean definition file.

    <beans xmlns="http://www.springframework.org/schema/beans"
    	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    	xsi:schemaLocation="http://www.springframework.org/schema/beans
    	http://www.springframework.org/schema/beans/spring-beans-3.0.xsd">

    	<bean id="itemBean" class="com.mkyong.core.Item">
    		<property name="name" value="itemA" />
    		<property name="qty" value="10" />
    	</bean>

    	<bean id="customerBean" class="com.mkyong.core.Customer">
    		<property name="item" value="#{itemBean}" />
    		<property name="itemName" value="#{itemBean.name}" />
    	</bean>

    </beans>

1.  **#{itemBean}** – inject “itemBean” into “customerBean” bean’s “item” property.
2.  **#{itemBean.name}** – inject “itemBean”‘s “name” property into “customerBean” bean’s “itemName” property.

## 4\. Spring EL in Annotation

See equivalent version in annotation mode.

**Note**  
To use SpEL in annotation, you must register your component via annotation. If you register your bean in XML and define `@Value` in Java class, the `@Value` will failed to execute.

    package com.mkyong.core;

    import org.springframework.beans.factory.annotation.Value;
    import org.springframework.stereotype.Component;

    @Component("customerBean")
    public class Customer {

    	@Value("#{itemBean}")
    	private Item item;

    	@Value("#{itemBean.name}")
    	private String itemName;

    	//...

    }

    package com.mkyong.core;

    import org.springframework.beans.factory.annotation.Value;
    import org.springframework.stereotype.Component;

    @Component("itemBean")
    public class Item {

    	@Value("itemA") //inject String directly
    	private String name;

    	@Value("10") //inject interger directly
    	private int qty;

    	public String getName() {
    		return name;
    	}

    	//...
    }

Enable auto component scanning.

    <beans xmlns="http://www.springframework.org/schema/beans"
    	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    	xmlns:context="http://www.springframework.org/schema/context"
    	xsi:schemaLocation="http://www.springframework.org/schema/beans
    	http://www.springframework.org/schema/beans/spring-beans-3.0.xsd
    	http://www.springframework.org/schema/context
    	http://www.springframework.org/schema/context/spring-context-3.0.xsd">

    	<context:component-scan base-package="com.mkyong.core" />

    </beans>

In annotation mode, you use `@Value` to define Spring EL. In this case, you inject a String and Integer value directly into the “**itemBean**“, and later inject the “itemBean” into “**customerBean**” property.

## 5\. Output

Run it, both SpEL in XML and annotation are display the same result :

    package com.mkyong.core;

    import org.springframework.context.ApplicationContext;
    import org.springframework.context.support.ClassPathXmlApplicationContext;

    public class App {
    	public static void main(String[] args) {
    	    ApplicationContext context = new ClassPathXmlApplicationContext("SpringBeans.xml");

    	    Customer obj = (Customer) context.getBean("customerBean");
    	    System.out.println(obj);
    	}
    }

_Output_

    Customer [item=Item [name=itemA, qty=10], itemName=itemA]

[http://www.mkyong.com/spring3/spring-el-hello-world-example/](http://www.mkyong.com/spring3/spring-el-hello-world-example/)
